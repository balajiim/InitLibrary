package com.venue.venuewallet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mastmobilemedia on 22-12-2017.
 */

public class EmvPaymentItem implements Serializable {

    /*@SerializedName("name")
    @Expose
    private String name;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("unit_price")
    @Expose
    private String unit_price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @SerializedName("price")
    @Expose
    private String price;
*/
    public EmvPaymentItem(String itemTitle, String amount) {
        this.itemTitle = itemTitle;
        this.amount = amount;
    }

    public EmvPaymentItem() {

    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @SerializedName("itemTitle")
    @Expose
    private String itemTitle;

    @SerializedName("amount")
    @Expose
    private String amount;


}
