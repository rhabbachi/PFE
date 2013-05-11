
package com.tunav.tunavmedi.datatype;

import java.util.Date;

public class Record {
    private Long mID = null;
    private Patient mPatient = null;
    private Date mUpdated = null;
    private String mRecord = null;

    public Record(long id, Patient patient, Date updated, String record) {
        mID = id;
        mPatient = patient;
        mUpdated = updated;
        mRecord = record;
    }

    public Long getID() {
        return mID;
    }

    public Patient getPatient() {
        return mPatient;
    }

    public Date getUpdated() {
        return mUpdated;
    }

    public String getRecord() {
        return mRecord;
    }
}
