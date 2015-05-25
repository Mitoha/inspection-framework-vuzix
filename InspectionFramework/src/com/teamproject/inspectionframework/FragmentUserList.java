package com.teamproject.inspectionframework;

import java.util.List;

import com.teamproject.inspectionframework.Entities.User;
import com.teamproject.inspectionframework.List_Adapters.UserListAdapter;
import com.teamproject.inspectionframework.Persistence_Layer.MySQLiteHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Fragment for the user list tab
 *
 */
public class FragmentUserList extends ListFragment {

	// VAR-declaration
	private MySQLiteHelper datasource;
	private UserListAdapter adapter;
	private MyApplication myApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		myApp = (MyApplication) getActivity().getApplicationContext();

		View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);

		this.createOutputList();
		return rootView;
	}

	/**
	 * Creates the user list
	 */
	public void createOutputList() {
		datasource = new MySQLiteHelper(getActivity());
		List<User> listWithAllStoredUsers = datasource.getAllUser();

		adapter = new UserListAdapter(getActivity(), listWithAllStoredUsers);
		this.setListAdapter(adapter);

		datasource.close();
	}

	public void onListItemClick(ListView l, android.view.View v, int position, long id) {

		User clickedUser = adapter.getClickedUser(position);

		Intent goToAssignmentIntent = new Intent(getActivity(), AssignmentList.class);
		myApp.setUser(clickedUser.getUserId());

		startActivity(goToAssignmentIntent);
	};
}
