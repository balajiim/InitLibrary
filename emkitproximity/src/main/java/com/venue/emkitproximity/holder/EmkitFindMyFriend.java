package com.venue.emkitproximity.holder;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class EmkitFindMyFriend implements Serializable {

    public ArrayList<EmkitFriendGeofence> getGeofences() {
        return geofences;
    }

    public void setGeofences(ArrayList<EmkitFriendGeofence> geofences) {
        this.geofences = geofences;
    }

    private ArrayList<EmkitFriendGeofence> geofences;

    private String FMF_relay_time_interval;

    public String getFMF_relay_time_interval() {
        return FMF_relay_time_interval;
    }

    public void setFMF_relay_time_interval(String fMF_relay_time_interval) {
        FMF_relay_time_interval = fMF_relay_time_interval;
    }

}