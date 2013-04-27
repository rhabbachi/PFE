package com.tunav.tunavmedi.datatypes;

import java.sql.Timestamp;
import java.util.Date;

public class Task {

    private Integer id = null;
    private Timestamp created = null;
    private String description = null;
    private Priority priority = Priority.PRIORITY_UNKNOWEN;
    private Status status = Status.STATUS_UNKNOWEN;
    private Date due = null;
    private Placemark placemark = null;

    public enum Priority {
	PRIORITY_UNKNOWEN, PRIORITY_NORMAL, PRIORITY_HIGH, PRIORITY_CRITICAL;
    }

    public enum Status {
	STATUS_UNKNOWEN, STATUS_PROCEDING, STATUS_DONE, STATUS_DECLINED;
    }

    public Task(Integer id) {
	this.id = id;
    }

    public Task(Integer id, Timestamp created) {
	this(id);
	this.created = created;
    }

    public Task(Integer id, Timestamp created, String description) {
	this(id, created);
	this.description = description;
    }

    public Task(Integer id, Timestamp created, String description,
	    Priority priority) {
	this(id, created, description);
	this.priority = priority;
    }

    public Task(Integer id, Timestamp created, String description,
	    Priority priority, Status status) {
	this(id, created, description, priority);
	this.status = status;
    }

    public Task(Integer id, Timestamp created, String description,
	    Priority priority, Status status, Date due) {
	this(id, created, description, priority, status);
	this.due = due;
    }

    public Task(Integer id, Timestamp created, String description,
	    Priority priority, Status status, Date due, Placemark placemark) {
	this(id, created, description, priority, status, due);
	this.placemark = placemark;
    }

    @Override
    public String toString() {
	// TODO
	return null;
    }

    public long getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Timestamp getCreated() {
	return created;
    }

    public void setCreated(Timestamp created) {
	this.created = created;
    }

    public Priority getPriority() {
	return priority;
    }

    public void setPriority(Priority priority) {
	this.priority = priority;
    }

    public Date getDue() {
	return due;
    }

    public void setDue(Date due) {
	this.due = due;
    }

    public Status getStatus() {
	return status;
    }

    public void setStatus(Status status) {
	this.status = status;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Placemark getPlacemark() {
	return placemark;
    }

    public void setPlacemark(Placemark placemark) {
	this.placemark = placemark;
    }
}
