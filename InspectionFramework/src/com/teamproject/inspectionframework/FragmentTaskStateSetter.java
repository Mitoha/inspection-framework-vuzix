package com.teamproject.inspectionframework;

import com.teamproject.inspectionframework.Entities.Task;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTaskStateSetter extends Fragment {
	
	private MySQLiteHelper datasource;
	private Task task;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_task_state_setter, container, false);
        
        //Set task description
		TextView taskDescription = (TextView) rootView.findViewById(R.id.tvTaskDescription);
		
		datasource = new MySQLiteHelper(getActivity());
		task = datasource.getTaskById(getActivity().getIntent().getExtras().getString("taskId"));
		String descr = task.getDescription();
		
		if(descr == null || descr == "" || descr.isEmpty()) {
			taskDescription.setText("No description available");
		}
		else {
			taskDescription.setText(descr);
		}
        
        return rootView;
        
    }
}
