package com.teamproject.inspectionframework.vuzixHelpers;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import com.vuzix.speech.VoiceControl;

public class VuzixVoiceControl extends VoiceControl {

	public VuzixVoiceControl(Context ctx) {
		super(ctx);

	}

	public VuzixVoiceControl(Context ctx, String[] grammars) {
		super(ctx, grammars);
	}

	@Override
	public void onRecognition(String cmd) {
		
		System.out.println(cmd);

		// General commands: Mapping commands to buttons
		if(cmd.equals("show help")) {

			//Instrumentation inst = new Instrumentation();
           // inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_LEFT);
		}

	}
}
