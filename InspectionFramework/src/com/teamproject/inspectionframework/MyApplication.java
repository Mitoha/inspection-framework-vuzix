package com.teamproject.inspectionframework;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.Application;
import android.content.res.Configuration;

/**
 * This class stores application-wide used values
 * 
 */
public class MyApplication extends Application {

	private User user;
	private Assignment assignment;
	private Task task;

	private MySQLiteHelper datasource;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	// Getter and Setter

	public User getUser() {
		return user;
	}

	public void setUser(String userId) {
		datasource = new MySQLiteHelper(getApplicationContext());
		User user = datasource.getUserByUserId(userId);
		this.user = user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(String assignmentId) {
		datasource = new MySQLiteHelper(getApplicationContext());
		Assignment assignment = datasource.getAssignmentById(assignmentId);
		this.assignment = assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(String taskId) {
		datasource = new MySQLiteHelper(getApplicationContext());
		Task task = datasource.getTaskById(taskId);
		this.task = task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
