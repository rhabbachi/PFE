
package com.tunav.tunavmedi.dal.abstraction;

import android.util.Log;
import com.tunav.tunavmedi.datatype.Patient;

import java.util.ArrayList;
import java.util.Observable;

public abstract class PatientsHandler extends Observable {
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

    public ArrayList<Patient> pullPatients() {
        return null;
    }

    public int pushPatients(ArrayList<Patient> patients) {
        return 0;
    }

    protected static void setNotificationThread(Runnable runnable) {
        notificationRunnable = runnable;
    }
}
