package com.teamproject.inspectionframework.Application_Layer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.content.Context;

public class SynchronizationHelper {

	private MySQLiteHelper datasource;
	private HttpCustomClient restInstance;

	public SynchronizationHelper() {

	}

	public void SynchronizeAssignments(Context ctx) {

		datasource = new MySQLiteHelper(ctx);
		restInstance = new HttpCustomClient();

		String inputAssignment = restInstance.readHerokuServer("assignment");

		try {
			JSONArray jArray = new JSONArray(inputAssignment);

			for (int i = 0; i < jArray.length(); i++) {
				Assignment assignment = new Assignment();
				JSONObject jObject = jArray.getJSONObject(i);

				// Checks if assignment is template
				if (jObject.get("isTemplate").toString() == "true") {
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

				JSONObject jObjectUser = new JSONObject(jObject.get("user").toString());

				assignment.setUserId(jObjectUser.get("id").toString());
				assignment.setInspectionObjectId(inspectionObject.getId());

				// Store all assignments into the database
				datasource.createAssignment(assignment);
				datasource.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
