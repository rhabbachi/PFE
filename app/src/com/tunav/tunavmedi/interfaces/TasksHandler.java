package com.tunav.tunavmedi.interfaces;

import java.sql.Timestamp;
import java.util.List;

import com.tunav.tunavmedi.datatypes.Task;

public interface TasksHandler {
    void close();

    static final int MODE_READ = 1;
    static final int MODE_WRITE = 2;
    static final int MODE_READ_WRITE = 3;

    boolean open(int mode);

    List<Task> getAllTasks();

    Task getTask(long taskID);

    void markTaskDone(long taskID);

    /*
     * Checks if tasks are updated server side, as new tasks are added or their
     * status are updated. Mostly used in a services.
     * 
     * @return true if the tasks set is updated, false if not.
     */
    boolean newTasksAvailable(Timestamp mostRecentTask);

    /*
     * Get the most recent tasks available after the mostRecentTask time-stamp
     * exclusive.
     * 
     * @param Timestamp mostRecentTask
     * 
     * @return List<Task> list of tasks containing only the recent ones
     * available after the passed time-stamp.
     */
    List<Task> getNewTasks(Timestamp mostRecentTask);
}
