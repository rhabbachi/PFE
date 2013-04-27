package com.tunav.tunavmedi.gui;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.datatypes.Task;
import com.tunav.tunavmedi.interfaces.TasksHandler;

public class TasksAdapter extends BaseAdapter {
    private static final String TAG = "TasksAdapter";

    private Context mContext = null;
    private TasksHandler mHelper = null;
    private Thread mHelperThread = null;
    private ArrayList<Task> mTasksList = new ArrayList<Task>();
    private LayoutInflater mInflater;

    public TasksAdapter(Context context, TasksHandler helper) {
	Log.v(TAG, "TasksAdapter()");
	this.mContext = context;
	this.mHelper = helper;
	// Thread helperThread = new Thread(helper);
	// helperThread.start();
	mTasksList = mHelper.getTasks();
	mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
	Log.v(TAG, "getCount()");
	// int size = mTasksList.size();
	// Log.d(TAG, "size= " + size);
	// return size;
	return 2;
    }

    @Override
    public Object getItem(int position) {
	Log.v(TAG, "getItem()");
	Log.v(TAG, "position = " + position);
	try {
	    return mTasksList.get(position);
	} catch (IndexOutOfBoundsException tr) {
	    Log.getStackTraceString(tr);
	}
	return null;
    }

    @Override
    public long getItemId(int position) {
	Log.v(TAG, "getItemId()");
	Log.v(TAG, "position = " + position);
	try {
	    Task task = mTasksList.get(position);
	    return task.getId();
	} catch (IndexOutOfBoundsException tr) {
	    Log.getStackTraceString(tr);
	}
	return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	Log.v(TAG, "getView()");
	if (convertView == null) {
	    convertView = mInflater.inflate(
		    R.layout.fragment_tasks_list_item_dummy, null);
	}
	return convertView;
    }
}
