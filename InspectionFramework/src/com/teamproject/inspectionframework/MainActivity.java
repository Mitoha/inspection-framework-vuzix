package com.teamproject.inspectionframework;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.teamproject.inspectionframework.Application_Layer.SynchronizationHelper;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterLoginScreen;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;
import com.teamproject.inspectionframework.vuzixHelpers.VuzixVoiceControl;
import com.vuzix.speech.Constants;
import com.vuzix.speech.VoiceControl;

/**
 * Creates the screen displaying the user login and user list
 *
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private TabAdapterLoginScreen mAdapter;
	private ActionBar actionBar;
	private VoiceControl vc;
	private String[] tabs = { "Login", "User list" };
	private MyApplication myApp;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myApp = (MyApplication) getApplicationContext();

		// Set title for Action Bar
		actionBar = getActionBar();
		actionBar.setTitle(getString(R.string.app_name) + ": User Login");

		// START VC ACTIVITY
		vc = new VuzixVoiceControl(getApplicationContext());
		vc.addGrammar(Constants.GRAMMAR_BASIC);

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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent goToMainActivity = new Intent(this, MainActivity.class);
			if (vc != null)
				vc.destroy();
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

		switch (menuItemId) {
		case R.id.action_clean_database:
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
			alert.setTitle("Database Cleaning");
			alert.setMessage("This action will delete all database entries! Continue?");
			alert.setPositiveButton("Clean Database", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Clean database
					MySQLiteHelper datasource = new MySQLiteHelper(getApplicationContext());
					datasource.cleanDatabase();
					datasource.close();
					Intent reloadList = new Intent(getApplicationContext(), MainActivity.class);
					if (vc != null)
						vc.destroy();
					startActivity(reloadList);
					Toast.makeText(getApplicationContext(), "Database cleaned", Toast.LENGTH_LONG).show();

				}
			});
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// ...
				}
			});
			alert.show();
			break;
		}

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
	 * Handles clicks on the login button
	 * 
	 * @param v
	 */
	public void onClickLoginButton(View v) {
		EditText editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		EditText editPassword = (EditText) findViewById(R.id.editTextUserPassword);

		SynchronizationHelper syncHelper = new SynchronizationHelper();
		String userId = syncHelper.UserLogin(getApplicationContext(), editTextUserName.getText().toString(), editPassword.getText().toString());

		if (userId != "0") {

			Intent goToAssignmentIntent = new Intent(getApplicationContext(), AssignmentList.class);
			myApp.setUser(userId);
			if (vc != null)
				vc.destroy();
			startActivity(goToAssignmentIntent);
		}
	}

}