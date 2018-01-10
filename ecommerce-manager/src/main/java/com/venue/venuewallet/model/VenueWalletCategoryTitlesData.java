package com.venue.venuewallet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by manager on 15-12-2017.
 */

public class VenueWalletCategoryTitlesData implements Serializable {

    @SerializedName("LONGTERM")
    @Expose
    String LONGTERM;
    @SerializedName("SHORTTERM")
    @Expose
    String SHORTTERM;
    @SerializedName("CREDITCARDS")
    @Expose
    String CREDITCARDS;
    @SerializedName("STOREDVALUE")
    @Expose
    String STOREDVALUE;

    public String getLONGTERM() {
        return LONGTERM;
    }

    public void setLONGTERM(String LONGTERM) {
        this.LONGTERM = LONGTERM;
    }

    public String getSHORTTERM() {
        return SHORTTERM;
    }

    public void setSHORTTERM(String SHORTTERM) {
        this.SHORTTERM = SHORTTERM;
    }

    public String getCREDITCARDS() {
        return CREDITCARDS;
    }

    public void setCREDITCARDS(String CREDITCARDS) {
        this.CREDITCARDS = CREDITCARDS;
    }

    public String getSTOREDVALUE() {
        return STOREDVALUE;
    }

    public void setSTOREDVALUE(String STOREDVALUE) {
        this.STOREDVALUE = STOREDVALUE;
    }
}
