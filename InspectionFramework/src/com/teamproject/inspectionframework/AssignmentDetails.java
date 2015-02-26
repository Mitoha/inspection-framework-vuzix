package com.teamproject.inspectionframework;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentDetails extends Activity {

	// VAR-Declaration
	private String assignmentId;
	private String assignmentName;
	private MySQLiteHelper datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_details);

		// Set Variables
		this.assignmentId = getIntent().getExtras().getString("AssignmentId");
		this.assignmentName = getIntent().getExtras().getString("AssignmentName");

		// Adjust Action Bar title
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.title_activity_assignment_details) + ": " + assignmentName);
		
		//Create output
		datasource = new MySQLiteHelper(getApplicationContext());
		Assignment assignment = datasource.getAssignmentById(assignmentId);
		TextView buildElement = new TextView(this);
		
		buildElement = (TextView) findViewById(R.id.tvAssignmentName);
		buildElement.setText("Name: " + assignmentName);
		buildElement = (TextView) findViewById(R.id.tvAssignmentStartDate);
		buildElement.setText("Start Date: " + assignment.getStartDate());
		buildElement = (TextView) findViewById(R.id.tvAssignmentDueDate);
		buildElement.setText("Due Date: " + assignment.getDueDate());
		buildElement = (TextView) findViewById(R.id.tvAssignmentInspObj);
		buildElement.setText("Insp. Object: " + assignment.getInspectionObjectId());
		buildElement = (TextView) findViewById(R.id.tvAssignmentDescription);
		buildElement.setText("Description: " + assignment.getDescription());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignment_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
