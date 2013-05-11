
package com.tunav.tunavmedi.datatype;

import java.util.Date;

public class Patient {
    private Long mID = null;
    private String mFirstName = null;
    private String mLastName = null;
    private Date mBirthDate = null;
    private String mImageName = null;
    private String mInfo = null;

    public Patient(Long id, String first, String last, Date birth) {
        mID = id;
        mFirstName = first;
        mLastName = last;
        mBirthDate = birth;
    }

    public Long getID() {
        return mID;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public Date getBirthDate() {
        return mBirthDate;
    }

    public void setBirthDate(Date mBirthDate) {
        this.mBirthDate = mBirthDate;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String mInfo) {
        this.mInfo = mInfo;
    }

    public Patient(Long id, String first, String last, Date birth, String image) {
        this(id, first, last, birth);
        mImageName = image;
    }

    public Patient(Long id, String first, String last, Date birth, String image, String info) {
        this(id, first, last, birth, image);
        mInfo = info;
    }
}
