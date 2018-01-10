package com.venue.venuewallet.model;

import java.io.Serializable;

/**
 * Created by manager on 15-12-2017.
 */

public class VenueWalletConfigResponse implements Serializable {


    String name;
    String code;
    String wso2_root_endpoint;
    boolean is_sandbox;
    VenueWalletConfigData config_dict;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWso2_root_endpoint() {
        return wso2_root_endpoint;
    }

    public void setWso2_root_endpoint(String wso2_root_endpoint) {
        this.wso2_root_endpoint = wso2_root_endpoint;
    }

    public boolean is_sandbox() {
        return is_sandbox;
    }

    public void setIs_sandbox(boolean is_sandbox) {
        this.is_sandbox = is_sandbox;
    }

    public VenueWalletConfigData getConfig_dict() {
        return config_dict;
    }

    public void setConfig_dict(VenueWalletConfigData config_dict) {
        this.config_dict = config_dict;
    }
}
