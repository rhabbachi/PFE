package com.tunav.tunavmedi.datatypes;

import java.sql.Timestamp;
import java.util.Date;

public class Task {

    private Date date;
    private int priority;
    private Date dueDate;
    private int status;
    private String description;
    private String description_short;
    private Placemark place;

    public static final int PRIORITY_UNKNOWEN = -1;
    public static final int PRIORITY_NORMAL = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_CRITICAL = 2;

    public static final int STATUS_UNKNOWEN = -1;
    public static final int STATUS_PROCEDING = 0;
    public static final int STATUS_DONE = 1;
    public static final int STATUS_DECLINED = 2;

    public Task() {
	priority = PRIORITY_UNKNOWEN;
	status = STATUS_UNKNOWEN;
	date = null;
	dueDate = null;
    }

    @Override
    public String toString() {
	// TODO
	return null;
    }
}
