package com.teamproject.inspectionframework.vuzixHelpers;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.vuzix.speech.VoiceControl;

public class VuzixVoiceControl extends VoiceControl {

	private String cmd;

	public VuzixVoiceControl(Context ctx) {
		super(ctx);

	}

	public VuzixVoiceControl(Context ctx, String[] grammars) {
		super(ctx, grammars);
	}

	@Override
	protected void onRecognition(String voiceCommand) {
		Log.i("IF", voiceCommand);
		cmd = voiceCommand;

		new Thread(new Runnable() {
			@Override
			public void run() {
				Instrumentation inst = new Instrumentation();
				if (cmd.equals("move left")) {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_LEFT);
				}
				if (cmd.equals("move right")) {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
				}
				if (cmd.equals("move up")) {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_UP);
				}
				if (cmd.equals("move down")) {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
				}
				if (cmd.equals("select")) {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
				}
				if (cmd.equals("go back")) {
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
				}
			}
		}).start();

	}
}
