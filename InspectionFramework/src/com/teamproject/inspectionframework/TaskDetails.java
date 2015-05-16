package com.teamproject.inspectionframework;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamproject.inspectionframework.Application_Layer.BitmapUtility;
import com.teamproject.inspectionframework.Application_Layer.HttpCustomClient;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterTaskDetails;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;
import com.teamproject.inspectionframework.vuzixHelpers.AttachmentHandler;
import com.teamproject.inspectionframework.vuzixHelpers.PhotoHandler;
import com.vuzix.hardware.VuzixCamera;

public class TaskDetails extends FragmentActivity implements ActionBar.TabListener {

	private MySQLiteHelper datasource;
	private ViewPager viewPager;
	private TabAdapterTaskDetails mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Details", "Attachments" };
	private MyApplication myApp;
	private VuzixCamera camera;
	private boolean picAvailable = false;
	private AttachmentHandler attHandler;

	// TODO: Check what needed here (liste unten)

	// audiorecording vars;
	MediaRecorder recorder;
	File audiofile = null;
	private static final String TAG = "SoundRecordingActivity";
	private View startButton;
	private View stopButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_details);

		myApp = (MyApplication) getApplicationContext();
		attHandler = new AttachmentHandler(getApplicationContext(), this);

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

		datasource = new MySQLiteHelper(getApplicationContext());
		Button picture = (Button) findViewById(R.id.task_att_btn1);
		Assignment assignment = myApp.getAssignment();
		Task task = myApp.getTask();

	}

	// Handles the picture processing after a photo was taken
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean result = false;
		
		try {
			result = attHandler.takePictureResult(requestCode, resultCode, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(result == true) {
			Toast.makeText(this, "Picture successfully saved!", Toast.LENGTH_LONG).show();
		}
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
	public void onClickStateSetter(View view) {
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
		if (myApp.getAssignment().getState() != 1) {
			myApp.getAssignment().setState(1);
			datasource.updateAssignment(myApp.getAssignment());
		}

		datasource.close();

		// Go back to task list
		Intent goToTaskList = new Intent(this, TaskList.class);
		startActivity(goToTaskList);
	}

	public void onClickTakePicture(View view) {
		attHandler.takePicture("task");
	}
}
