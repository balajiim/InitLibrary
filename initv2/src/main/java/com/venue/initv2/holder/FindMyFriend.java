package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Puspak on 06-12-2017.
 */

public class FindMyFriend {
    @SerializedName("geofences")
    @Expose
    private List<Geofence> geofences = null;
    @SerializedName("FMF_relay_time_interval")
    @Expose
    private String fMFRelayTimeInterval;

    public List<Geofence> getGeofences() {
        return geofences;
    }

    public void setGeofences(List<Geofence> geofences) {
        this.geofences = geofences;
    }

    public String getFMFRelayTimeInterval() {
        return fMFRelayTimeInterval;
    }

    public void setFMFRelayTimeInterval(String fMFRelayTimeInterval) {
        this.fMFRelayTimeInterval = fMFRelayTimeInterval;
    }

}

