package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Puspak on 08-12-2017.
 */

public class LogEvent {
    @SerializedName("emp2UserId")
    @Expose
    private String emp2UserId;
    @SerializedName("eventCategory")
    @Expose
    private String eventCategory;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("eventValue")
    @Expose
    private String eventValue;
    @SerializedName("screenReference")
    @Expose
    private String screenReference;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    public String getEmp2UserId() {
        return emp2UserId;
    }

    public void setEmp2UserId(String emp2UserId) {
        this.emp2UserId = emp2UserId;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getScreenReference() {
        return screenReference;
    }

    public void setScreenReference(String screenReference) {
        this.screenReference = screenReference;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
