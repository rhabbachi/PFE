package com.tunav.tunavmedi.abstraction;

import java.util.ArrayList;

import com.tunav.tunavmedi.Task;
import com.tunav.tunavmedi.Task.Priority;
import com.tunav.tunavmedi.Task.Status;

public abstract class TasksHandler {
    public ArrayList<Task> getTasks() {
	return null;
    }

    public void setPriority(Long id, Priority priority) {

    }

    public void setStatus(Long id, Status status) {

    }
}
