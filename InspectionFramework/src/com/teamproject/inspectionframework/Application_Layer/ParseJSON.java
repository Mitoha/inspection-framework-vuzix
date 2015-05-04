package com.teamproject.inspectionframework.Application_Layer;

import java.util.List;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON {

    // Method: Parse User to JSON String
    public JSONObject userToJson(User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getUserId());
            /*jsonObject.put("userName", user.getUserName());
            jsonObject.put("emailAddress", user.getEmail());
            jsonObject.put("role", user.getRole());
            jsonObject.put("firstName", user.getFirstName());
            jsonObject.put("lastName", user.getLastName());
            jsonObject.put("phoneNumber", user.getPhoneNumber());
            jsonObject.put("mobileNumber", user.getMobileNumber());*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    // Method: Parse Inspection to JSONObject
    public JSONObject inspectionObjectToJson(InspectionObject inspectionObject) {
        JSONObject jsonObjectInspectionObject = new JSONObject();
        try {
            jsonObjectInspectionObject.put("id", inspectionObject.getId());
            /*jsonObjectInspectionObject.put("objectName", inspectionObject.getObjectName());
            jsonObjectInspectionObject.put("description", inspectionObject.getDescription());
            jsonObjectInspectionObject.put("location", inspectionObject.getLocation());
            jsonObjectInspectionObject.put("customerName", inspectionObject.getCustomerName());
            jsonObjectInspectionObject.put("attachmentIds", null);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectInspectionObject;
    }

    // Method: Parse Task to JSON String
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

    // Method: Parse Assignment to JSON String
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
