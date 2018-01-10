package com.venue.venuewallet.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 28-12-2017.
 */

public class TE2 {

    @SerializedName("id")
    private String id;
    @SerializedName("place_id")
    private String place_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
