package com.teamproject.inspectionframework.List_Adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.Entities.Task;

public class TaskListAdapter extends BaseAdapter {

	List<Task> taskList;
	Context context;

	// Constructor; provides the needed context and list of tasks given by the
	// calling method
	public TaskListAdapter(Context activityContext, List<Task> tasks) {
		super();
		this.context = activityContext;
		this.taskList = tasks;
	}

	@Override
	public int getCount() {
		return taskList.size();
	}

	@Override
	public Object getItem(int position) {
		return taskList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_element_task_list, parent, false);
		}
		TextView Name = (TextView) convertView.findViewById(R.id.taskName);
		TextView State = (TextView) convertView.findViewById(R.id.taskState);
		

		Task task = taskList.get(position);

		// TODO: Insert conversion from number to real state descr. or logo
		Name.setText(task.getTaskName());
		State.setText(String.valueOf(task.getState()));

		return convertView;
	}

	// Gives the item per position (needed for ClickListener)
	public Task getClickedTask(int position) {
		return taskList.get(position);
	}

}
