package com.teamproject.inspectionframework;

import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.teamproject.inspectionframework.Application_Layer.SynchronizationHelper;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.List_Adapters.AssignmentListAdapter;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

/**
 * Creates the screen displaying the list of assignments per user
 * 
 *
 */
public class AssignmentList extends ListActivity {

	// VAR-declaration
	private MySQLiteHelper datasource;
	private AssignmentListAdapter adapter;
	private SynchronizationHelper syncHelper;
	private MyApplication myApp;

	@Override
	protected void onResume() {
		super.onResume();
		myApp.vc.on();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myApp.vc.on();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_list);
		myApp = (MyApplication) getApplicationContext();

		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.title_activity_assignment_list) + " for " + myApp.getUser().getUserName());

		this.createOutputList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the menu launcher
		getMenuInflater().inflate(R.menu.assignment_list, menu);
		return true;
	}

	/**
	 * Creates the list of assignments
	 */
	public void createOutputList() {
		datasource = new MySQLiteHelper(getApplicationContext());
		List<Assignment> listWithAllAssignmentsByUser = datasource.getAssignmentsByUserId(myApp.getUser().getUserId());

		adapter = new AssignmentListAdapter(this, listWithAllAssignmentsByUser);
		setListAdapter(adapter);
		datasource.close();
	}

	// Listens to clicks on list entries and calls the adapter for retrieving
	// the corresponding assignment object
	protected void onListItemClick(ListView l, android.view.View v, int position, long id) {

		Assignment clickedAssignment = adapter.getClickedAssignment(position);

		Intent goToTaskListIntent = new Intent(this, TaskList.class);
		myApp.setAssignment(clickedAssignment);
		startActivity(goToTaskListIntent);
	};

	/**
	 * Updates the list of users when pressing BACK
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent goToMainActivityIntent = new Intent(this, MainActivity.class);
			startActivity(goToMainActivityIntent);
		}

		return super.onKeyDown(keyCode, event);
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

			syncHelper = new SynchronizationHelper();
			syncHelper.SynchronizeAssignments(getApplicationContext(), myApp.getUser().getUserId(), AssignmentList.this);

			// Creates the output list after retrieving updates from the server
			this.createOutputList();
			break;

		case R.id.action_logout_user:

			// Logout: Delete the user entry in the local database and return to
			// login screen
			datasource = new MySQLiteHelper(getApplicationContext());
			datasource.deleteUser(myApp.getUser().getUserId());
			Intent goToUserListIntent = new Intent(this, MainActivity.class);
			startActivity(goToUserListIntent);

		default:
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
