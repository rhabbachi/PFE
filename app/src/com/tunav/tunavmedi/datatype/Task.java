
package com.tunav.tunavmedi.datatype;

import android.text.Html;
import android.text.Spanned;

import java.sql.Timestamp;
import java.util.Date;

public class Task {

    private Long mID = null;
    private String mTitle = null;
    private Timestamp mCreationDate = null;
    private String mDescription = null;
    private Priority mPriority = Priority.PRIORITY_NORMAL;
    private Status mStatus = Status.STATUS_PROCEEDING;
    private Date mDue = null;
    private Placemark mPlacemark = null;
    private String mImageName = null;

    public enum Priority {
        // NORMAL < HIGH
        PRIORITY_NORMAL("NORMAL"), PRIORITY_HIGH("HIGH");

        private String mToString = null;

        Priority(String toSring) {
            mToString = toSring;
        }

        @Override
        public String toString() {
            return mToString;
        }
    }

    public enum Status {
        // DONE < PROCEEDING
        STATUS_DONE("DONE"), STATUS_PROCEEDING("PROCEEDING");
        private String mToString = null;

        Status(String toSring) {
            mToString = toSring;
        }

        @Override
        public String toString() {
            return mToString;
        }
    }

    public Task(Long id, String source, Timestamp creationDate,
            String description) {
        mID = id;
        mTitle = source;
        mCreationDate = creationDate;
        mDescription = description;
    }

    public Task(Long id, String title, Timestamp creationDate,
            String description, Priority priority) {
        this(id, title, creationDate, description);
        this.mPriority = priority;
    }

    public Task(Long id, String title, Timestamp creationDate,
            String description, Priority priority, Status status) {
        this(id, title, creationDate, description, priority);
        this.mStatus = status;
    }

    public Task(Long id, String title, Timestamp creationDate,
            String description, Priority priority, Status status, Date due) {
        this(id, title, creationDate, description, priority, status);
        this.mDue = due;
    }

    public Task(Long id, String title, Timestamp creationDate,
            String description, Priority priority, Status status, Date due,
            Placemark placemark) {
        this(id, title, creationDate, description, priority, status, due);
        this.mPlacemark = placemark;
    }

    public Task(Long id, String title, Timestamp creationDate,
            String description, Priority priority, Status status, Date due,
            Placemark placemark, String imagePath) {
        this(id, title, creationDate, description, priority, status, due,
                placemark);
        mImageName = imagePath;
    }

    @Override
    public String toString() {
        // TODO
        return null;
    }

    public long getId() {
        return mID;
    }

    public Timestamp getCreationDate() {
        return mCreationDate;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        this.mPriority = priority;
    }

    public Date getDueDate() {
        return mDue;
    }

    public void setDueDate(Date due) {
        this.mDue = due;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
    }

    public Spanned getDescriptionHTML() {
        return Html.fromHtml(mDescription);
    }

    public String getDescription() {
        return mDescription;
    }

    public Placemark getPlacemark() {
        return mPlacemark;
    }

    public void setPlacemark(Placemark placemark) {
        this.mPlacemark = placemark;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imagePath) {
        this.mImageName = imagePath;
    }
}
