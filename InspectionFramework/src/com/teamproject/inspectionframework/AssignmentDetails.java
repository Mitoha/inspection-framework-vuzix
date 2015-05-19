package com.teamproject.inspectionframework;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;
import com.teamproject.inspectionframework.vuzixHelpers.VuzixVoiceControl;
import com.vuzix.speech.Constants;
import com.vuzix.speech.VoiceControl;

public class AssignmentDetails extends Activity {

	// VAR-Declaration
	private MySQLiteHelper datasource;
	private MyApplication myApp;
	private VoiceControl vc;

	@Override
	protected void onResume() {
		super.onResume();
		vc.on();
	}

	@Override
	protected void onPause() {
		super.onPause();
		vc.off();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_details);
		myApp = (MyApplication) getApplicationContext();

		// Adjust Action Bar title
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.title_activity_assignment_details) + ": " + myApp.getAssignment().getAssignmentName());

		// START VC ACTIVITY
		vc = new VuzixVoiceControl(getApplicationContext());
		vc.addGrammar(Constants.GRAMMAR_BASIC);

		// Create output
		datasource = new MySQLiteHelper(getApplicationContext());

		Assignment assignment = myApp.getAssignment();
		TextView buildElement = new TextView(this);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",Locale.GERMANY);
		Date startDate = new Date(assignment.getStartDate());
		Date dueDate = new Date(assignment.getDueDate());

		buildElement = (TextView) findViewById(R.id.tvAssignmentName);
		buildElement.setText(assignment.getAssignmentName());
		buildElement = (TextView) findViewById(R.id.tvAssignmentStartDate);
		buildElement.setText(dateFormat.format(startDate));
		buildElement = (TextView) findViewById(R.id.tvAssignmentDueDate);
		buildElement.setText(dateFormat.format(dueDate));

		buildElement = (TextView) findViewById(R.id.tvAssignmentInspObj);
		if (assignment.getInspectionObjectId() != null) {
			buildElement.setText(datasource.getInspectionObjectById(assignment.getInspectionObjectId()).getObjectName());
		} else {
			buildElement.setText("Not found");
		}

		buildElement = (TextView) findViewById(R.id.tvAssignmentDescription);
		if (assignment.getDescription() != null) {
			buildElement.setText(assignment.getDescription());
		} else {
			buildElement.setText("Not found");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignment_details, menu);
		return true;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int menuItem = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
}
