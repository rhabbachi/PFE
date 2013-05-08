package com.tunav.tunavmedi.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.tunav.tunavmedi.R;
import com.tunav.tunavmedi.TunavMedi;
import com.tunav.tunavmedi.adapter.TasksAdapter;

public class TaskListFragment extends ListFragment {

	private static final String tag = "TaskListFragment";

	private Activity mParentActivity = null;
	private TasksAdapter mTasksAdapter = null;
	private SwipeListView mSwipeListView = null;
	private ProgressDialog progressDialog = null;
	private TextView emptyView = null;

	// Called when the Fragment is attached to its parent Activity.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(tag, "onAttach()");
		// Get a reference to the parent Activity.
		mParentActivity = activity;
	}

	// Called to do the initial creation of the Fragment.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(tag, "onCreate()");
		// Initialize the Fragment.
		// for the action bar items
		setHasOptionsMenu(true);

		// setup the tasks adapter
		mTasksAdapter = new TasksAdapter(mParentActivity);

		// progress dialog
		progressDialog = new ProgressDialog(mParentActivity);
	}

	// Called once the Fragment has been created in order for it to
	// create its user interface.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(tag, "onCreateView()");
		// Create, or inflate the Fragment's UI, and return it.
		// If this Fragment has no UI then return null.
		return inflater.inflate(R.layout.fragmenttaskslist_swipe, container,
				false);
	}

	// Called once the parent Activity and the Fragment's UI have
	// been created.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v(tag, "onActivityCreated()");
		// Complete the Fragment initialization Ð particularly anything
		// that requires the parent Activity to be initialized or the
		// Fragment's view to be fully inflated.

		mSwipeListView = (SwipeListView) getListView();
		mSwipeListView.setSwipeListViewListener(mBaseSwipeListViewListener);
		emptyView = (TextView) mSwipeListView.getEmptyView();
		setListAdapter(mTasksAdapter);
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		super.onStart();
		Log.v(tag, "onStart()");
		// Apply any required UI change now that the Fragment is visible.
	}

	// Called at the start of the active lifetime.
	@Override
	public void onResume() {
		super.onResume();
		Log.v(tag, "onResume()");
		// Resume any paused UI updates, threads, or processes required
		// by the Fragment but suspended when it became inactive.
	}

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		Log.v(tag, "onPause()");
		// Suspend UI updates, threads, or CPU intensive processes
		// that don't need to be updated when the Activity isn't
		// the active foreground activity.
		// Persist all edits or state changes
		// as after this call the process is likely to be killed.
		super.onPause();
	}

	// Called to save UI state changes at the
	// end of the active lifecycle.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		Log.v(tag, "onSaveInstanceState()");
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate, onCreateView, and
		// onCreateView if the parent Activity is killed and restarted.
		super.onSaveInstanceState(savedInstanceState);
	}

	// Called at the end of the visible lifetime.
	@Override
	public void onStop() {
		Log.v(tag, "onStop()");
		// Suspend remaining UI updates, threads, or processing
		// that aren't required when the Fragment isn't visible.
		super.onStop();
	}

	// Called when the Fragment's View has been detached.
	@Override
	public void onDestroyView() {
		Log.v(tag, "onDestroyView()");
		// Clean up resources related to the View.
		mSwipeListView = null;
		super.onDestroyView();
	}

	// Called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		Log.v(tag, "onDestroy()");
		// Clean up any resources including ending threads,
		// closing database connections etc.
		super.onDestroy();
	}

	// Called when the Fragment has been detached from its parent Activity.
	@Override
	public void onDetach() {
		Log.v(tag, "onDetach()");
		super.onDetach();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
		super.onCreateOptionsMenu(menu, menuInflater);
		Log.v(tag, "onCreateOptionsMenu()");
		menuInflater.inflate(R.menu.fragment_tasklist, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(tag, "onOptionsItemSelected()");

		SharedPreferences.Editor spEditor = mParentActivity
				.getSharedPreferences(TunavMedi.SHAREDPREFS_NAME,
						Context.MODE_PRIVATE).edit();
		boolean state = item.isChecked();

		switch (item.getItemId()) {
		case R.id.tasklist_menu_done:
			spEditor.putBoolean(
					mParentActivity.getResources().getString(
							R.string.tasklist_sp_show_done), state);
			spEditor.apply();
			if (state) {
				item.setTitle(R.string.tasklist_menu_done_title_on);
			} else {
				item.setTitle(R.string.tasklist_menu_done_title_off);
			}
			item.setChecked(!state);
			return true;
		case R.id.tasklist_menu_location:
			spEditor.putBoolean(
					mParentActivity.getResources().getString(
							R.string.tasklist_sp_sort_location), state);
			spEditor.apply();
			if (state) {
				item.setTitle(R.string.tasklist_menu_location_title_on);
			} else {
				item.setTitle(R.string.tasklist_menu_location_title_off);
			}
			item.setChecked(!state);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.v(tag, "onListItemClick()");
		// Insert desired behavior here.
		Log.i(tag, "Item clicked: " + id);
	}

	BaseSwipeListViewListener mBaseSwipeListViewListener = new BaseSwipeListViewListener() {
		private static final String tag = "TaskListFragment.mBaseSwipeListViewListener";

		@Override
		public void onOpened(int position, boolean toRight) {
			Log.v(tag, String.format("onOpened(position: %d, toRight: %s)",
					position, toRight ? "true" : "false"));
		}

		@Override
		public void onClosed(int position, boolean fromRight) {
			Log.v(tag, String.format("onClosed(position: %d, toRight: %s)",
					position, fromRight ? "true" : "false"));
		}

		@Override
		public void onListChanged() {
			Log.v(tag, "onListChanged()");
		}

		@Override
		public void onMove(int position, float x) {
			Log.v(tag,
					String.format("onMove(position: %d, x: %f )", position, x));
		}

		@Override
		public void onStartOpen(int position, int action, boolean right) {
			Log.v(tag, String.format("onStartOpen %d - action %d", position,
					action));
		}

		@Override
		public void onStartClose(int position, boolean right) {
			Log.v(tag, String.format("onStartClose %d", position));
		}

		@Override
		public void onClickFrontView(int position) {
			Log.v(tag, String.format("onClickFrontView %d", position));
		}

		@Override
		public void onClickBackView(int position) {
			Log.v(tag, String.format("onClickBackView %d", position));
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				Log.v(tag, "" + position);
			}
			mTasksAdapter.notifyDataSetChanged();
		}
	};
}
