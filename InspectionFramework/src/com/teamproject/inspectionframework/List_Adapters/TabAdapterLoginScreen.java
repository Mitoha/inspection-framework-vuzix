package com.teamproject.inspectionframework.List_Adapters;

import com.teamproject.inspectionframework.FragmentUserList;
import com.teamproject.inspectionframework.FragmentUserLogin;
import com.teamproject.inspectionframework.FragmentTaskAttachment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabAdapterLoginScreen extends FragmentPagerAdapter {
 
    public TabAdapterLoginScreen(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // User Login Fragment
            return new FragmentUserLogin();
        case 1:
            // User List Fragment
            return new FragmentUserList();
        case 2:
        	//Task Attachment Fragment
        	return new FragmentTaskAttachment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}
