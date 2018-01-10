package com.venue.emkitproximity.holder;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EmkitFriendGeofence implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

}