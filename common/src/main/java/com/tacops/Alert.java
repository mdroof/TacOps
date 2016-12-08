package com.tacops;

import java.security.Timestamp;

/**
 * Created by Topgun on 12/7/2016.
 */

public class Alert {

    //Class member data
    Timestamp timestamp;
    String object;
    String event;

    public Alert(Timestamp timestamp, String object, String event) {
        this.timestamp = timestamp;
        this.object = object;
        this.event = event;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
