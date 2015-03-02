package com.teamproject.inspectionframework.vuzixHelpers;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.vuzix.speech.VoiceControl;

public class VuzixVoiceControl extends VoiceControl {

	public VuzixVoiceControl(Context context) {
		super(context);
		
	}
	
	public VuzixVoiceControl(Context context, String[] grammars) {
		super(context, grammars);
	}

	@Override
	protected void onRecognition(String cmd) {
		
		if(cmd.equals("show help")){
			
			
		}
		
	}

}
