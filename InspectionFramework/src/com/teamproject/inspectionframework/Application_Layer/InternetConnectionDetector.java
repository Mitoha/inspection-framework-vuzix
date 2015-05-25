package com.teamproject.inspectionframework.Application_Layer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Checks whether an internet connection is available
 *
 */
public class InternetConnectionDetector {

	// Var-declaration
	private Context _context;

	// Constructor
	public InternetConnectionDetector(Context context) {
		this._context = context;
	}

	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
}
