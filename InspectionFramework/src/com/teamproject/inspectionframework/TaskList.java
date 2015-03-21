package com.teamproject.inspectionframework;

import java.util.List;

import com.teamproject.inspectionframework.Application_Layer.HttpCustomClient;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.List_Adapters.TaskListAdapter;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class TaskList extends ListActivity {

	// VAR-declaration
	private MySQLiteHelper datasource;
	private HttpCustomClient restInstance;
	private TaskListAdapter adapter;

	private String assignmentId;
	private String assignmentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);

		// Set Variables
		this.assignmentId = getIntent().getExtras().getString("AssignmentId");
		this.assignmentName = getIntent().getExtras().getString("AssignmentName");

		// Adjust Action Bar title
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.title_activity_task_list) + ": " + assignmentName);

		this.createOutputList();
	}

	public void createOutputList() {
		datasource = new MySQLiteHelper(getApplicationContext());
		List<Task> listWithAllTasksByAssignment = datasource.getTasksByAssignmentId(assignmentId);

		adapter = new TaskListAdapter(this, listWithAllTasksByAssignment);
		setListAdapter(adapter);

		datasource.close();
	}

	// Listens to clicks on list entries and calls the adapter for retrieving
	// the corresponding task object
	protected void onListItemClick(ListView l, android.view.View v, int position, long id) {

		Task clickedTask = adapter.getClickedTask(position);

		Intent gotToTaskDetailsIntent = new Intent(this, TaskDetails.class);
		gotToTaskDetailsIntent.putExtra("taskId", clickedTask.getId());
		startActivity(gotToTaskDetailsIntent);
	};

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
			goToAssignmentDetailsIntent.putExtra("AssignmentName", assignmentName);
			goToAssignmentDetailsIntent.putExtra("AssignmentId", assignmentId);
			startActivity(goToAssignmentDetailsIntent);

			break;

		case R.id.action_show_task_attachment:

			Intent goToTaskAttachmentIntent = new Intent(this, TaskAttachment.class);
			//goToTaskAttachmentIntent.putExtra("taskName", task);
			startActivity(goToTaskAttachmentIntent);
			//Toast toast = Toast.makeText(this, "Function not implemented yet!", Toast.LENGTH_SHORT);
			//toast.show();

			break;

		case R.id.action_attachment_finish_assignment:	
			
			Toast toast = Toast.makeText(this, "Function not implemented yet!", Toast.LENGTH_SHORT);
			toast.show();
			
			break;
			
		default:
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
