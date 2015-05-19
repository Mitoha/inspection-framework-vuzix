package com.teamproject.inspectionframework.Application_Layer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Attachment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

public class SynchronizationHelper {

    private MySQLiteHelper datasource;
    private HttpCustomClient restInstance;
    private HttpCustomClient putrestInstance;
    private InternetConnectionDetector icd;
    private ParseJSON parser = new ParseJSON();
    private boolean mResult = false;
    private boolean uploadReady;
    private boolean downloadReady;


    public SynchronizationHelper() {

    }

    public void SynchronizeAssignments(Context ctx, String userId, Activity activity) {

        datasource = new MySQLiteHelper(ctx);
        restInstance = new HttpCustomClient();
        putrestInstance = new HttpCustomClient();
        icd = new InternetConnectionDetector(ctx);
        uploadReady = false;
        downloadReady = false;

        List<String> noSyncList = new ArrayList<String>();

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
                    //Upload the assignment with all related tasks
                    putJObject = parser.completeAssignmentToJson(assignment, taskList, user, inspectionObject);
                    System.out.println(putJObject);
                    System.out.println(assignment.getState());
                    Integer statusResponse = restInstance.putToHerokuServer("assignment", putJObject, assignment.getId());
                    System.out.println("PUT:"+statusResponse);


                    // Gives the user to the choice to delete or keep the local
                    // version if upload is not possible due to version problems
                   if (statusResponse == 400) {



                        boolean userChoice = alertDialogHandler(assignment.getAssignmentName() + ": Version error", "Download new version and overwrite local or keep local?", activity);

                        // Keep local version
                        if (userChoice == true) {
                            noSyncList.add(assignment.getId());

                        }

                        // Download remote version
                        if (userChoice == false) {
                            // Continue with program
                        }
                    }

                    if (statusResponse == 204){
                        //Post all attachments related to an assignment
                        List<Attachment> attachmentList = new ArrayList<Attachment>();
                        attachmentList = datasource.getAttachmentsByAssignmentId(assignment.getId());

                        if(attachmentList != null) {
                            for (int j = 0; j < attachmentList.size(); j++) {
                                Attachment attachment = attachmentList.get(j);
                                putrestInstance.postAttachmentToHerokuServer(assignment.getId(), attachment.getTaskId(), attachment.getBinaryObject());
                                System.out.println("Attachments uploaded!");
                            }
                        }
                        uploadReady = true;
                    }

                    // Deletes all local instances in the database only when the assignment is final (state 2)
                    if (assignment.getState() == 2) {
                        datasource.deleteInspectionObject(assignment.getInspectionObjectId());
                        System.out.println("IO gelöscht!");
                        datasource.deleteAssignment(assignment.getId());

                        System.out.println("Assignment deleted!");
                        for (int j = 0; j < taskList.size(); j++) {
                            Task task = taskList.get(j);
                            datasource.deleteTask(task.getId());
                            System.out.println("Task:"+j);
                        }
                    }


                }
            System.out.println("Nicht gesynct: "+noSyncList);

            } catch (Exception e) {
                e.printStackTrace();
            }

            restInstance.client.getConnectionManager().closeExpiredConnections();
            // DOWNLOAD-PART
            // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            String inputAssignment = restInstance.readHerokuServer("assignment?user_id=" + userId);
            if (inputAssignment != null){
                downloadReady = true;
            }
            try {
                JSONArray jArray = new JSONArray(inputAssignment);

                for (int i = 0; i < jArray.length(); i++) {
                    Assignment assignment = new Assignment();
                    JSONObject jObject = jArray.getJSONObject(i);

                    JSONObject jObjectUser = new JSONObject(jObject.get("user").toString());

                    // Filters the input stream: Don't pick templates,
                    // finished assignments and assignments that don't get
                    // updated
                    if (jObject.get("isTemplate").toString() == "true" || jObject.getInt("state") == 2) {
                        continue;
                    }

                    // get and set the values for the table assignments
                    assignment.setDescription(jObject.get("description").toString());
                    assignment.setAssignmentName(jObject.get("assignmentName").toString());
                    assignment.setId(jObject.get("id").toString());
                    assignment.setIsTemplate((jObject.get("isTemplate").toString()));
                    assignment.setStartDate(jObject.getLong("startDate"));
                    assignment.setDueDate(jObject.getLong("endDate"));
                    assignment.setInspectionObjectId(jObject.get("isTemplate").toString());
                    assignment.setState(jObject.getInt("state"));
                    assignment.setVersion(jObject.getInt("version"));

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
                        task.setErrorDescription(jObjectTask.get("errorDescription").toString());

                        // Store all assigned tasks into the database
                        List<Task> taskList = new ArrayList<Task>();
                        taskList = datasource.getTasksByAssignmentId(assignment.getId());
                        for (int m=0; m<taskList.size(); m++){
                            Task task1 = new Task();
                            task1 = taskList.get(m);
                            if (task.getId().equals(task1.getId())){
                                datasource.deleteTask(task1.getId());

                            }
                        }

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
                    // Store all assigned tasks into the database
                    List<Assignment> assignmentList = new ArrayList<Assignment>();
                    assignmentList = datasource.getAllAssignments();
                    int state = 0;
                    System.out.println("Status: 0");

                    for (int m=0; m<assignmentList.size(); m++){
                        Assignment assignment1 = new Assignment();
                        assignment1 = assignmentList.get(m);

                        if (assignment.getId().equals(assignment1.getId())){
                        state = 1;
                            System.out.println("Status: 1");
                        for (int b = 0; b < noSyncList.size(); b++){
                            String noSyncedId = noSyncList.get(b);
                            if (assignment.getId().equals(noSyncedId)) {
                                assignment1.setVersion(assignment.getVersion());
                                datasource.updateAssignment(assignment1);
                                state = 2;

                            }
                            if (state == 2){
                                System.out.println("Status: 2");
                                break;
                        }
                    }



                }
                    //Handles accorcing to the state
                    if (state == 1){
                        datasource.updateAssignment(assignment);
                        break;
                    }
               }
                    if(state==0){
                        datasource.createAssignment(assignment);
                    }
              }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast errorToast = Toast.makeText(ctx, R.string.toast_no_internet, Toast.LENGTH_SHORT);
            errorToast.show();
        }


            Toast errorToast = Toast.makeText(ctx, R.string.toast_sync_success, Toast.LENGTH_SHORT);
            errorToast.show();

    }

    public boolean alertDialogHandler(String title, String message, Activity activity) {
        // make a handler that throws a runtime exception when a message is
        // received

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };

        // make a text input dialog and show it
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Keep", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.setNegativeButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mResult = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        // loop till a runtime exception is triggered.
        try {
            Looper.loop();
        } catch (RuntimeException e2) {
        }

        return mResult;
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
