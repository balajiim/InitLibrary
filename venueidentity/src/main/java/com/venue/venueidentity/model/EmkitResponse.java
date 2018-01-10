package com.venue.venueidentity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 06-12-2017.
 */

public class EmkitResponse {

    public String getEmp2UserId() {
        return emp2UserId;
    }

    public void setEmp2UserId(String emp2UserId) {
        this.emp2UserId = emp2UserId;
    }

    public String getCardDataURL() {
        return cardDataURL;
    }

    public void setCardDataURL(String cardDataURL) {
        this.cardDataURL = cardDataURL;
    }

    public String getSightingURL() {
        return sightingURL;
    }

    public void setSightingURL(String sightingURL) {
        this.sightingURL = sightingURL;
    }

    public String getBeaconsURL() {
        return beaconsURL;
    }

    public void setBeaconsURL(String beaconsURL) {
        this.beaconsURL = beaconsURL;
    }

    public String geteMkitVersionPage() {
        return eMkitVersionPage;
    }

    public void seteMkitVersionPage(String eMkitVersionPage) {
        this.eMkitVersionPage = eMkitVersionPage;
    }

    public String geteMcard_verno() {
        return eMcard_verno;
    }

    public void seteMcard_verno(String eMcard_verno) {
        this.eMcard_verno = eMcard_verno;
    }

    public String getExtCardDataURL() {
        return extCardDataURL;
    }

    public void setExtCardDataURL(String extCardDataURL) {
        this.extCardDataURL = extCardDataURL;
    }

    @SerializedName("emp2UserId")
    @Expose
    private String emp2UserId;

    @SerializedName("cardDataURL")
    @Expose
    private String cardDataURL;

    @SerializedName("sightingURL")
    @Expose
    private String sightingURL;

    @SerializedName("beaconsURL")
    @Expose
    private String beaconsURL;

    @SerializedName("eMkitVersionPage")
    @Expose
    private String eMkitVersionPage;

    @SerializedName("eMcard_verno")
    @Expose
    private String eMcard_verno;

    @SerializedName("extCardDataURL")
    @Expose
    private String extCardDataURL;

}
