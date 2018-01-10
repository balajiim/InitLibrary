package com.venue.emkitproximity.holder;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EmkitInitObject implements Serializable {

    private String emp2UserId;
    private String cardDataURL;
    private String sightingURL;
    private String beaconsURL;
    private String eMkitVersionPage;
    private String eMcard_verno;
    private String extCardDataURL;

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

    public EmkitProximityServices getProximity_services() {
        return proximity_services;
    }

    public void setProximity_services(EmkitProximityServices proximity_services) {
        this.proximity_services = proximity_services;
    }

    public EmkitFindMyFriend getFindMyFriend() {
        return findMyFriend;
    }

    public void setFindMyFriend(EmkitFindMyFriend findMyFriend) {
        this.findMyFriend = findMyFriend;
    }

    public String getAPPSTOREVERSION() {
        return APPSTOREVERSION;
    }

    public void setAPPSTOREVERSION(String APPSTOREVERSION) {
        this.APPSTOREVERSION = APPSTOREVERSION;
    }

    public String getGOOGLE_ANALYTICS_ID_DEV() {
        return GOOGLE_ANALYTICS_ID_DEV;
    }

    public void setGOOGLE_ANALYTICS_ID_DEV(String GOOGLE_ANALYTICS_ID_DEV) {
        this.GOOGLE_ANALYTICS_ID_DEV = GOOGLE_ANALYTICS_ID_DEV;
    }

    public String getGOOGLE_ANALYTICS_ID_PROD() {
        return GOOGLE_ANALYTICS_ID_PROD;
    }

    public void setGOOGLE_ANALYTICS_ID_PROD(String GOOGLE_ANALYTICS_ID_PROD) {
        this.GOOGLE_ANALYTICS_ID_PROD = GOOGLE_ANALYTICS_ID_PROD;
    }

    private EmkitProximityServices proximity_services;
    private EmkitFindMyFriend findMyFriend;
    private String APPSTOREVERSION;
    private String GOOGLE_ANALYTICS_ID_DEV;
    private String GOOGLE_ANALYTICS_ID_PROD;

}