package com.teamproject.inspectionframework.vuzixHelpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.teamproject.inspectionframework.MyApplication;
import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.R.id;
import com.teamproject.inspectionframework.R.layout;
import com.teamproject.inspectionframework.Application_Layer.BitmapUtility;
import com.teamproject.inspectionframework.Application_Layer.HttpCustomClient;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Attachment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;
import com.vuzix.hardware.VuzixCamera;
import com.vuzix.hardware.VuzixCamera.PictureCallback;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.media.videoeditor.MediaItem.GetThumbnailListCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AttachmentHandler {

	private MySQLiteHelper datasource;
	private MyApplication myApp;
	private Context context;
	private Activity activity;
	private Task task;
	private Assignment assignment;
	private String photoPath;
	private String attachmentTarget;
	private MediaRecorder recorder;
	private File audiofile;
	private static final int REQUEST_TAKE_PHOTO = 1;

	public AttachmentHandler(Context ctx, Activity activity) {
		myApp = (MyApplication) ctx;
		datasource = new MySQLiteHelper(ctx);
		context = ctx;
		this.activity = activity;

		assignment = myApp.getAssignment();
		task = myApp.getTask();
	}

	public void takePicture(String target) {
		this.attachmentTarget = target;

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Create the File where the photo should go
		File photoFile = null;
		try {
			photoFile = createImageFile();
		} catch (IOException ex) {
			// Error occurred while creating the File
			Log.e("IF", "Error while taking picture");
		}
		// Continue only if the File was successfully created
		if (photoFile != null) {
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

		}
	}

	public boolean takePictureResult(int requestCode, int resultCode, Intent data) throws IOException {
		boolean result = false;

		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
			try {
				Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] array = stream.toByteArray();

				Attachment attachment = new Attachment();
				attachment.setTaskId(task.getId());
				attachment.setAssignmentId(assignment.getId());
				attachment.setFile_type("Photo");
				attachment.setBinaryObject(array);

				datasource.createAttachment(attachment);
				datasource.close();
				result = true;
			} catch (Exception e) {
				Log.e("IF", "An error occurec!", e);
			}
		}

		return result;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		photoPath = image.getAbsolutePath();
		return image;
	}

	// Audio recording methods
	public boolean startAudioRecording(String target) throws IOException {
		boolean result = false;
		this.attachmentTarget = target;
		audiofile = createAudioFile();
		try {
			recorder.prepare();
			recorder.start();
		} catch (IOException e) {
			// prepare() fails
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// start() before prepare; prepare() after start() or before
			// setOutputFormat()
			e.printStackTrace();
		}
		result = true;
		return result;

	}

	public boolean stopAudioRecording(String target) throws IOException {
		boolean result = false;
		try {
			recorder.stop();
			recorder.release();
			addRecordingToMediaLibrary();
			
//			Attachment attachment = new Attachment();
//			attachment.setTaskId(task.getId());
//			attachment.setAssignmentId(assignment.getId());
//			attachment.setFile_type("Photo");
//			attachment.setBinaryObject(array);
//
//			datasource.createAttachment(attachment);
//			datasource.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private File createAudioFile() throws IOException {
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		File audiofile = null;

		try {
			audiofile = File.createTempFile("sound", ".3gp", storageDir);
		} catch (IOException e) {
			Log.e("IF", "SDcard access error");
		}
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(audiofile.getAbsolutePath());

		return audiofile;
	}

	private void addRecordingToMediaLibrary() {
		ContentValues values = new ContentValues(0);
		long current = System.currentTimeMillis();
		values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		ContentResolver contentResolver = activity.getContentResolver();

		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);

		activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	}

}
