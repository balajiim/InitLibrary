package com.venue.emkitproximity.holder;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CardGimbalDetails implements Serializable {

    private String message;
    private String emkit_URL;
    private String title;
    private String campaignId;
    private String link;
    private String type;

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getInstantpush_id() {
        return instantpush_id;
    }

    public void setInstantpush_id(String instantpush_id) {
        this.instantpush_id = instantpush_id;
    }

    private String campaign_id;
    private String instantpush_id;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmkit_URL() {
        return emkit_URL;
    }

    public void setEmkit_URL(String emkit_URL) {
        this.emkit_URL = emkit_URL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

}