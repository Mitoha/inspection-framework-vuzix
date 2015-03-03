package com.teamproject.inspectionframework.vuzixHelpers;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.vuzix.speech.VoiceControl;

class VuzixVoiceControl extends VoiceControl {

	private Context context;
	
	public VuzixVoiceControl(Context ctx) {
		super(ctx);
		
	}
	
	public VuzixVoiceControl(Context ctx, String[] grammars) {
		super(ctx, grammars);
	}

	@Override
	protected void onRecognition(String cmd) {
		
		if(cmd.equals("show help")){
			Log.i("IF",cmd);
			
		}
		
	}

}
