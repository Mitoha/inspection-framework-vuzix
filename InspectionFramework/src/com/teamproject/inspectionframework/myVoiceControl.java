/* package com.teamproject.inspectionframework;

import android.content.Context;
import com.vuzix.speech.VoiceControl;

class myVoiceControl extends VoiceControl {
	
	private VoiceControl vc;
	
	public myVoiceControl(Context context) {
		super(context);
		vc.on();
	}

	@Override
	protected void onRecognition(String result) {
		Log.i(TAG, result);
	}
	
	
	@Override
	protected void onResume(){
		super.onResume();
		vc.on();
	}
	protected void onPause(){
	super.onPause();
	vc.off();
	}
*/