package com.venue.venuewallet.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manager on 15-12-2017.
 */

public class VenueWalletConfigData implements Serializable {

    ArrayList<VenueWalletStmCardsData> STMCards;
    ArrayList<VenueWalletCreditCardsData> CreditCards;
    ArrayList<VenueWalletLoadedValueData> LoadedValue;
    VenueWalletCategoryTitlesData CategoryTitles;
    boolean DirectPaymentSupported;

    public ArrayList<VenueWalletStmCardsData> getSTMCards() {
        return STMCards;
    }

    public void setSTMCards(ArrayList<VenueWalletStmCardsData> STMCards) {
        this.STMCards = STMCards;
    }

    public ArrayList<VenueWalletCreditCardsData> getCreditCards() {
        return CreditCards;
    }

    public void setCreditCards(ArrayList<VenueWalletCreditCardsData> creditCards) {
        CreditCards = creditCards;
    }

    public ArrayList<VenueWalletLoadedValueData> getLoadedValue() {
        return LoadedValue;
    }

    public void setLoadedValue(ArrayList<VenueWalletLoadedValueData> loadedValue) {
        LoadedValue = loadedValue;
    }

    public VenueWalletCategoryTitlesData getCategoryTitles() {
        return CategoryTitles;
    }

    public void setCategoryTitles(VenueWalletCategoryTitlesData categoryTitles) {
        CategoryTitles = categoryTitles;
    }

    public boolean isDirectPaymentSupported() {
        return DirectPaymentSupported;
    }

    public void setDirectPaymentSupported(boolean directPaymentSupported) {
        DirectPaymentSupported = directPaymentSupported;
    }
}
