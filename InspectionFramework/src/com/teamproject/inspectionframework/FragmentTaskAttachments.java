package com.teamproject.inspectionframework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTaskAttachments extends Fragment {
	
	private MyApplication myApp;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		myApp = (MyApplication) getActivity().getApplicationContext();
 
        View rootView = inflater.inflate(R.layout.fragment_task_attachments, container, false);
        
        return rootView;
    }
}
