package com.teamproject.inspectionframework.Application_Layer;

import android.util.Log;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ParseJSON {
	
	public void Tester() {
		Log.i("IF","Region Reached");
	}
	
    //Method: Parse User to JSON
    public String userToJson(User user){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", user.getUserId());
            jsonObject.put("userName", user.getUserName());
            jsonObject.put("emailAddress", user.getEmail());
            jsonObject.put("role", user.getRole());
            jsonObject.put("firstName", user.getFirstName());
            jsonObject.put("lastName", user.getLastName());
            jsonObject.put("phoneNumber", user.getPhoneNumber());
            jsonObject.put("mobileNumber", user.getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String taskToJson (Task task){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", task.getId());
            jsonObject.put("taskName", task.getTaskName());
            jsonObject.put("description", task.getDescription());
            jsonObject.put("state", task.getState());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String assignmentToJson(Assignment assignment){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", assignment.getId());
            jsonObject.put("assignmentName", assignment.getAssignmentName());
            jsonObject.put("description", assignment.getDescription());
            jsonObject.put("isTemplate", assignment.getIsTemplate());
            jsonObject.put("tasks", null);
            jsonObject.put("startDate", assignment.getStartDate());
            jsonObject.put("endDate", assignment.getDueDate());
            jsonObject.put("attachmentIds", null);
            jsonObject.put("user",null);
            jsonObject.put("inspectionObject", null);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
