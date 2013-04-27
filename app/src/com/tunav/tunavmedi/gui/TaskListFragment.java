package com.tunav.tunavmedi.gui;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.helpers.sqlite.TasksHelper;
import com.tunav.tunavmedi.interfaces.TasksHandler;
import com.tunav.tunavmedi.interfaces.TasksListener;

public class TaskListFragment extends ListFragment implements TasksListener {

    private static final String TAG = "TaskListFragment";

    private Context mContext = null;
    private TasksAdapter mTaskAdapter = null;
    private TasksHandler mHelper = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	Log.v(TAG, "onActivityCreated()");
	mContext = getActivity().getApplicationContext();
	mHelper = new TasksHelper(mContext);
	mTaskAdapter = new TasksAdapter(mContext, mHelper);
	setEmptyText(getResources().getText(R.string.task_list_empty));
	setListAdapter(mTaskAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	Log.v(TAG, "onListItemClick()");
	// Insert desired behavior here.
	Log.i(TAG, "Item clicked: " + id);
    }

    @Override
    public void onNewTask() {
	Log.v(TAG, "onNewTask()");
	// TODO Auto-generated method stub
    }
}
