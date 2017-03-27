package com.tacops;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Topgun on 12/7/2016.
 */

public class Alert {

    //Class member data
    Date timestamp;
    String object;
    String event;

    public Alert(Date timestamp, String object, String event) {
        this.timestamp = timestamp;
        this.object = object;
        this.event = event;
    }

    public Alert(String object, String event) {
        this.timestamp = new Date();
        this.object = object;
        this.event = event;
    }

    public Alert(){
        this.timestamp = new Date();
        this.object = null;
        this.event = null;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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
