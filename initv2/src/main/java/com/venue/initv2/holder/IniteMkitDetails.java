package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("serial")
public class IniteMkitDetails {

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
    private String eMcardVerno;
    @SerializedName("extCardDataURL")
    @Expose
    private String extCardDataURL;
    @SerializedName("updateStatusInterval")
    @Expose
    private String updateStatusInterval;
    @SerializedName("proximity_services")
    @Expose
    private ProximityServices proximityServices;
    @SerializedName("findMyFriend")
    @Expose
    private FindMyFriend findMyFriend;
    @SerializedName("APPSTOREVERSION")
    @Expose
    private String aPPSTOREVERSION;
    @SerializedName("GOOGLE_ANALYTICS_ID_DEV")
    @Expose
    private String gOOGLEANALYTICSIDDEV;
    @SerializedName("GOOGLE_ANALYTICS_ID_PROD")
    @Expose
    private String gOOGLEANALYTICSIDPROD;

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

    public String getEMkitVersionPage() {
        return eMkitVersionPage;
    }

    public void setEMkitVersionPage(String eMkitVersionPage) {
        this.eMkitVersionPage = eMkitVersionPage;
    }

    public String getEMcardVerno() {
        return eMcardVerno;
    }

    public void setEMcardVerno(String eMcardVerno) {
        this.eMcardVerno = eMcardVerno;
    }

    public String getExtCardDataURL() {
        return extCardDataURL;
    }

    public void setExtCardDataURL(String extCardDataURL) {
        this.extCardDataURL = extCardDataURL;
    }

    public String getUpdateStatusInterval() {
        return updateStatusInterval;
    }

    public void setUpdateStatusInterval(String updateStatusInterval) {
        this.updateStatusInterval = updateStatusInterval;
    }

    public ProximityServices getProximityServices() {
        return proximityServices;
    }

    public void setProximityServices(ProximityServices proximityServices) {
        this.proximityServices = proximityServices;
    }

    public FindMyFriend getFindMyFriend() {
        return findMyFriend;
    }

    public void setFindMyFriend(FindMyFriend findMyFriend) {
        this.findMyFriend = findMyFriend;
    }

    public String getAPPSTOREVERSION() {
        return aPPSTOREVERSION;
    }

    public void setAPPSTOREVERSION(String aPPSTOREVERSION) {
        this.aPPSTOREVERSION = aPPSTOREVERSION;
    }

    public String getGOOGLEANALYTICSIDDEV() {
        return gOOGLEANALYTICSIDDEV;
    }

    public void setGOOGLEANALYTICSIDDEV(String gOOGLEANALYTICSIDDEV) {
        this.gOOGLEANALYTICSIDDEV = gOOGLEANALYTICSIDDEV;
    }

    public String getGOOGLEANALYTICSIDPROD() {
        return gOOGLEANALYTICSIDPROD;
    }

    public void setGOOGLEANALYTICSIDPROD(String gOOGLEANALYTICSIDPROD) {
        this.gOOGLEANALYTICSIDPROD = gOOGLEANALYTICSIDPROD;
    }

}

	
