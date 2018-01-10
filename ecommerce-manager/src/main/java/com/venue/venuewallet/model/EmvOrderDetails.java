package com.venue.venuewallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mastmobilemedia on 22-12-2017.
 */

public class EmvOrderDetails implements Serializable {

    @SerializedName("orderID")
    private String orderID;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("placeId")
    private String placeId;

    @SerializedName("seatId")
    private int seatId;

    @SerializedName("totalAmount")
    private Float totalAmount;

    @SerializedName("items")
    private ArrayList<EmvPaymentItem> items;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<EmvPaymentItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<EmvPaymentItem> items) {
        this.items = items;
    }

    public EmvOrderDetails(String orderID, String customerName, String placeId, int seatId, Float totalAmount, ArrayList<EmvPaymentItem> items) {
        this.orderID = orderID;
        this.customerName = customerName;
        this.placeId = placeId;
        this.seatId = seatId;
        this.totalAmount = totalAmount;
        this.items = items;
    }

}
