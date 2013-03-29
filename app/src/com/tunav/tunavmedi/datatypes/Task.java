package com.tunav.tunavmedi.datatypes;

import java.sql.Timestamp;
import java.util.Date;

public class Task {

    private long id;
    public static final String KEY_ID = "id";
    private Timestamp created;
    public static final String KEY_CREATED = "created";
    private int priority;
    public static final String KEY_PRIORITY = "priority";
    private Date due;
    public static final String KEY_DATE_DUE = "due";
    private int status;
    public static final String KEY_STATUS = "status";
    private String description;
    static final String KEY_DESCRIPTION = "description";
    private String description_short;
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    private Placemark placemark;
    public static final String KEY_PLACEMARK = "placemark";

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
	created = null;
	due = null;
    }

    @Override
    public String toString() {
	// TODO
	return null;
    }
}
