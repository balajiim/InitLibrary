package com.venue.emkitproximity.holder;

import android.content.Context;

import java.io.Serializable;

@SuppressWarnings("serial")
public class IniteMkitDetails implements Serializable {

    private String emp2UserId;
    private String cardDataURL;
    private String sightingURL;
    private String beaconsURL;
    private String eMkitVersionPage;
    private String extCardDataURL;
    private String APPSTOREVERSION;
    private String GOOGLE_ANALYTICS_ID_DEV;
    public String GOOGLE_ANALYTICS_ID_PROD;
    private String updateStatusInterval;

    public String getUpdateStatusInterval() {
        return updateStatusInterval;
    }

    public void setUpdateStatusInterval(String updateStatusInterval) {
        this.updateStatusInterval = updateStatusInterval;
    }

    private EmkitProximityServices proximity_services;

    public String getAPPSTOREVERSION() {
        return APPSTOREVERSION;
    }

    public void setAPPSTOREVERSION(String aPPSTOREVERSION) {
        APPSTOREVERSION = aPPSTOREVERSION;
    }

    public String getGOOGLE_ANALYTICS_ID_DEV() {
        return GOOGLE_ANALYTICS_ID_DEV;
    }

    public void setGOOGLE_ANALYTICS_ID_DEV(String gOOGLE_ANALYTICS_ID_DEV) {
        GOOGLE_ANALYTICS_ID_DEV = gOOGLE_ANALYTICS_ID_DEV;
    }

    public String getGOOGLE_ANALYTICS_ID_PROD() {
        return GOOGLE_ANALYTICS_ID_PROD;
    }

    public void setGOOGLE_ANALYTICS_ID_PROD(String gOOGLE_ANALYTICS_ID_PROD) {
        GOOGLE_ANALYTICS_ID_PROD = gOOGLE_ANALYTICS_ID_PROD;
    }

    public EmkitProximityServices getProximity_services() {
        return proximity_services;
    }

    public void setProximity_services(EmkitProximityServices proximity_services) {
        this.proximity_services = proximity_services;
    }

    public String getExtCardDataURL() {
        return extCardDataURL;
    }

    private String newcradDataStructure = "NO";

    public IniteMkitDetails() {

    }

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

    public void setExtCardDataURL(String extCardDataURL) {
        this.extCardDataURL = extCardDataURL;
    }

    public void openCard(Context context, CardDetails cardDetails) {

		/*Intent mainIntent = new Intent(context, EMCardActivity.class);
        Bundle bundle =  new Bundle();
		
		bundle.putSerializable("cardDetails", cardDetails);
		bundle.putBoolean("fromFile", true);
		mainIntent.putExtras(bundle);	
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(mainIntent);*/

    }


}