package com.teamproject.inspectionframework.Application_Layer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.teamproject.inspectionframework.AssignmentList;
import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Attachment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

/**
 * Provides functions for synchronizing data with the server
 *
 */
public class SynchronizationHelper {

	private MySQLiteHelper datasource;
	private HttpCustomClient restInstance;
	private HttpCustomClient putrestInstance;
	private InternetConnectionDetector icd;
	private ParseJSON parser = new ParseJSON();
	private Context ctx;
	private List<String> versionErrorList;
	private String userId;
	private Activity activity;

	public SynchronizationHelper() {

	}

	/**
	 * Triggers the synchronization of assignments for the invoking user
	 * 
	 * @param ctx
	 *            The context of the invoking activity
	 * @param userId
	 *            The ID of the invoking user
	 * @param activity
	 *            The activity object of the invoking activity
	 */
	public void SynchronizeAssignments(Context ctx, String userId, Activity activity) {

		datasource = new MySQLiteHelper(ctx);
		restInstance = new HttpCustomClient();
		putrestInstance = new HttpCustomClient();
		icd = new InternetConnectionDetector(ctx);
		this.ctx = ctx;
		this.userId = userId;
		this.activity = activity;

		versionErrorList = new ArrayList<String>();

		if (icd.isConnectedToInternet() == true) {

			// UPLOAD-PART
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++

			try {
				String putJObject = null;
				List<Assignment> listWithAllAssignmentsByUser = datasource.getAssignmentsByUserId(userId);

				for (int i = 0; i < listWithAllAssignmentsByUser.size(); i++) {
					Assignment assignment = listWithAllAssignmentsByUser.get(i);

					User user = datasource.getUserByUserId(userId);
					InspectionObject inspectionObject = datasource.getInspectionObjectById(assignment.getInspectionObjectId());

					List<Task> taskList = datasource.getTasksByAssignmentId(assignment.getId());

					// Upload the assignment with all related tasks
					putJObject = parser.completeAssignmentToJson(assignment, taskList, user, inspectionObject);

					Integer statusResponse = restInstance.putToHerokuServer("assignment", putJObject, assignment.getId());

					// Records version errors
					if (statusResponse == 400) {
						versionErrorList.add(assignment.getId());
					}

					if (statusResponse == 204) {
						// Post all attachments related to an assignment
						List<Attachment> attachmentList = new ArrayList<Attachment>();
						attachmentList = datasource.getAttachmentsByAssignmentId(assignment.getId());

						if (attachmentList != null) {
							for (int j = 0; j < attachmentList.size(); j++) {
								Attachment attachment = attachmentList.get(j);
								putrestInstance.postAttachmentToHerokuServer(assignment.getId(), attachment.getTaskId(), attachment.getBinaryObject());
							}
						}

						datasource.deleteInspectionObject(assignment.getInspectionObjectId());
						datasource.deleteAssignment(assignment.getId());

						for (int j = 0; j < taskList.size(); j++) {
							Task task = taskList.get(j);
							datasource.deleteTask(task.getId());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			restInstance.client.getConnectionManager().closeExpiredConnections();

			// DOWNLOAD-PART
			// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++

			String inputAssignment = restInstance.readHerokuServer("assignment?user_id=" + userId);

			try {
				JSONArray jArray = new JSONArray(inputAssignment);

				for (int i = 0; i < jArray.length(); i++) {
					Assignment assignment = new Assignment();
					JSONObject jObject = jArray.getJSONObject(i);

					// Filters the input stream: Don't pick templates,
					// finished assignments and assignments where a local
					// version error occurred
					if (jObject.get("isTemplate").toString().equals("true") || jObject.getInt("state") == 2 || versionErrorList.contains(jObject.get("id").toString())) {
						continue;
					}

					// Set values for an assignment
					assignment.setDescription(jObject.get("description").toString());
					assignment.setAssignmentName(jObject.get("assignmentName").toString());
					assignment.setId(jObject.get("id").toString());
					assignment.setIsTemplate((jObject.get("isTemplate").toString()));
					assignment.setStartDate(jObject.getLong("startDate"));
					assignment.setDueDate(jObject.getLong("endDate"));
					assignment.setInspectionObjectId(jObject.get("isTemplate").toString());
					assignment.setState(jObject.getInt("state"));
					assignment.setVersion(jObject.getInt("version"));

					// Set values for all tasks
					JSONArray jArrayTask = new JSONArray(jObject.get("tasks").toString());

					for (int j = 0; j < jArrayTask.length(); j++) {
						Task task = new Task();
						JSONObject jObjectTask = jArrayTask.getJSONObject(j);
						task.setId(jObjectTask.get("id").toString());
						task.setDescription(jObjectTask.get("description").toString());
						task.setAssignmentId(assignment.getId());
						task.setState(jObjectTask.getInt("state"));
						task.setTaskName(jObjectTask.get("taskName").toString());
						task.setErrorDescription(jObjectTask.get("errorDescription").toString());

						datasource.createTask(task);
					}

					JSONObject jObjectInspectionObject = new JSONObject(jObject.get("inspectionObject").toString());
					InspectionObject inspectionObject = new InspectionObject();
					inspectionObject.setId(jObjectInspectionObject.get("id").toString());
					inspectionObject.setObjectName(jObjectInspectionObject.get("objectName").toString());
					inspectionObject.setCustomerName(jObjectInspectionObject.get("customerName").toString());
					inspectionObject.setDescription(jObjectInspectionObject.get("description").toString());
					inspectionObject.setLocation(jObjectInspectionObject.get("location").toString());

					// Store the inspection object into the database
					datasource.createInspectionObject(inspectionObject);

					assignment.setUserId(userId);
					assignment.setInspectionObjectId(inspectionObject.getId());

					// Store all assignments into the database
					datasource.createAssignment(assignment);
					datasource.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// A user prompt will be shown if version error(s) occurred
			if (versionErrorList.size() > 0) {
				for (int j = 0; j < versionErrorList.size(); j++) {

					Assignment assignment = datasource.getAssignmentById(versionErrorList.get(j));
					alertDialogHandler(assignment.getAssignmentName() + ": Version error", "Download new version and overwrite local or keep local?\nWarning: Assignments having the status 'finished' on the server will be removed from this device!", activity, j, assignment);
				}

			} else {
				Toast toast = Toast.makeText(ctx, R.string.toast_sync_success, Toast.LENGTH_SHORT);
				toast.show();
			}

		} else {
			Toast toast = Toast.makeText(ctx, R.string.toast_no_internet, Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	/**
	 * Downloads assignments and associated object for assignments where a
	 * version error occurred
	 * 
	 */
	private void syncAssignmentWithVersionError(Assignment assignment) {

		// Remove version from local database
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		datasource.deleteInspectionObject(assignment.getInspectionObjectId());
		datasource.deleteAssignment(assignment.getId());

		List<Task> taskList = datasource.getTasksByAssignmentId(assignment.getId());

		for (int j = 0; j < taskList.size(); j++) {
			Task task = taskList.get(j);
			datasource.deleteTask(task.getId());
		}

		// Download updated assignment version and related items
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		String inputAssignment = restInstance.readHerokuServer("assignment/" + assignment.getId());

		try {
			JSONObject jObject = new JSONObject(inputAssignment);
			Assignment downloadedAssignment = new Assignment();

			// Filters the input stream: Don't pick templates and finished
			// assignments
			if (jObject.get("isTemplate").toString().equals("true") || jObject.getInt("state") == 2) {
				;
			} else {

				// Set values for an assignment
				downloadedAssignment.setDescription(jObject.get("description").toString());
				downloadedAssignment.setAssignmentName(jObject.get("assignmentName").toString());
				downloadedAssignment.setId(jObject.get("id").toString());
				downloadedAssignment.setIsTemplate((jObject.get("isTemplate").toString()));
				downloadedAssignment.setStartDate(jObject.getLong("startDate"));
				downloadedAssignment.setDueDate(jObject.getLong("endDate"));
				downloadedAssignment.setInspectionObjectId(jObject.get("isTemplate").toString());
				downloadedAssignment.setState(jObject.getInt("state"));
				downloadedAssignment.setVersion(jObject.getInt("version"));

				// Set values for all tasks
				JSONArray jArrayTask = new JSONArray(jObject.get("tasks").toString());

				for (int j = 0; j < jArrayTask.length(); j++) {
					Task task = new Task();
					JSONObject jObjectTask = jArrayTask.getJSONObject(j);
					task.setId(jObjectTask.get("id").toString());
					task.setDescription(jObjectTask.get("description").toString());
					task.setAssignmentId(downloadedAssignment.getId());
					task.setState(jObjectTask.getInt("state"));
					task.setTaskName(jObjectTask.get("taskName").toString());
					task.setErrorDescription(jObjectTask.get("errorDescription").toString());

					datasource.createTask(task);
				}

				JSONObject jObjectInspectionObject = new JSONObject(jObject.get("inspectionObject").toString());
				InspectionObject inspectionObject = new InspectionObject();
				inspectionObject.setId(jObjectInspectionObject.get("id").toString());
				inspectionObject.setObjectName(jObjectInspectionObject.get("objectName").toString());
				inspectionObject.setCustomerName(jObjectInspectionObject.get("customerName").toString());
				inspectionObject.setDescription(jObjectInspectionObject.get("description").toString());
				inspectionObject.setLocation(jObjectInspectionObject.get("location").toString());

				// Store the inspection object into the database
				datasource.createInspectionObject(inspectionObject);

				downloadedAssignment.setUserId(userId);
				downloadedAssignment.setInspectionObjectId(inspectionObject.getId());

				// Store all assignments into the database
				datasource.createAssignment(downloadedAssignment);
				datasource.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the alert dialogue that occurs when a version error occurs
	 * 
	 * @param title
	 *            Title of the error message
	 * @param message
	 *            The error message
	 * @param activity
	 *            The activity object of the invoking activity
	 * @return The user's decision
	 */
	public void alertDialogHandler(String title, String message, Activity activity, int itemNumber, Assignment assignment) {
		final int ITEM = itemNumber;
		final Assignment ASSIGNMENT = assignment;
		final Activity act = this.activity;

		// make a text input dialog and show it
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setPositiveButton("Keep Local", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// ...

				if (ITEM == 0) {
					Intent updateList = new Intent(ctx, AssignmentList.class);
					act.startActivity(updateList);
					Toast toast = Toast.makeText(ctx, R.string.toast_sync_success, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		alert.setNegativeButton("Download", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				// Remove local version from database and download new version
				// from server
				syncAssignmentWithVersionError(ASSIGNMENT);

				if (ITEM == 0) {
					Intent updateList = new Intent(ctx, AssignmentList.class);
					act.startActivity(updateList);
					Toast toast = Toast.makeText(ctx, R.string.toast_sync_success, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		alert.show();
	}

	/**
	 * Performs the login process
	 * 
	 * @param ctx
	 *            The context of the invoking activity
	 * @param username
	 *            Username
	 * @param passwordPassword
	 * @return UserID at successful login; "0" otherwise
	 */
	public String UserLogin(Context ctx, String username, String password) {

		datasource = new MySQLiteHelper(ctx);
		restInstance = new HttpCustomClient();
		icd = new InternetConnectionDetector(ctx);
		String userId = "0";

		if (icd.isConnectedToInternet() == true) {

			if (restInstance.postToHerokuServer("login", username, password) == true) {

				try {

					JSONObject jObject = new JSONObject(restInstance.readHerokuServer("users/byusername/" + username));

					User user = new User();

					// Only ROLE_INSPECTOR users can log in
					if (!(jObject.get("role").toString()).equals("ROLE_INSPECTOR")) {
						Toast errorToast = Toast.makeText(ctx, "Only Inspectors can login at this client", Toast.LENGTH_SHORT);
						errorToast.show();

					} else {

						// get and set the values for the table user
						user.setUserId(jObject.get("id").toString());
						user.setUserName(jObject.get("userName").toString());
						user.setFirstName(jObject.get("firstName").toString());
						user.setLastName(jObject.get("lastName").toString());
						user.setRole(jObject.get("role").toString());
						user.setEmail(jObject.get("emailAddress").toString());
						user.setPhoneNumber(jObject.get("phoneNumber").toString());
						user.setMobileNumber(jObject.get("mobileNumber").toString());

						userId = user.getUserId();

						datasource.createUser(user);
						datasource.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Toast errorToast = Toast.makeText(ctx, "Wrong username or password", Toast.LENGTH_SHORT);
				errorToast.show();
			}
		} else {
			Toast errorToast = Toast.makeText(ctx, R.string.toast_no_internet, Toast.LENGTH_SHORT);
			errorToast.show();

		}
		return userId;
	}
}
