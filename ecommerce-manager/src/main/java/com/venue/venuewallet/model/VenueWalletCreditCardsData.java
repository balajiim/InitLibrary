package com.venue.venuewallet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by manager on 15-12-2017.
 */

public class VenueWalletCreditCardsData implements Serializable {

    @SerializedName("cardName")
    @Expose
    String cardName;
    @SerializedName("operator")
    @Expose
    String operator;
    @SerializedName("cardImage")
    @Expose
    String cardImage;
    @SerializedName("insertCardName")
    @Expose
    boolean insertCardName;
    @SerializedName("cardDescription")
    @Expose
    String cardDescription;
    @SerializedName("insertCardNumber")
    @Expose
    boolean insertCardNumber;
    @SerializedName("managewallletcardImage")
    @Expose
    String managewallletcardImage;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public boolean isInsertCardName() {
        return insertCardName;
    }

    public void setInsertCardName(boolean insertCardName) {
        this.insertCardName = insertCardName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public boolean isInsertCardNumber() {
        return insertCardNumber;
    }

    public void setInsertCardNumber(boolean insertCardNumber) {
        this.insertCardNumber = insertCardNumber;
    }

    public String getManagewallletcardImage() {
        return managewallletcardImage;
    }

    public void setManagewallletcardImage(String managewallletcardImage) {
        this.managewallletcardImage = managewallletcardImage;
    }
}
