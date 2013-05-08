package com.tunav.tunavmedi.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.Task;
import com.tunav.tunavmedi.Task.Priority;
import com.tunav.tunavmedi.Task.Status;
import com.tunav.tunavmedi.TunavMedi;
import com.tunav.tunavmedi.abstraction.TasksHandler;
import com.tunav.tunavmedi.demo.sqlite.helper.TasksHelper;

public class TasksAdapter extends BaseAdapter {
	private static final String tag = "TasksAdapter";

	private Context mContext = null;
	private LayoutInflater mInflater;
	private TasksHandler mHelper = null;
	private ArrayList<Task> mTasksList = null;
	private boolean showDone = false;
	private boolean sortPlacemark = true;
	private SharedPreferences mSharedPreferences = null;
	private OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			Log.v(tag, "onSharedPreferenceChanged()");

			String spDone = mContext.getResources().getString(
					R.string.tasklist_sp_show_done);
			String spLocation = mContext.getResources().getString(
					R.string.tasklist_sp_sort_location);
			if (key == spDone) {
				showDone = sharedPreferences.getBoolean(spDone, true);
			} else if (key == spLocation) {
				sortPlacemark = sharedPreferences.getBoolean(spLocation, true);
			} else {
				return;
			}
			sort();
			notifyDataSetChanged();
		}
	};

	// Elegant sort technique
	private enum TasksSort implements Comparator<Task> {
		SORT_DONE {
			@Override
			public int compare(Task lhs, Task rhs) {
				return lhs.getStatus().compareTo(rhs.getStatus());
			}
		},

		SORT_PRIORITY {
			@Override
			public int compare(Task lhs, Task rhs) {
				return lhs.getPriority().compareTo(rhs.getPriority());
			}
		},

		SORT_LOCATION {
			@Override
			public int compare(Task lhs, Task rhs) {
				return lhs.getPlacemark().compareTo(rhs.getPlacemark());
			}
		};
	}

	public static Comparator<Task> getComparator(
			final TasksSort... multipleOptions) {
		return new Comparator<Task>() {
			public int compare(Task o1, Task o2) {
				for (TasksSort option : multipleOptions) {
					int result = option.compare(o1, o2);
					if (result != 0) {
						return result;
					}
				}
				return 0;
			}
		};
	}

	// Nice and beautiful!

	public TasksAdapter(Context context) {
		Log.v(tag, "TasksAdapter()");
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mHelper = new TasksHelper(mContext);
		mTasksList = mHelper.getTasks();
		mSharedPreferences = mContext.getSharedPreferences(
				TunavMedi.SHAREDPREFS_NAME, Context.MODE_PRIVATE);
		mSharedPreferences
				.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
	}

	@Override
	public int getCount() {
		Log.v(tag, "getCount()");
		int size = mTasksList.size();
		Log.d(tag, "size= " + size);
		// return size;
		return 0;
	}

	@Override
	public Task getItem(int position) {
		Log.v(tag, "getItem()");
		Log.v(tag, "position = " + position);
		try {
			return mTasksList.get(position);
		} catch (IndexOutOfBoundsException tr) {
			Log.e(tag, Log.getStackTraceString(tr));
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Log.v(tag, "getItemId()");
		Log.v(tag, "position = " + position);
		try {
			Task task = mTasksList.get(position);
			return task.getId();
		} catch (IndexOutOfBoundsException tr) {
			Log.e(tag, Log.getStackTraceString(tr));
		}
		return -1;
	}

	static class TaskViewHolder {
		ImageView taskImage;
		TextView taskTitle;
		TextView taskDescription;
		TextView taskTime;
		ToggleButton buttonLocation;
		ToggleButton buttonPriority;
		ToggleButton buttonReminder;
		ToggleButton buttonDone;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.v(tag, "getView()");
		TaskViewHolder viewHolder = null;
		Task task = getItem(position);
		Long taskID = getItemId(position);

		if (convertView == null
				|| convertView.getTag(R.id.TAG_TASKVIEW) == null) {
			convertView = mInflater.inflate(
					R.layout.fragmenttaskslist_swipe_item, parent, false);
			viewHolder = new TaskViewHolder();
			viewHolder.taskImage = (ImageView) convertView
					.findViewById(R.id.front_icon);
			viewHolder.taskTitle = (TextView) convertView
					.findViewById(R.id.front_title);
			viewHolder.taskDescription = (TextView) convertView
					.findViewById(R.id.front_description);
			viewHolder.taskTime = (TextView) convertView
					.findViewById(R.id.front_time);
			viewHolder.buttonLocation = (ToggleButton) convertView
					.findViewById(R.id.back_button_location);
			viewHolder.buttonPriority = (ToggleButton) convertView
					.findViewById(R.id.back_button_priority);
			viewHolder.buttonReminder = (ToggleButton) convertView
					.findViewById(R.id.back_button_reminder);
			viewHolder.buttonDone = (ToggleButton) convertView
					.findViewById(R.id.back_button_done);

		} else {
			viewHolder = (TaskViewHolder) convertView.getTag(R.id.TAG_TASKVIEW);
		}

		if (task.getDrawable() == null) {
			viewHolder.taskImage.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.hospital));
		} else {
			viewHolder.taskImage.setImageDrawable(task.getDrawable());
		}

		viewHolder.taskTitle.setText(task.getTitle());
		try {
			viewHolder.taskDescription.setText(R.string.task_list_dummy);
		} catch (IndexOutOfBoundsException tr) {
			Log.e(tag, task.getDescription(), tr);
		}

		viewHolder.taskTime.setText(android.text.format.DateUtils
				.getRelativeTimeSpanString(task.getCreationDate().getTime()));// TODO

		// DONE
		viewHolder.buttonDone
				.setOnCheckedChangeListener(mOnCheckedChangeListener);
		viewHolder.buttonDone.setTag(R.id.TAG_TASK_ID, taskID);
		switch (task.getStatus()) {
		case STATUS_DONE:
			viewHolder.buttonDone.setEnabled(true);
			viewHolder.buttonDone.setChecked(true);
			break;
		case STATUS_PROCEEDING:
			viewHolder.buttonDone.setEnabled(true);
			viewHolder.buttonDone.setChecked(false);
			break;
		default:
			viewHolder.buttonDone.setEnabled(false);
			viewHolder.buttonDone.setChecked(false);
			break;
		}

		// LOCATION
		viewHolder.buttonLocation
				.setOnCheckedChangeListener(mOnCheckedChangeListener);
		viewHolder.buttonLocation.setTag(R.id.TAG_TASK_ID, taskID);

		// PRIORITY
		viewHolder.buttonPriority
				.setOnCheckedChangeListener(mOnCheckedChangeListener);
		viewHolder.buttonPriority.setTag(R.id.TAG_TASK_ID, taskID);

		// REMINDER
		viewHolder.buttonReminder
				.setOnCheckedChangeListener(mOnCheckedChangeListener);
		viewHolder.buttonReminder.setTag(R.id.TAG_TASK_ID, taskID);
		// TODO check status from server

		convertView.setTag(R.id.TAG_TASKVIEW, viewHolder);
		convertView.setTag(R.id.TAG_TASK_ID, taskID);

		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		Log.v(tag, "notifyDataSetChanged()");
		super.notifyDataSetChanged();
	}

	public void updateDataSet() {
		Log.v(tag, "updateDataSet()");
		mTasksList = mHelper.getTasks();
		notifyDataSetChanged();
	}

	private void sort() {
		Log.v(tag, "sort()");
		Collections.sort(mTasksList, getComparator(TasksSort.SORT_DONE));
	}

	private OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			Log.v(tag, "onCheckedChanged()");
			if (buttonView.isEnabled()) {
				Long taskID = (Long) buttonView.getTag(R.id.TAG_TASK_ID);
				if (taskID == null) {
					// Error, revet button back
					Log.e(tag, "taskID null");
					buttonView.setChecked(!isChecked);
				} else {
					switch (buttonView.getId()) {
					case R.id.back_button_done:
						Log.i(tag, "Done: " + (isChecked ? "on" : "off"));
						mHelper.setStatus(taskID,
								isChecked ? Status.STATUS_DONE
										: Status.STATUS_PROCEEDING);
						break;
					case R.id.back_button_location:
						Log.i(tag, "Location: " + (isChecked ? "on" : "off"));
						// TODO
						break;
					case R.id.back_button_priority:
						Log.i(tag, "Priority: " + (isChecked ? "on" : "off"));
						mHelper.setPriority(taskID,
								isChecked ? Priority.PRIORITY_HIGH
										: Priority.PRIORITY_NORMAL);
						break;
					case R.id.back_button_reminder:
						Log.i(tag, "Reminder: " + (isChecked ? "on" : "off"));
						// TODO
						break;
					default:
						break;
					}
				}
			}
		}
	};
}
