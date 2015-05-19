package com.teamproject.inspectionframework.List_Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.teamproject.inspectionframework.FragmentTaskAttachments;
import com.teamproject.inspectionframework.FragmentTaskStateSetter;

public class TabAdapterTaskDetails extends FragmentPagerAdapter {

	public TabAdapterTaskDetails(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// User Login Fragment
			return new FragmentTaskStateSetter();
		case 1:
			// User List Fragment
			return new FragmentTaskAttachments();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
