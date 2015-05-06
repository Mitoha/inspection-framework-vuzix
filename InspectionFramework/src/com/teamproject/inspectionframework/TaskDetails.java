package com.teamproject.inspectionframework;

import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterLoginScreen;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterTaskDetails;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetails extends FragmentActivity implements ActionBar.TabListener {

	private MySQLiteHelper datasource;
	private ViewPager viewPager;
	private TabAdapterTaskDetails mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Details", "Attachments" };
	private MyApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_details);

		myApp = (MyApplication) getApplicationContext();

		// Adjust Action Bar title
		actionBar = getActionBar();
		String taskStateWording = "initial";
		switch (myApp.getTask().getState()) {
		case 0:
			taskStateWording = "open";
			break;
		case 1:
			taskStateWording = "OK";
			break;
		case 2:
			taskStateWording = "Error";
			break;
		}
		actionBar.setTitle(getString(R.string.title_activity_task_details) + ": " + myApp.getTask().getTaskName() + " [" + taskStateWording + "]");

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.loginScreenPager);
		mAdapter = new TabAdapterTaskDetails(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		// Sets the tab when view is changed by swiping left/right
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int menuItemId = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Not needed
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Not needed
	}

	/**
	 * Handles the click on the buttons setting the task state
	 * 
	 * @author Michael Hartl
	 * @param view
	 */
	public void onClick(View view) {
		String clickedButtonTag = (String) view.getTag();

		if (clickedButtonTag.equals("buttonOk")) {
			myApp.getTask().setState(1);
		}
		if (clickedButtonTag.equals("buttonError")) {
			myApp.getTask().setState(2);
		}
		
		datasource = new MySQLiteHelper(getApplicationContext());
		datasource.updateTask(myApp.getTask());

		// Update assignment state to 'in progress' if not current state
		if(myApp.getAssignment().getState() != 1) {
			myApp.getAssignment().setState(1);
			datasource.updateAssignment(myApp.getAssignment());
		}

		datasource.close();

		// Go back to task list
		Intent goToTaskList = new Intent(this, TaskList.class);
		startActivity(goToTaskList);
	}
	
	
	
	
	
}
