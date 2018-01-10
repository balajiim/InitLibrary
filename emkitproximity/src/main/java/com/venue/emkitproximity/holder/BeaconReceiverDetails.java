package com.venue.emkitproximity.holder;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BeaconReceiverDetails implements Serializable {

    private String id;
    private String type;
    private String sub_type;
    private String dwell_time;
    private String min_rssi;
    private String max_rssi;
    private String relay_time_interval;
    private String max_sighting_interval;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getDwell_time() {
        return dwell_time;
    }

    public void setDwell_time(String dwell_time) {
        this.dwell_time = dwell_time;
    }

    public String getMin_rssi() {
        return min_rssi;
    }

    public void setMin_rssi(String min_rssi) {
        this.min_rssi = min_rssi;
    }

    public String getMax_rssi() {
        return max_rssi;
    }

    public void setMax_rssi(String max_rssi) {
        this.max_rssi = max_rssi;
    }

    public String getRelay_time_interval() {
        return relay_time_interval;
    }

    public void setRelay_time_interval(String relay_time_interval) {
        this.relay_time_interval = relay_time_interval;
    }

    public String getMax_sighting_interval() {
        return max_sighting_interval;
    }

    public void setMax_sighting_interval(String max_sighting_interval) {
        this.max_sighting_interval = max_sighting_interval;
    }

}