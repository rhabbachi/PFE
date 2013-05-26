
package com.tunav.tunavmedi.abstraction;

import android.util.Log;
import com.tunav.tunavmedi.datatype.Task;

import java.util.ArrayList;
import java.util.Observable;

public abstract class TasksHandler extends Observable {
    public static final String tag = "TasksHandler";
    public static Runnable notificationRunnable = new Runnable() {

        @Override
        public void run() {
            String msg = "Default notification thread.";
            Log.d(tag, msg);
        }
    };

    public Runnable getNotificationRunnable() {
        return notificationRunnable;
    }

    public ArrayList<Task> pullTasks() {
        return null;
    }

    public int pushTask(ArrayList<Task> tasks) {
        return 0;
    }

    protected static void setNotificationThread(Runnable runnable) {
        notificationRunnable = runnable;
    }
}
