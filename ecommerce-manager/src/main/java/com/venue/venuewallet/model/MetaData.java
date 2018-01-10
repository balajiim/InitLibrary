package com.venue.venuewallet.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 28-12-2017.
 */

public class MetaData {

    @SerializedName("response_code")
    private String response_code;

    @SerializedName("auth_code")
    private String auth_code;

    @SerializedName("avs_result_code")
    private String avs_result_code;

    @SerializedName("cvv_result_code")
    private String cvv_result_code;

    @SerializedName("cavv_result_code")
    private String cavv_result_code;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getAvs_result_code() {
        return avs_result_code;
    }

    public void setAvs_result_code(String avs_result_code) {
        this.avs_result_code = avs_result_code;
    }

    public String getCvv_result_code() {
        return cvv_result_code;
    }

    public void setCvv_result_code(String cvv_result_code) {
        this.cvv_result_code = cvv_result_code;
    }

    public String getCavv_result_code() {
        return cavv_result_code;
    }

    public void setCavv_result_code(String cavv_result_code) {
        this.cavv_result_code = cavv_result_code;
    }
}
