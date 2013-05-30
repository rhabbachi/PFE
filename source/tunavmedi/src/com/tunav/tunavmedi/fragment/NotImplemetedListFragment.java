
package com.tunav.tunavmedi.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;

public class NotImplemetedListFragment extends ListFragment {
    private static final String TAG = "DummyFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated()");
        setEmptyText("Not Supported Yet!");
        setListAdapter(null);
    }
}
