package com.teamproject.inspectionframework.Application_Layer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

public class SynchronizationHelper {

	private MySQLiteHelper datasource;
	private HttpCustomClient restInstance;
	private InternetConnectionDetector icd;

	public SynchronizationHelper() {

	}

	public void SynchronizeAssignments(Context ctx, String userId) {

		datasource = new MySQLiteHelper(ctx);
		restInstance = new HttpCustomClient();
		icd = new InternetConnectionDetector(ctx);

		// DOWNLOAD-PART +++++++++++++++++++++++++++
		
		if (icd.isConnectedToInternet() == true) {
			String inputAssignment = restInstance.readHerokuServer("assignment");

			try {
				JSONArray jArray = new JSONArray(inputAssignment);

				for (int i = 0; i < jArray.length(); i++) {
					Assignment assignment = new Assignment();
					JSONObject jObject = jArray.getJSONObject(i);

					JSONObject jObjectUser = new JSONObject(jObject.get("user").toString());

					// Checks if assignment is template
					if (jObject.get("isTemplate").toString() == "true") {
						continue;
					}

					// Check if user is correct, i.e. the one who is currently logged-in
					if (!userId.equals(jObjectUser.get("id").toString())) {
						continue;
					}

					// get and set the values for the table assignments
					assignment.setDescription(jObject.get("description").toString());
					assignment.setAssignmentName(jObject.get("assignmentName").toString());
					assignment.setId(jObject.get("id").toString());
					assignment.setStartDate(jObject.getLong("startDate"));
					assignment.setDueDate(jObject.getLong("endDate"));
					assignment.setInspectionObjectId(jObject.get("isTemplate").toString());
					assignment.setState(jObject.getInt("state"));

					// Download all tasks assigned to an assignment from the
					// server
					// jArrayTask gets the SubJSONObject "tasks"

					JSONArray jArrayTask = new JSONArray(jObject.get("tasks").toString());

					for (int j = 0; j < jArrayTask.length(); j++) {
						Task task = new Task();
						JSONObject jObjectTask = jArrayTask.getJSONObject(j);
						task.setId(jObjectTask.get("id").toString());
						task.setDescription(jObjectTask.get("description").toString());
						task.setAssignmentId(assignment.getId());

						// Checks if Task state is set
						if (jObjectTask.isNull("state")) {
							task.setState(0);
						} else {
							task.setState(jObjectTask.getInt("state"));
						}

						task.setTaskName(jObjectTask.get("taskName").toString());

						// Store all assigned tasks into the database
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

					assignment.setUserId(jObjectUser.get("id").toString());
					assignment.setInspectionObjectId(inspectionObject.getId());

					// Store all assignments into the database
					datasource.createAssignment(assignment);
					datasource.close();

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast errorToast = Toast.makeText(ctx, R.string.toast_no_internet, Toast.LENGTH_SHORT);
			errorToast.show();
		}
	}

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
