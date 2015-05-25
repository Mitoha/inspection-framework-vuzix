package com.teamproject.inspectionframework.Application_Layer;

import java.util.List;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides functions for creating JSON files out of local objects for server
 * synchronization
 *
 */
public class ParseJSON {

	/**
	 * Parse user to JSON Object
	 * 
	 * @param user
	 *            The user object to be parsed
	 * @return The JSON object for the parsed user
	 */
	public JSONObject userToJson(User user) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", user.getUserId());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	/**
	 * Parse inspection object to JSON Object
	 * 
	 * @param inspectionObject
	 *            The inspection object to be parsed
	 * @return The JSON object for the parsed inspection object
	 */
	public JSONObject inspectionObjectToJson(InspectionObject inspectionObject) {
		JSONObject jsonObjectInspectionObject = new JSONObject();
		try {
			jsonObjectInspectionObject.put("id", inspectionObject.getId());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObjectInspectionObject;
	}

	/**
	 * Parse Task List to JSON Array
	 * 
	 * @param taskList
	 *            The task list to be parsed
	 * @return The JSON array with the parsed task list
	 */
	public JSONArray taskToJson(List<Task> taskList) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);

			try {
				jsonObject = new JSONObject();
				jsonObject.put("id", task.getId());
				jsonObject.put("taskName", task.getTaskName());
				jsonObject.put("description", task.getDescription());
				jsonObject.put("errorDescription", task.getErrorDescription());
				jsonObject.put("assignmentVersion", null);
				jsonObject.put("state", task.getState());

				jsonArray.put(jsonObject);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	/**
	 * Parse Assignment to JSON Object
	 * 
	 * @param assignment
	 *            The assignment object to be parsed
	 * @param listWithallAssignedTasks
	 *            The list of tasks related to the assignment
	 * @param assignedUser
	 *            The user to whom the assignment is assigned to
	 * @param assignedInspectionObject
	 *            The inspection object related to the assignment
	 * @return The JSON Object for the parsed assignment as String
	 */
	public String completeAssignmentToJson(Assignment assignment, List<Task> listWithallAssignedTasks, User assignedUser, InspectionObject assignedInspectionObject) {
		JSONObject jsonObjectAssignment = new JSONObject();
		JSONArray jsonArrayTasks = new JSONArray();
		jsonArrayTasks = taskToJson(listWithallAssignedTasks);
		JSONObject jsonObjectInspectionObject = inspectionObjectToJson(assignedInspectionObject);
		JSONObject jsonObjectUser = userToJson(assignedUser);

		try {

			jsonObjectAssignment.put("id", assignment.getId());
			jsonObjectAssignment.put("assignmentName", assignment.getAssignmentName());
			jsonObjectAssignment.put("description", assignment.getDescription());
			jsonObjectAssignment.put("isTemplate", false);
			jsonObjectAssignment.put("state", assignment.getState());
			jsonObjectAssignment.put("tasks", jsonArrayTasks);
			jsonObjectAssignment.put("startDate", assignment.getStartDate());
			jsonObjectAssignment.put("endDate", assignment.getDueDate());
			jsonObjectAssignment.put("attachmentIds", null);
			jsonObjectAssignment.put("user", jsonObjectUser);
			jsonObjectAssignment.put("inspectionObject", jsonObjectInspectionObject);
			jsonObjectAssignment.put("version", assignment.getVersion());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObjectAssignment.toString();
	}
}
