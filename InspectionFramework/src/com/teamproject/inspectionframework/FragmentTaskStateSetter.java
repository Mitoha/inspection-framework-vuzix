package com.teamproject.inspectionframework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Fragment for the task state setter tab
 *
 */
public class FragmentTaskStateSetter extends Fragment {

	private MyApplication myApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		myApp = (MyApplication) getActivity().getApplicationContext();

		View rootView = inflater.inflate(R.layout.fragment_task_state_setter, container, false);

		// Set task description
		TextView taskDescription = (TextView) rootView.findViewById(R.id.tvTaskDescription);
		String descr = myApp.getTask().getDescription();

		if (descr == null || descr == "" || descr.isEmpty()) {
			taskDescription.setText("No description available");
		} else {
			taskDescription.setText(descr);
		}

		return rootView;

	}
}
