package com.teamproject.inspectionframework;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.teamproject.inspectionframework.Application_Layer.SynchronizationHelper;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.List_Adapters.AssignmentAdapter;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

public class AssignmentList extends ListActivity {

	// VAR-declaration
	private MySQLiteHelper datasource;
	private AssignmentAdapter adapter;
	private SynchronizationHelper syncHelper;

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
		List<Assignment> listWithAllAssignmentsByUser = datasource.getAssignmentsByUserId(getIntent().getExtras().getString("userId"));

		adapter = new AssignmentAdapter(this, listWithAllAssignmentsByUser);
		setListAdapter(adapter);

		datasource.close();
	}

	// Listens to clicks on list entries and calls the adapter for retrieving
	// the corresponding assignment object
	protected void onListItemClick(ListView l, android.view.View v, int position, long id) {

		Assignment clickedAssignment = adapter.getClickedAssignment(position);

		Intent goToTaskListIntent = new Intent(this, TaskList.class);
		goToTaskListIntent.putExtra("AssignmentName", clickedAssignment.getAssignmentName());
		goToTaskListIntent.putExtra("AssignmentId", clickedAssignment.getId());
		startActivity(goToTaskListIntent);
	};

	/**
	 * This makes sure that when pressing the BACK-Button, the User List is
	 * up-to-date
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
			
			ProgressDialog progress = new ProgressDialog(this);
			progress.setTitle("Synchronization");
			progress.setMessage("Please wait...");
			progress.show();
			
			syncHelper = new SynchronizationHelper();
			syncHelper.SynchronizeAssignments(getApplicationContext(), getIntent().getExtras().getString("userId"));
			
			progress.dismiss();
			
			// Creates the output list after retrieving updates from the server
			this.createOutputList();
			break;

		case R.id.action_logout_user:

			// Logout: Delete the user entry in the local database and return to
			// login screen
			datasource = new MySQLiteHelper(getApplicationContext());
			datasource.deleteUser(getIntent().getExtras().getString("userId"));
			Log.i("IF", getIntent().getExtras().getString("userId"));

			Intent goToUserListIntent = new Intent(this, MainActivity.class);
			startActivity(goToUserListIntent);

		default:
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
