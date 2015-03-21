package com.teamproject.inspectionframework;

import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterLoginScreen;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterTaskDetails;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TaskDetails extends FragmentActivity implements ActionBar.TabListener {

	private String taskId;
	private Task task;
	private MySQLiteHelper datasource;

	private ViewPager viewPager;
	private TabAdapterTaskDetails mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Details", "Attachments" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_details);

		// Set variables
		datasource = new MySQLiteHelper(getApplicationContext());
		this.taskId = getIntent().getExtras().getString("taskId");
		this.task = datasource.getTaskById(taskId);

		// Adjust Action Bar title
		actionBar = getActionBar();
		String taskStateWording = "initial";
		switch (task.getState()) {
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
		actionBar.setTitle(getString(R.string.title_activity_task_details) + ": " + task.getTaskName() + " [" + taskStateWording + "]");

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
		// TODO: Deactivate swiping possibility
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
}
