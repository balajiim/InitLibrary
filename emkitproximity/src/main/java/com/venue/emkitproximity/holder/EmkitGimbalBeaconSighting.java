package com.venue.emkitproximity.holder;

public class EmkitGimbalBeaconSighting {

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getBeaconRssi() {
        return beaconRssi;
    }

    public void setBeaconRssi(String beaconRssi) {
        this.beaconRssi = beaconRssi;
    }

    public String getBeaconTemp() {
        return beaconTemp;
    }

    public void setBeaconTemp(String beaconTemp) {
        this.beaconTemp = beaconTemp;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    private String beaconName;
    private String batteryLevel;
    private String beaconRssi;
    private String beaconTemp;
    private String beaconId;

}
