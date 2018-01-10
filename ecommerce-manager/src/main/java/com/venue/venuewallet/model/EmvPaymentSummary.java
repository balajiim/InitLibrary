package com.venue.venuewallet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mastmobilemedia on 22-12-2017.
 */

public class EmvPaymentSummary implements Serializable {

    public ArrayList<EmvPaymentItem> getPaymentItems() {
        return items;
    }

    public void setPaymentItems(ArrayList<EmvPaymentItem> paymentItems) {
        this.items = paymentItems;
    }

    @SerializedName("items")
    @Expose
    private ArrayList<EmvPaymentItem> items;

    public EmvPaymentSummary(ArrayList<EmvPaymentItem> items) {
        this.items = items;
    }

}
