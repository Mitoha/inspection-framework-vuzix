package com.teamproject.inspectionframework;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamproject.inspectionframework.Application_Layer.RESTServices;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.InspectionObject;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Entities.User;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AssignmentList extends Activity {

	// VAR-declaration
	ListView listViewAssignmentList;
	private MySQLiteHelper datasource;
	private List<String> listOutput;
	private RESTServices restInstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_list);

		this.createOutputList();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the menu launcher
		getMenuInflater().inflate(R.menu.assignment_list, menu);
		return true;
	}

	public void createOutputList() {
		datasource = new MySQLiteHelper(getApplicationContext());
		listViewAssignmentList = (ListView) findViewById(R.id.lvAssignmentList);

		// load all assignments from the database to the list listWithAllStoredAssigmments
		List<Assignment> listWithAllStoredAssignments = datasource.getAllAssignments();

		listOutput = new ArrayList<String>();

		// TODO: CHANGE OUTPUT FOR CUSTOM ADAPTER AND JAVA OBJECT (assignment)
		// OUTPUT VIA ADAPTER
		for (int i = 0; i < listWithAllStoredAssignments.size(); i++) {
			Assignment assignment = listWithAllStoredAssignments.get(i);
			String assignmentName = assignment.getAssignmentName();
			listOutput.add(assignmentName);
		}

		ListAdapter listenAdapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, listOutput);
		listViewAssignmentList.setAdapter(listenAdapter);
		
		
		datasource.close();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int menuItemId = item.getItemId();
		switch (menuItemId) {

		// Sync assignments between Heroku server and local database
		case R.id.action_synchronize_assignments:
			Log.i("IF", "Assignment Sync activated");

			restInstance = new RESTServices();

			String inputAssignment = restInstance
					.readHerokuServer("assignment");

			try {
				JSONArray jArray = new JSONArray(inputAssignment);

				for (int i = 0; i < jArray.length(); i++) {
					Assignment ass = new Assignment();
					JSONObject jObject = jArray.getJSONObject(i);

					// get and set the values for the table assignments
					ass.setDescription(jObject.get("description").toString());
					ass.setAssignmentName(jObject.get("assignmentName")
							.toString());
					ass.setId(jObject.get("id").toString());
					ass.setStartDate(jObject.getInt("startDate"));
					ass.setDueDate(jObject.getInt("endDate"));
					ass.setInspectionObjectId(jObject.get("isTemplate")
							.toString());

					String assignmentName = ass.getAssignmentName();
					String description = ass.getDescription();
					String id = ass.getId();
					Integer startDate = ass.getStartDate();
					Integer endDate = ass.getDueDate();
					String isTemplate = ass.getIsTemplate();

					// Download all tasks assigned to an assignment from the
					// server
					// jArrayTask gets the SubJSONObject "tasks"
					JSONArray jArrayTask = new JSONArray(jObject.get("tasks")
							.toString());

					for (int j = 0; j < jArrayTask.length(); j++) {
						Task task = new Task();
						JSONObject jObjectTask = jArrayTask.getJSONObject(j);
						task.setId(jObjectTask.get("id").toString());
						task.setDescription(jObjectTask.get("description")
								.toString());
						//task.setState(jObjectTask.getInt("state"));
						task.setTaskName(jObjectTask.get("taskName").toString());

						String taskName = task.getTaskName();
						String taskId = task.getId();
						String taskDescription = task.getDescription();
						Integer taskState = task.getState();


						// Store all assigned tasks into the database
						datasource.createTask(taskId, taskName,
								taskDescription, taskState, id);
					}

					JSONObject jObjectInspectionObject = new JSONObject(jObject
							.get("inspectionObject").toString());
					InspectionObject inspectionObject = new InspectionObject();
					inspectionObject.setId(jObjectInspectionObject.get("id")
							.toString());
					String objectId;
					objectId = inspectionObject.getId();

					// TODO: Error occurs when userId = null -> Assignments will
					// not be loaded
					// JSONObject jObjectUser = new
					// JSONObject(jObject.get("user")
					// .toString());
					// User user = new User();
					// user.setUserId(jObjectUser.get("id").toString());
					// String userId = user.getUserId();

					// Store all assignments into the database
					datasource.createAssignment(id, assignmentName,
							description, startDate, endDate, objectId,
							"TestUser", isTemplate);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// Creates the output list after retrieving updates from the server
			this.createOutputList();
			break;

		// Open Assignment Details for selected assignment
		case R.id.action_show_assignment_details:
			// TODO: Code here
			Intent goToAssignmentDetailsIntent = new Intent(this,AssignmentDetails.class);
			//goToAssignmentDetailsIntent.putExtra("AssignmentObject",assignment);
	    	startActivity(goToAssignmentDetailsIntent);
			break;

		// Show the screen for assignment finishing and attachment adding
		case R.id.action_attachment_finish_assignment:
			// TODO: Code here
			Toast finish = Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT);
	    	finish.show();
			break;
		default:
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
