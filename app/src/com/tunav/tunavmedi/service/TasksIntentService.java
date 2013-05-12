
package com.tunav.tunavmedi.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;

public class TasksIntentService extends IntentService {
    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public TasksIntentService getService() {
            // Return this instance of LocalService so clients can call public
            // methods
            return TasksIntentService.this;
        }
    }

    public static final String tag = "TasksIntentService";

    public TasksIntentService() {
        super(tag);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

    }
}
