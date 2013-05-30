
package com.tunav.tunavmedi.dal.abstraction;

import android.util.Log;

import com.tunav.tunavmedi.datatype.Patient;

import java.util.ArrayList;
import java.util.Observable;

public abstract class PatientsHandler extends Observable {

    public static final String tag = "PatientsHandler";

    public static Runnable notifyTask = new Runnable() {

        @Override
        public void run() {
            String msg = "Default notification thread.";
            Log.d(tag, msg);
        }
    };

    protected static void setNotifyTask(Runnable runnable) {
        notifyTask = runnable;
    }

    public Runnable getNotifyTask() {
        return notifyTask;
    }

    public ArrayList<Patient> pullPatients() {
        return null;
    }

    public int pushPatients(ArrayList<Patient> patients) {
        return 0;
    }
}
