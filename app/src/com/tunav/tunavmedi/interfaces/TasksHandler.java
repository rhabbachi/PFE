package com.tunav.tunavmedi.interfaces;

import java.util.ArrayList;

import com.tunav.tunavmedi.datatypes.Task;
import com.tunav.tunavmedi.datatypes.Task.Status;

public interface TasksHandler extends Runnable {
    public ArrayList<Task> getTasks();

    public void updateTaskStatus(long taskId, Status status);
}
