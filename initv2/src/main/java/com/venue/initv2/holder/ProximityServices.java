package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Puspak on 06-12-2017.
 */

public class ProximityServices {
    @SerializedName("gimbal_geo")
    @Expose
    private String gimbalGeo;
    @SerializedName("gimbal_ble")
    @Expose
    private String gimbalBle;
    @SerializedName("signal360_audio")
    @Expose
    private String signal360Audio;
    @SerializedName("signal360_ble")
    @Expose
    private String signal360Ble;
    @SerializedName("signal360_geo")
    @Expose
    private String signal360Geo;
    @SerializedName("radius_ble")
    @Expose
    private String radiusBle;
    @SerializedName("radius_geo")
    @Expose
    private String radiusGeo;
    @SerializedName("listenr_audio")
    @Expose
    private String listenrAudio;
    @SerializedName("footmarks_ble")
    @Expose
    private String footmarksBle;

    public String getGimbalGeo() {
        return gimbalGeo;
    }

    public void setGimbalGeo(String gimbalGeo) {
        this.gimbalGeo = gimbalGeo;
    }

    public String getGimbalBle() {
        return gimbalBle;
    }

    public void setGimbalBle(String gimbalBle) {
        this.gimbalBle = gimbalBle;
    }

    public String getSignal360Audio() {
        return signal360Audio;
    }

    public void setSignal360Audio(String signal360Audio) {
        this.signal360Audio = signal360Audio;
    }

    public String getSignal360Ble() {
        return signal360Ble;
    }

    public void setSignal360Ble(String signal360Ble) {
        this.signal360Ble = signal360Ble;
    }

    public String getSignal360Geo() {
        return signal360Geo;
    }

    public void setSignal360Geo(String signal360Geo) {
        this.signal360Geo = signal360Geo;
    }

    public String getRadiusBle() {
        return radiusBle;
    }

    public void setRadiusBle(String radiusBle) {
        this.radiusBle = radiusBle;
    }

    public String getRadiusGeo() {
        return radiusGeo;
    }

    public void setRadiusGeo(String radiusGeo) {
        this.radiusGeo = radiusGeo;
    }

    public String getListenrAudio() {
        return listenrAudio;
    }

    public void setListenrAudio(String listenrAudio) {
        this.listenrAudio = listenrAudio;
    }

    public String getFootmarksBle() {
        return footmarksBle;
    }

    public void setFootmarksBle(String footmarksBle) {
        this.footmarksBle = footmarksBle;
    }

}
