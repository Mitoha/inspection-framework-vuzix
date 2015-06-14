package com.teamproject.inspectionframework.vuzixHelpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.teamproject.inspectionframework.MyApplication;
import com.teamproject.inspectionframework.Entities.Assignment;
import com.teamproject.inspectionframework.Entities.Attachment;
import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

/**
 * Handles all attachment-related functions for taking photos and making audio
 * recordings
 *
 */
public class AttachmentHandler {

	private MySQLiteHelper datasource;
	private MyApplication myApp;
	private Activity activity;
	private Task task;
	private Assignment assignment;
	private String photoPath;
	MediaRecorder recorder;
	File audiofile;
	private static final int REQUEST_TAKE_PHOTO = 1;

	public AttachmentHandler(Context ctx, Activity activity) {
		myApp = (MyApplication) ctx;
		datasource = new MySQLiteHelper(ctx);
		this.activity = activity;

		assignment = myApp.getAssignment();
		task = myApp.getTask();
	}

	/**
	 * Handles the intent to take a picture
	 */
	public void takePicture() {

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

	/**
	 * Handles the processing of a picture after taking picture intent
	 * 
	 * @param requestCode
	 *            Request code of the invoking method
	 * @param resultCode
	 *            Result code of the camera application
	 * @param data
	 *            Data returned by the photo application
	 * @return The result of the processing
	 * @throws IOException
	 */
	public boolean takePictureResult(int requestCode, int resultCode, Intent data) throws IOException {
		boolean result = false;

		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
			try {
				Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
				byte[] array = stream.toByteArray();

				// Search in DB if task has already a photo attachment
				List<String> attachmentList = datasource.getAttachmentIds(assignment.getId(), task.getId(), "Photo");
				for (int i = 0; i < attachmentList.size(); i++) {
					String oldAttachment = attachmentList.get(i);
					if (oldAttachment != null) {
						datasource.deleteAttachment(oldAttachment);
					}
				}

				Attachment attachment = new Attachment();
				attachment.setTaskId(task.getId());
				attachment.setAssignmentId(assignment.getId());
				attachment.setFile_type("Photo");
				attachment.setBinaryObject(array);
				attachment.setId(assignment.getId() + task.getId() + "_photo");

				datasource.createAttachment(attachment);
				datasource.close();
				result = true;

			} catch (Exception e) {
				Log.e("IF", "An error occurec!", e);
			}
		}

		return result;
	}

	/**
	 * Creates an image file on the local storage directory
	 * 
	 * @return The created file
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMANY).format(new Date());
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
	// ++++++++++++++++++++++++++++++++++++

	/**
	 * Starts the audio recording process
	 * 
	 * @return The result of the process
	 * @throws IOException
	 */
	public boolean startAudioRecording() throws IOException {
		boolean result = false;
		audiofile = createAudioFile();

		Log.i("IF", "StartRecording__Audiofile created!");
		try {
			recorder.prepare();

			// Short break necessary because of slow Vuzix processor
			Thread.sleep(1000);

			recorder.start();

		} catch (IOException e) {
			// prepare() fails
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// start() before prepare; prepare() after start() or before
			// setOutputFormat()
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		result = true;
		return result;

	}

	/**
	 * Stops the audio recording process
	 * 
	 * @return The result of the stopping process
	 * @throws IOException
	 */
	public boolean stopAudioRecording() throws IOException {
		boolean result = false;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			recorder.stop();
			recorder.release();
			addRecordingToMediaLibrary();

			try {
				fis = new FileInputStream(audiofile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				while (fis.available() > 0) {
					bos.write(fis.read());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			fis.close();

			byte[] array = bos.toByteArray();

			// Search in DB if task has already an audio attachment
			List<String> attachmentList = datasource.getAttachmentIds(assignment.getId(), task.getId(), "Audio");
			for (int i = 0; i < attachmentList.size(); i++) {
				String oldAttachment = attachmentList.get(i);
				if (oldAttachment != null) {
					datasource.deleteAttachment(oldAttachment);
				}
			}

			Attachment attachment = new Attachment();
			attachment.setTaskId(task.getId());
			attachment.setAssignmentId(assignment.getId());
			attachment.setFile_type("Audio");
			attachment.setBinaryObject(array);
			attachment.setId(assignment.getId() + task.getId() + "_audio");

			datasource.createAttachment(attachment);
			datasource.close();

			result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Creates an audio file on the local storage directory
	 * 
	 * @return The created file
	 * @throws IOException
	 */
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

	/**
	 * Adds the recorded audio file to the media library
	 */
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
