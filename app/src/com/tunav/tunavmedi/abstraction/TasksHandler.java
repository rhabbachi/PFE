
package com.tunav.tunavmedi.abstraction;

import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.datatype.Task.Priority;
import com.tunav.tunavmedi.datatype.Task.Status;

import java.util.ArrayList;

public abstract class TasksHandler {
    public interface OnTasksChangedListener {
        public void onTasksChanged();
    }

    private ArrayList<OnTasksChangedListener> mListeners = null;

    public TasksHandler() {
        mListeners = new ArrayList<TasksHandler.OnTasksChangedListener>();
    }

    public void addOnTasksChangedListener(OnTasksChangedListener listener) {
        mListeners.add(listener);
    }

    public ArrayList<Task> getTasks() {
        return null;
    }

    public void notifyTasksChangedListeners() {
        for (OnTasksChangedListener listener : mListeners) {
            listener.onTasksChanged();
        }
    }

    public boolean removeTasksChangedListener(OnTasksChangedListener listener) {
        return mListeners.remove(listener);
    }

    public void setPriority(Long id, Priority priority) {

    }

    public void setStatus(Long id, Status status) {

    }
}
