package com.venue.venuewallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mastmobilemedia on 22-12-2017.
 */

public class EmvPaymentRequest implements Serializable {

    @SerializedName("EmvOrderDetails")
    private EmvOrderDetails EmvOrderDetails;

    @SerializedName("EmvPaymentSummary")
    private EmvPaymentSummary EmvPaymentSummary;

    public EmvOrderDetails getEmvOrderDetails() {
        return EmvOrderDetails;
    }

    public void setEmvOrderDetails(EmvOrderDetails emvOrderDetails) {
        this.EmvOrderDetails = emvOrderDetails;
    }

    public EmvPaymentSummary getEmvPaymentSummary() {
        return EmvPaymentSummary;
    }

    public void setEmvPaymentSummary(EmvPaymentSummary emvPaymentSummary) {
        this.EmvPaymentSummary = emvPaymentSummary;
    }

    public EmvPaymentRequest(EmvOrderDetails EmvOrderDetails, EmvPaymentSummary EmvPaymentSummary) {
        this.EmvOrderDetails = EmvOrderDetails;
        this.EmvPaymentSummary = EmvPaymentSummary;
    }
}
