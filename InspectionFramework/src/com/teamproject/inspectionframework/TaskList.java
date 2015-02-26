package com.teamproject.inspectionframework;

import java.util.List;

import com.teamproject.inspectionframework.Application_Layer.RESTServices;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.List_Adapters.AssignmentAdapter;
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

public class TaskList extends ListActivity {

	// VAR-declaration
	private MySQLiteHelper datasource;
	private RESTServices restInstance;
	private TaskListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);

		// Adjust Action Bar title
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.title_activity_task_list) + ": " + getIntent().getExtras().getString("AssignmentName"));

		this.createOutputList();
	}

	public void createOutputList() {
		datasource = new MySQLiteHelper(getApplicationContext());
		List<Task> listWithAllTasksByAssignment = datasource.getTasksByAssignmentId(getIntent().getExtras().getString("AssignmentId"));
		Log.i("IF","ASS_ID " + getIntent().getExtras().getString("AssignmentId"));

		adapter = new TaskListAdapter(this, listWithAllTasksByAssignment);
		setListAdapter(adapter);

		datasource.close();
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
			//TODO: fill
			break;
			
		case R.id.action_attachment_finish_assignment:
			//TODO: fill
			break;
			
		default:
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
