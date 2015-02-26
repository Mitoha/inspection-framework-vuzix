package com.teamproject.inspectionframework.List_Adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.Entities.Assignment;

public class AssignmentAdapter extends BaseAdapter {

	List<Assignment> assignmentList;
	Context context;
	
	//Constructor; provides the needed context and list of assignments given by the calling method
	public AssignmentAdapter(Context activityContext, List<Assignment> assignments) {
		super();
		this.context = activityContext;
		this.assignmentList = assignments;
	}

	@Override
	public int getCount() {
		return assignmentList.size();
	}

	@Override
	public Object getItem(int position) {
		return assignmentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_element_assignment_list, parent, false);
		}
		TextView Name = (TextView) convertView.findViewById(R.id.assignmentName);
		TextView DueDate = (TextView) convertView.findViewById(R.id.assignmentDueDate);

		Assignment assignment = assignmentList.get(position);

		Name.setText(assignment.getAssignmentName());
		DueDate.setText(assignment.getDueDate().toString());

		return convertView;
	}
	
	//Gives the item per position (needed for ClickListener)
	public Assignment getClickedAssignment(int position) {
		return assignmentList.get(position);
	}

}
