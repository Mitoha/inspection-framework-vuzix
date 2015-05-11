package com.teamproject.inspectionframework;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.teamproject.inspectionframework.Application_Layer.BitmapUtility;
import com.teamproject.inspectionframework.Entities.Attachment;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;
import com.teamproject.inspectionframework.R;

//TODO: CHANGE THIS METHOD SO THAT IT APPLIES TO THE TASK ATTACHMENT SCREEN AND IS ALSO WORKING ACCORDINGLY


//Button Butt, Save;
//	private ImageView IMG;
//	private int REQUEST_IMAGE_CAPTURE =1;
//	private MySQLiteHelper datasource;
//	private Bitmap imageBitmap;
//	private BitmapUtility bitmapUtility;
//

public class AssignmentAttachment extends Activity {

	private static final int REQUEST_IMAGE_CAPTURE = 0;
	private static final int ACTIVITY_RECORD_SOUND = 0;
	private static final int RECORD_SOUND = 0;
	private static final int REQUESTCODE_RECORDING = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_attachment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_attachment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void takePicture(View v) {

        dispatchTakePictureIntent();

    }
	
	public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

	
}
