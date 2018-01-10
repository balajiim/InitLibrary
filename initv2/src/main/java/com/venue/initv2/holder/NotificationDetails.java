package com.venue.initv2.holder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Puspak on 07-12-2017.
 */

public class NotificationDetails {

    @SerializedName("MembershipUpdates")
    @Expose
    private Integer membershipUpdates;
    @SerializedName("BreakingNews")
    @Expose
    private Integer breakingNews;
    @SerializedName("GeneralContent")
    @Expose
    private Integer generalContent;
    @SerializedName("LiveProgramming")
    @Expose
    private Integer liveProgramming;
    @SerializedName("GameUpdates")
    @Expose
    private Integer gameUpdates;
    @SerializedName("Promotions")
    @Expose
    private Integer promotions;

    public Integer getMembershipUpdates() {
        return membershipUpdates;
    }

    public void setMembershipUpdates(Integer membershipUpdates) {
        this.membershipUpdates = membershipUpdates;
    }

    public Integer getBreakingNews() {
        return breakingNews;
    }

    public void setBreakingNews(Integer breakingNews) {
        this.breakingNews = breakingNews;
    }

    public Integer getGeneralContent() {
        return generalContent;
    }

    public void setGeneralContent(Integer generalContent) {
        this.generalContent = generalContent;
    }

    public Integer getLiveProgramming() {
        return liveProgramming;
    }

    public void setLiveProgramming(Integer liveProgramming) {
        this.liveProgramming = liveProgramming;
    }

    public Integer getGameUpdates() {
        return gameUpdates;
    }

    public void setGameUpdates(Integer gameUpdates) {
        this.gameUpdates = gameUpdates;
    }

    public Integer getPromotions() {
        return promotions;
    }

    public void setPromotions(Integer promotions) {
        this.promotions = promotions;
    }


}
