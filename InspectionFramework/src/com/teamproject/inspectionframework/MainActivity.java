package com.teamproject.inspectionframework;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.teamproject.inspectionframework.Application_Layer.SynchronizationHelper;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterLoginScreen;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private TabAdapterLoginScreen mAdapter;
	private ActionBar actionBar;
	//private VoiceControl vc;
	private String[] tabs = { "Login", "User list" };
	private MyApplication myApp;

//	 protected void onResume() {
//	 super.onResume();
//	 vc.on();
//	 }

	// TODO: Clarify if ok when off -> Voice recognition stays on when switching
	// the activity
	/*
	 * @Override protected void onPause() { super.onPause(); vc.off(); }
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myApp = (MyApplication) getApplicationContext();

		// Set title for Action Bar
		actionBar = getActionBar();
		actionBar.setTitle("User Login");

		 //START VC ACTIVITY
//		 vc = new VuzixVoiceControl(getApplicationContext());
//		 vc.addGrammar(Constants.GRAMMAR_BASIC);

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.loginScreenPager);
		mAdapter = new TabAdapterLoginScreen(getSupportFragmentManager());

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

	/**
	 * This makes sure that when pressing the BACK-Button the User can't go back
	 * to assignment list
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent goToMainActivity = new Intent(this, MainActivity.class);
			startActivity(goToMainActivity);
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	// method that should call the voice recording screen
	// public void voiceRecordingScreen(View view) {
	//
	// TODO: Clarify
	// // Intent to audio recording
	// Intent goToVoiceRecordingIntent = new Intent(this,
	// SoundRecordingActivity.class);
	// startActivity(goToVoiceRecordingIntent);
	// }

	public void onClickLoginButton(View v) {
		EditText editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		EditText editPassword = (EditText) findViewById(R.id.editTextUserPassword);

		SynchronizationHelper syncHelper = new SynchronizationHelper();
		String userId = syncHelper.UserLogin(getApplicationContext(), editTextUserName.getText().toString(), editPassword.getText().toString());

		if (userId != "0") {

			Intent goToAssignmentIntent = new Intent(getApplicationContext(), AssignmentList.class);
			myApp.setUser(userId);
			startActivity(goToAssignmentIntent);
		}
	}

}
