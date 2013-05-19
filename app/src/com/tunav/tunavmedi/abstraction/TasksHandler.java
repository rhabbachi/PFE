
package com.tunav.tunavmedi.abstraction;

import com.tunav.tunavmedi.datatype.Task;

import java.util.ArrayList;

public abstract class TasksHandler {

    public interface OnTasksChangedListener {
        public void onTasksChanged();
    }

    public static Runnable notificationRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO default implementation exists with code 0

        }
    };
    private static ArrayList<OnTasksChangedListener> mListeners = null;

    public static void addOnTasksChangedListener(OnTasksChangedListener listener) {
        mListeners.add(listener);
    }

    public TasksHandler() {
        mListeners = new ArrayList<TasksHandler.OnTasksChangedListener>();
    }

    public Runnable getNotificationRunnable() {
        return notificationRunnable;
    }

    public void notifyTasksChangedListeners() {
        for (OnTasksChangedListener listener : mListeners) {
            listener.onTasksChanged();
        }
    }

    public ArrayList<Task> pullTasks() {
        return null;
    }

    public boolean pushTask(Task task) {
        return false;
    }

    public boolean removeTasksChangedListener(OnTasksChangedListener listener) {
        return mListeners.remove(listener);
    }

    public void setNotificationThread(Runnable runnable) {
        notificationRunnable = runnable;
    }
}
