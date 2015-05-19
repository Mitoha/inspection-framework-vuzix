package com.teamproject.inspectionframework.List_Adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamproject.inspectionframework.R;
import com.teamproject.inspectionframework.Entities.User;

public class UserListAdapter extends BaseAdapter {

	List<User> userList;
	Context context;

	// Constructor; provides the needed context and list of tasks given by the
	// calling method
	public UserListAdapter(Context activityContext, List<User> users) {
		super();
		this.context = activityContext;
		this.userList = users;
	}

	@Override
	public int getCount() {
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_element_user_list, parent, false);
		}

		TextView userName = (TextView) convertView.findViewById(R.id.userName);
		TextView realName = (TextView) convertView.findViewById(R.id.firstAndLastName);

		User user = userList.get(position);

		userName.setText(user.getUserName());
		realName.setText(user.getFirstName() + " " + user.getLastName());

		return convertView;
	}

	// Gives the item per position (needed for ClickListener)
	public User getClickedUser(int position) {
		return userList.get(position);
	}

}
