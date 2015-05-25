package com.teamproject.inspectionframework;

import java.io.IOException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.teamproject.inspectionframework.List_Adapters.TabAdapterTaskDetails;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;
import com.teamproject.inspectionframework.vuzixHelpers.AttachmentHandler;
import com.teamproject.inspectionframework.vuzixHelpers.VuzixVoiceControl;
import com.vuzix.speech.Constants;
import com.vuzix.speech.VoiceControl;

/**
 * Creates the screen displaying the details of a task (state setter and
 * attachment adding screens)
 *
 */
public class TaskDetails extends FragmentActivity implements ActionBar.TabListener {

	private MySQLiteHelper datasource;
	private ViewPager viewPager;
	private TabAdapterTaskDetails mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Details", "Attachments" };
	private MyApplication myApp;
	private VoiceControl vc;
	private AttachmentHandler attHandler;
	private Button soundRecordingButton;

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_details);

		myApp = (MyApplication) getApplicationContext();
		attHandler = new AttachmentHandler(getApplicationContext(), this);

		// START VC ACTIVITY
		vc = new VuzixVoiceControl(getApplicationContext());
		vc.addGrammar(Constants.GRAMMAR_BASIC);

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
		viewPager = (ViewPager) findViewById(R.id.task_details_pager);
		mAdapter = new TabAdapterTaskDetails(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		// Sets the tab when view is changed by swiping left/right
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}
		});

		datasource = new MySQLiteHelper(getApplicationContext());
	}

	// Handles the processing of an intent to take a picture
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean result = false;

		try {
			result = attHandler.takePictureResult(requestCode, resultCode, data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (result == true) {
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
		@SuppressWarnings("unused")
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
		if (vc != null)
			vc.destroy();
		startActivity(goToTaskList);
	}

	public void onClickTakePicture(View view) {
		attHandler.takePicture();
	}

	/**
	 * Handles clicks on the voice recording button
	 * 
	 * @param view
	 * @throws IOException
	 */
	public void onClickRecordingButton(View view) throws IOException {

		soundRecordingButton = (Button) findViewById(R.id.task_att_audioRecordingButton);
		Chronometer chrono = (Chronometer) findViewById(R.id.task_att_chronometer);
		Boolean startTrigger = false;

		if (soundRecordingButton.getTag().equals("START")) {
			if (vc != null)
				vc.off();
			vc.destroy();
			boolean result = attHandler.startAudioRecording();
			if (result == true) {
				// Changes button layout
				soundRecordingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_stop, 0, 0, 0);
				soundRecordingButton.setText("Stop Audio Recording");
				soundRecordingButton.setTag("STOP");
				startTrigger = true;

				// Triggers Chronometer
				chrono.setBase(SystemClock.elapsedRealtime());
				chrono.setVisibility(1);
				chrono.start();
				Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
			}
		}

		if (soundRecordingButton.getTag().equals("STOP") && startTrigger == false) {
			boolean result = attHandler.stopAudioRecording();

			// START VC ACTIVITY
			vc = new VuzixVoiceControl(getApplicationContext());
			vc.addGrammar(Constants.GRAMMAR_BASIC);
			vc.on();

			// Changes button layout
			soundRecordingButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_play, 0, 0, 0);
			soundRecordingButton.setText("Start Audio Recording");
			soundRecordingButton.setTag("START");

			// Triggers Chronometer
			chrono.stop();
			Toast.makeText(this, "Recording stopped & saved!", Toast.LENGTH_SHORT).show();

			if (result == true) {

			}
		}
	}
}
