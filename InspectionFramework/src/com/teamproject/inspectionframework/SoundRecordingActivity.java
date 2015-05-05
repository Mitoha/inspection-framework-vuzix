//play ground
package com.teamproject.inspectionframework;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class SoundRecordingActivity extends Activity {

  MediaRecorder recorder;
  File audiofile = null;
  private static final String TAG = "SoundRecordingActivity";
  private View startButton2;
  private View stopButton2;
  
   
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_soundrecordingactivity);
    startButton2 = findViewById(R.id.start);
    stopButton2 = findViewById(R.id.stop);
    
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
    
    
  }
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.soundrecording, menu);
		return true;
	}

  public void startRecording2(View view) throws IOException {

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
    startButton2.setEnabled(false);
    stopButton2.setEnabled(true);
    
    Toast.makeText(getApplicationContext(), "Start recording...",
    		Toast.LENGTH_SHORT).show();
  }		

  public void stopRecording2(View view) {
    startButton2.setEnabled(true);
    stopButton2.setEnabled(false);
    recorder.stop();
    recorder.release();
    addRecordingToMediaLibrary2();
  }

  protected void addRecordingToMediaLibrary2() {
    ContentValues values = new ContentValues(4);
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