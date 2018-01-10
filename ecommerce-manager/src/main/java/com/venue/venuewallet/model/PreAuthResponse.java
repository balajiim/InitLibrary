package com.venue.venuewallet.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 28-12-2017.
 */

public class PreAuthResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("credit_card")
    private CreditCard credit_card;
    @SerializedName("point_of_sale")
    private PointOfSale point_of_sale;

    public PointOfSale getPoint_of_sale() {
        return point_of_sale;
    }

    public void setPoint_of_sale(PointOfSale point_of_sale) {
        this.point_of_sale = point_of_sale;
    }

    public CreditCard getCredit_card() {
        return credit_card;
    }

    public void setCredit_card(CreditCard credit_card) {
        this.credit_card = credit_card;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHmac_token() {
        return hmac_token;
    }

    public void setHmac_token(String hmac_token) {
        this.hmac_token = hmac_token;
    }

    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("hmac_token")
    private String hmac_token;
    @SerializedName("payment_gateway")
    private String payment_gateway;
    @SerializedName("merchant")
    private Merchant merchant;

    public String getPayment_gateway() {
        return payment_gateway;
    }

    public void setPayment_gateway(String payment_gateway) {
        this.payment_gateway = payment_gateway;
    }

    public TE2 getTe2_details() {
        return te2_details;
    }

    public void setTe2_details(TE2 te2_details) {
        this.te2_details = te2_details;
    }

    @SerializedName("te2_details")
    private TE2 te2_details;

}
