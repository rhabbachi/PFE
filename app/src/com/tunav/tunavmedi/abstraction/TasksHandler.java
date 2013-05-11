package com.tunav.tunavmedi.abstraction;

import java.util.ArrayList;

import com.tunav.tunavmedi.datatype.Task;
import com.tunav.tunavmedi.datatype.Task.Priority;
import com.tunav.tunavmedi.datatype.Task.Status;

public abstract class TasksHandler {
    public ArrayList<Task> getTasks() {
	return null;
    }

    public void setPriority(Long id, Priority priority) {

    }

    public void setStatus(Long id, Status status) {

    }
}
