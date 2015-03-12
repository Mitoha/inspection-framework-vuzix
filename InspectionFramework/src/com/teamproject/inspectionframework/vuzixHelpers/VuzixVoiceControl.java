package com.teamproject.inspectionframework.vuzixHelpers;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.widget.Toast;

import com.vuzix.speech.VoiceControl;

public class VuzixVoiceControl extends VoiceControl {

	private Context context;

	public VuzixVoiceControl(Context ctx) {
		super(ctx);

	}

	public VuzixVoiceControl(Context ctx, String[] grammars) {
		super(ctx, grammars);
	}

	@Override
	protected void onRecognition(String cmd) {

		// General commands: Mapping commands to buttons
		Log.i("IF",cmd);
		if(cmd.equals("move left")) {
			//Instrumentation inst = new Instrumentation();
           // inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_LEFT);
		}
		if(cmd.equals("show help")) {
		}

	}
}
