package com.teamproject.inspectionframework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.teamproject.inspectionframework.Application_Layer.BitmapUtility;
import com.teamproject.inspectionframework.Application_Layer.HttpCustomClient;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Attachment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterLoginScreen;
import com.teamproject.inspectionframework.List_Adapters.TabAdapterTaskDetails;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;

import java.text.SimpleDateFormat;

public class TaskDetails extends FragmentActivity implements ActionBar.TabListener {

	private MySQLiteHelper datasource;
	private ViewPager viewPager;
	private TabAdapterTaskDetails mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Details", "Attachments" };
	private MyApplication myApp;
	private static final int REQUEST_TAKE_PHOTO = 1;
	String mCurrentPhotoPath;
//	int Clicked = 0;
//	int NoPic=0;
//	Task task;
//    BitmapUtility bitmapUtility;
//    HttpCustomClient client;
//    Button Picture;
//    ImageView IMG;
	//audiorecording vars
    MediaRecorder recorder;
    File audiofile = null;
    private static final String TAG = "SoundRecordingActivity";
    private View startButton;
    private View stopButton;
    
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
		
		
		// audiorecording part
		startButton = findViewById(R.id.task_att_btn2);
		stopButton = findViewById(R.id.task_att_btn3);
	    
	    File sampleDir = Environment.getExternalStorageDirectory();
	    try {
	      audiofile = File.createTempFile("sound", ".3gp", sampleDir);
	    } catch (IOException e) {
	      Log.e(TAG, "sdcard access error");
	      return;
	    }
	    recorder = new MediaRecorder();
	    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    recorder.setOutputFile(audiofile.getAbsolutePath());
	    
		
		//START ADDITIONAL stuff from the app-guys (class: TaskAttachActivity)
		//this function should convert the picture into a byte[] and save it into the db, but it DOESNT work
		
//		datasource = new MySQLiteHelper(getApplicationContext());
//        bitmapUtility = new BitmapUtility();
//        client = new HttpCustomClient();
//        this.taskId = getIntent().getExtras().getString("TaskId");
//        this.taskName = getIntent().getExtras().getString("TaskName");
//        this.assignmentId = getIntent().getExtras().getString("AssignmentId");
//        Picture = (Button)findViewById(R.id.task_att_btn1);
//        
//        Assignment assignment = datasource.getAssignmentById(assignmentId);
//        
//		
//        Picture.setOnClickListener(new View.OnClickListener() {
//        	@Override
//        	public void onClick(View v) {
//
//        	//Updating the task in the database
//        	//Creating a new assignment and store it to the database
//            if (Clicked == 1) {
//
//                //The imagebitmap is transferred to byte[] before storing it to the database
//                //byte[] array = bitmapUtility.getBytes(imageBitmap);
//                Bitmap bmap = IMG.getDrawingCache();
//            	ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
//                byte[] array = bos.toByteArray();
//                System.out.println(array);
//                //TODO: think again
//                Attachment attachment = new Attachment();
//                attachment.setAssignmentId(assignmentId);
//                attachment.setFile_type("Photo");
//                attachment.setBinaryObject(array);
//                attachment.setTaskId(taskId);
//                if(NoPic==1) {
//                    myApp.createAttachment(attachment);
//                    Toast.makeText(getApplicationContext(), "Attachment created ",
//                            Toast.LENGTH_LONG).show();
//                }
//                else{
//                    datasource.deleteAttachment(taskId);
//                    datasource.createAttachment(attachment);
//                    Toast.makeText(getApplicationContext(), "Attachment updated ",
//                            Toast.LENGTH_LONG).show();
//                }
//                task.setState(2);
//                datasource.updateTask(task);
//            }
//            else if(Clicked == 0){
//                task.setState(2);
//                datasource.updateTask(task);
//            }
//        }
//        });
//		
		//END Additional method 4 saving picture
	
		
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
	
	/**
	 * Handles all functions used in the attachments tab
	 * 
	 * @author Stephan Voelkl
	 * @param view
	 */
	public void takePicture(View v) {

        dispatchTakePictureIntent();

    }
	//This method triggers the camera
	private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "File not created",
                        Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
	// the name of a picture is specified here
	private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(4));
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
	
	//audio recording part
	
	  public void startRecording(View view) throws IOException {

		    try{
		    	recorder.prepare();
		        recorder.start();
		    } catch (IOException e){
		    	//prepare() fails
		    	e.printStackTrace();
		    } catch (IllegalStateException e){
		    	// start() before prepare; prepare() after start() or before setOutputFormat()
		    	e.printStackTrace();
		    }
		    startButton.setEnabled(false);
		    stopButton.setEnabled(true);
		    
		    Toast.makeText(getApplicationContext(), "Start recording...",
		    		Toast.LENGTH_SHORT).show();
		  }		

		  public void stopRecording(View view) {
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
		    recorder.stop();
		    recorder.release();
		    addRecordingToMediaLibrary();
		  }

		  protected void addRecordingToMediaLibrary() {
		    ContentValues values = new ContentValues(0);
		    long current = System.currentTimeMillis();
		    values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
		    values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		    values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		    values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		    ContentResolver contentResolver = getContentResolver();

		    Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		    Uri newUri = contentResolver.insert(base, values);

		    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
		    Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
		  }
	
	
	
	
}
