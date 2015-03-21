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
import android.widget.Toast;

import com.teamproject.inspectionframework.Application_Layer.HttpCustomClient;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.List_Adapters.TaskListAdapter;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

public class TaskList extends ListActivity {

	// VAR-declaration
	private MySQLiteHelper datasource;
	private HttpCustomClient restInstance;
	private TaskListAdapter adapter;
	
	private MyApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		myApp = (MyApplication) getApplicationContext();

		// Adjust Action Bar title
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.title_activity_task_list) + ": " + myApp.getAssignment().getAssignmentName());

		this.createOutputList();
	}

	public void createOutputList() {
		datasource = new MySQLiteHelper(getApplicationContext());
		List<Task> listWithAllTasksByAssignment = datasource.getTasksByAssignmentId(myApp.getAssignment().getId());

		adapter = new TaskListAdapter(this, listWithAllTasksByAssignment);
		setListAdapter(adapter);

		datasource.close();
	}

	// Listens to clicks on list entries and calls the adapter for retrieving
	// the corresponding task object
	protected void onListItemClick(ListView l, android.view.View v, int position, long id) {

		Task clickedTask = adapter.getClickedTask(position);

		Intent gotToTaskDetailsIntent = new Intent(this, TaskDetails.class);
		myApp.setTask(clickedTask);
		startActivity(gotToTaskDetailsIntent);
	};
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent goToAssignmentList = new Intent(this, AssignmentList.class);
			startActivity(goToAssignmentList);
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int menuItemId = item.getItemId();
		switch (menuItemId) {
		case R.id.action_show_assignment_details:

			Intent goToAssignmentDetailsIntent = new Intent(this, AssignmentDetails.class);
			startActivity(goToAssignmentDetailsIntent);

			break;

		case R.id.action_show_task_attachment:

			//TODO: IMPLEMENT METHOD AND CHANGE TO ASSIGNMENT ATTACHMENT!
			Intent goToTaskAttachmentIntent = new Intent(this, TaskAttachment.class);
			startActivity(goToTaskAttachmentIntent);
			break;

		case R.id.action_attachment_finish_assignment:	
			
			//TODO: Implement method!
			Toast toast = Toast.makeText(this, "Function not implemented yet!", Toast.LENGTH_SHORT);
			toast.show();
			
			break;
			
		default:
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
