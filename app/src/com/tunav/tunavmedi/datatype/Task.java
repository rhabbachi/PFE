
package com.tunav.tunavmedi.datatype;

import android.text.Html;
import android.text.Spanned;

import java.util.Date;

public class Task {

    // TODO add support for done time
    private Long mID = null;
    private String mTitle = null;
    private Long mCreated = null;
    private String mDescription = null;
    private Boolean isUrgent = false;
    private Boolean isDone = false;
    private Boolean notify = false;

    private Date mDue = null;

    private Long mUpdated = null;

    private Placemark mPlacemark = null;
    private String mImageName = null;

    public Task(Long id, String source, Long created, Long updated,
            String description) {
        mID = id;
        mTitle = source;
        mCreated = created;
        mUpdated = updated;
        mDescription = description;
    }

    public Task(Long id, String title, Long creationDate, Long updated,
            String description, boolean priority) {
        this(id, title, creationDate, updated, description);
        this.isUrgent = priority;
    }

    public Task(Long id, String title, Long creationDate, Long updated,
            String description, boolean priority, boolean status) {
        this(id, title, creationDate, updated, description, priority);
        this.isDone = status;
    }

    public Task(Long id, String title, Long creationDate, Long updated,
            String description, boolean priority, boolean status, Date due) {
        this(id, title, creationDate, updated, description, priority, status);
        this.mDue = due;
    }

    public Task(Long id, String title, Long creationDate, Long updated,
            String description, boolean priority, boolean status, Date due,
            Placemark placemark) {
        this(id, title, creationDate, updated, description, priority, status, due);
        this.mPlacemark = placemark;
    }

    public Task(Long id, String title, Long creationDate, Long updated,
            String description, boolean priority, boolean status, Date due,
            Placemark placemark, String imagePath) {
        this(id, title, creationDate, updated, description, priority, status, due,
                placemark);
        mImageName = imagePath;
    }

    public Long getCreated() {
        return mCreated;
    }

    public String getDescription() {
        return mDescription;
    }

    public Spanned getDescriptionHTML() {
        return Html.fromHtml(mDescription);
    }

    public Date getDueDate() {
        return mDue;
    }

    public long getId() {
        return mID;
    }

    public String getImageName() {
        return mImageName;
    }

    public Boolean getNotify() {
        return notify;
    }

    public Placemark getPlacemark() {
        return mPlacemark;
    }

    public String getTitle() {
        return mTitle;
    }

    public Long getUpdated() {
        return mUpdated;
    }

    public Boolean isDone() {
        return isDone;
    }

    public Boolean isUrgent() {
        return isUrgent;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public void setDueDate(Date due) {
        this.mDue = due;
    }

    public void setImageName(String imagePath) {
        this.mImageName = imagePath;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public void setPlacemark(Placemark placemark) {
        this.mPlacemark = placemark;
    }

    public void setUpdated(Long updated) {
        mUpdated = updated;
    }

    public void setUrgent(boolean urgent) {
        this.isUrgent = urgent;
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }
}
