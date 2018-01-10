package com.venue.emkitproximity.holder;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BeaconSightedDetails implements Serializable {

    private String id;
    private String dwell_time;
    private String min_rssi;
    private String max_rssi;
    private String relay_time_interval;
    private String max_sighting_interval;
    private String beacon_temp;
    private String battery_level;
    private String first_sighted;
    private String last_sighted;
    private String last_sighted_sent_to_server;
    private String previous_rssi;
    private String beacon_rssi;
    private String beacon_name;
    private String beacon_state;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBeacon_temp() {
        return beacon_temp;
    }

    public void setBeacon_temp(String beacon_temp) {
        this.beacon_temp = beacon_temp;
    }

    public String getBattery_level() {
        return battery_level;
    }

    public void setBattery_level(String battery_level) {
        this.battery_level = battery_level;
    }

    public String getFirst_sighted() {
        return first_sighted;
    }

    public void setFirst_sighted(String first_sighted) {
        this.first_sighted = first_sighted;
    }

    public String getLast_sighted() {
        return last_sighted;
    }

    public void setLast_sighted(String last_sighted) {
        this.last_sighted = last_sighted;
    }

    public String getLast_sighted_sent_to_server() {
        return last_sighted_sent_to_server;
    }

    public void setLast_sighted_sent_to_server(String last_sighted_sent_to_server) {
        this.last_sighted_sent_to_server = last_sighted_sent_to_server;
    }

    public String getPrevious_rssi() {
        return previous_rssi;
    }

    public void setPrevious_rssi(String previous_rssi) {
        this.previous_rssi = previous_rssi;
    }

    public String getBeacon_rssi() {
        return beacon_rssi;
    }

    public void setBeacon_rssi(String beacon_rssi) {
        this.beacon_rssi = beacon_rssi;
    }

    public String getBeacon_name() {
        return beacon_name;
    }

    public void setBeacon_name(String beacon_name) {
        this.beacon_name = beacon_name;
    }

    public String getBeacon_state() {
        return beacon_state;
    }

    public void setBeacon_state(String beacon_state) {
        this.beacon_state = beacon_state;
    }
}