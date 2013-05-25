package com.tunav.tunavmedi.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class NotImplemetedListFragment extends ListFragment {
    private static final String TAG = "TaskListFragment";

    private Context mContext = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	Log.v(TAG, "onActivityCreated()");
	mContext = getActivity().getApplicationContext();
	setEmptyText("Not Supported Yet!");
	setListAdapter(null);
    }
}
