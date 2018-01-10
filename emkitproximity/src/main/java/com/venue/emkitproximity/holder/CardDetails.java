package com.venue.emkitproximity.holder;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CardDetails implements Serializable {

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    private String cardId;
    private String autoSave;
    private int[] pageId;
    private Details details;
    private Value html5URL;

    public Value getHtml5URL() {
        return html5URL;
    }

    public void setHtml5URL(Value html5url) {
        html5URL = html5url;
    }

    /*private String title;

    private String imageUrl;
    private String bgImage;
    private String mediaPropertyURL;
    private String headerImageUrl;*/
    /*public String getHeaderImageUrl() {
        return headerImageUrl;
	}
	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}*/
    private String shareTextTitle;
    private String campaignId;
    //private String logoImageURL;
    private String cardGroupId;
    private String cardGroupName;
    /*private String menuFont;
    private String menuColor;
    private String menuProperties;
    private String date;*/
	/*private String arrivalTextDescFont;
	private String arrivalTextDescColor;
	private String arrivalTextDescValue;
	private String arrivalText;
	private String arrivalTextColor;
	private String arrivalTextFont;
	private String arrivalText4;
	private String arrivalText4Color;
	private String arrivalText4Font;
	private String arrivalText3;
	private String arrivalText3Color;
	private String arrivalText3Font;*/
/*	private String mediaPropertyURLAction_id;
	private String mediaPropertyURLProperties;
	private String mediaPropertyURLValue;
	private String arrivalText2;
	private String arrivalText2Color;
	private String arrivalText2Font;
	private String arrivalText1;
	private String arrivalText1Color;
	private String arrivalText1Font;*/
    private String icon;
    /*	private String titleFont;
        private String titlecColor;
        private String titleProperties;
        private String titleValue;*/
    private String passUrl;
    private String qrcodes;
    private String cardName;
    private String cardExpirationDate;
    private String taskId;
    private String taskName;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private ArrayList<MenuItems> listMenuItems;


    /*public String getLogoImageURL() {
        return logoImageURL;
    }
    public void setLogoImageURL(String logoImageURL) {
        this.logoImageURL = logoImageURL;
    }*/
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(String autoSave) {
        this.autoSave = autoSave;
    }

    public int[] getPageId() {
        return pageId;
    }

    public void setPageId(int[] pageId) {
        this.pageId = pageId;
    }

    /*public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }*/
	/*public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtml5URL() {
		return html5URL;
	}
	public void setHtml5URL(String html5url) {
		html5URL = html5url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getBgImage() {
		return bgImage;
	}
	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}
	public String getMediaPropertyURL() {
		return mediaPropertyURL;
	}
	public void setMediaPropertyURL(String mediaPropertyURL) {
		this.mediaPropertyURL = mediaPropertyURL;
	}*/
    public String getShareTextTitle() {
        return shareTextTitle;
    }

    public void setShareTextTitle(String shareTextTitle) {
        this.shareTextTitle = shareTextTitle;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCardGroupId() {
        return cardGroupId;
    }

    public void setCardGroupId(String cardGroupId) {
        this.cardGroupId = cardGroupId;
    }

    public String getCardGroupName() {
        return cardGroupName;
    }

    public void setCardGroupName(String cardGroupName) {
        this.cardGroupName = cardGroupName;
    }

    public ArrayList<MenuItems> getListMenuItems() {
        return listMenuItems;
    }

    public void setListMenuItems(ArrayList<MenuItems> listMenuItems) {
        this.listMenuItems = listMenuItems;
    }

    /*public String getMenuFont() {
        return menuFont;
    }
    public void setMenuFont(String menuFont) {
        this.menuFont = menuFont;
    }
    public String getMenuColor() {
        return menuColor;
    }
    public void setMenuColor(String menuColor) {
        this.menuColor = menuColor;
    }
    public String getMenuProperties() {
        return menuProperties;
    }
    public void setMenuProperties(String menuProperties) {
        this.menuProperties = menuProperties;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getArrivalTextDescFont() {
        return arrivalTextDescFont;
    }
    public void setArrivalTextDescFont(String arrivalTextDescFont) {
        this.arrivalTextDescFont = arrivalTextDescFont;
    }
    public String getArrivalTextDescColor() {
        return arrivalTextDescColor;
    }
    public void setArrivalTextDescColor(String arrivalTextDescColor) {
        this.arrivalTextDescColor = arrivalTextDescColor;
    }
    public String getArrivalTextDescValue() {
        return arrivalTextDescValue;
    }
    public void setArrivalTextDescValue(String arrivalTextDescValue) {
        this.arrivalTextDescValue = arrivalTextDescValue;
    }
    public String getArrivalText() {
        return arrivalText;
    }
    public void setArrivalText(String arrivalText) {
        this.arrivalText = arrivalText;
    }
    public String getArrivalText4() {
        return arrivalText4;
    }
    public void setArrivalText4(String arrivalText4) {
        this.arrivalText4 = arrivalText4;
    }
    public String getArrivalText3() {
        return arrivalText3;
    }
    public void setArrivalText3(String arrivalText3) {
        this.arrivalText3 = arrivalText3;
    }
    public String getMediaPropertyURLAction_id() {
        return mediaPropertyURLAction_id;
    }
    public void setMediaPropertyURLAction_id(String mediaPropertyURLAction_id) {
        this.mediaPropertyURLAction_id = mediaPropertyURLAction_id;
    }
    public String getMediaPropertyURLProperties() {
        return mediaPropertyURLProperties;
    }
    public void setMediaPropertyURLProperties(String mediaPropertyURLProperties) {
        this.mediaPropertyURLProperties = mediaPropertyURLProperties;
    }
    public String getMediaPropertyURLValue() {
        return mediaPropertyURLValue;
    }
    public void setMediaPropertyURLValue(String mediaPropertyURLValue) {
        this.mediaPropertyURLValue = mediaPropertyURLValue;
    }
    public String getArrivalText2() {
        return arrivalText2;
    }
    public void setArrivalText2(String arrivalText2) {
        this.arrivalText2 = arrivalText2;
    }
    public String getArrivalText1() {
        return arrivalText1;
    }
    public void setArrivalText1(String arrivalText1) {
        this.arrivalText1 = arrivalText1;
    }*/
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /*public String getTitleFont() {
        return titleFont;
    }
    public void setTitleFont(String titleFont) {
        this.titleFont = titleFont;
    }
    public String getTitlecColor() {
        return titlecColor;
    }
    public void setTitlecColor(String titlecColor) {
        this.titlecColor = titlecColor;
    }
    public String getTitleProperties() {
        return titleProperties;
    }
    public void setTitleProperties(String titleProperties) {
        this.titleProperties = titleProperties;
    }
    public String getTitleValue() {
        return titleValue;
    }
    public void setTitleValue(String titleValue) {
        this.titleValue = titleValue;
    }*/
    public String getPassUrl() {
        return passUrl;
    }

    public void setPassUrl(String passUrl) {
        this.passUrl = passUrl;
    }

    /*	public String getArrivalTextColor() {
            return arrivalTextColor;
        }
        public void setArrivalTextColor(String arrivalTextColor) {
            this.arrivalTextColor = arrivalTextColor;
        }
        public String getArrivalTextFont() {
            return arrivalTextFont;
        }
        public void setArrivalTextFont(String arrivalTextFont) {
            this.arrivalTextFont = arrivalTextFont;
        }
        public String getArrivalText4Color() {
            return arrivalText4Color;
        }
        public void setArrivalText4Color(String arrivalText4Color) {
            this.arrivalText4Color = arrivalText4Color;
        }
        public String getArrivalText4Font() {
            return arrivalText4Font;
        }
        public void setArrivalText4Font(String arrivalText4Font) {
            this.arrivalText4Font = arrivalText4Font;
        }
        public String getArrivalText3Font() {
            return arrivalText3Font;
        }
        public void setArrivalText3Font(String arrivalText3Font) {
            this.arrivalText3Font = arrivalText3Font;
        }
        public String getArrivalText3Color() {
            return arrivalText3Color;
        }
        public void setArrivalText3Color(String arrivalText3Color) {
            this.arrivalText3Color = arrivalText3Color;
        }
        public String getArrivalText2Color() {
            return arrivalText2Color;
        }
        public void setArrivalText2Color(String arrivalText2Color) {
            this.arrivalText2Color = arrivalText2Color;
        }
        public String getArrivalText2Font() {
            return arrivalText2Font;
        }
        public void setArrivalText2Font(String arrivalText2Font) {
            this.arrivalText2Font = arrivalText2Font;
        }
        public String getArrivalText1Color() {
            return arrivalText1Color;
        }
        public void setArrivalText1Color(String arrivalText1Color) {
            this.arrivalText1Color = arrivalText1Color;
        }
        public String getArrivalText1Font() {
            return arrivalText1Font;
        }
        public void setArrivalText1Font(String arrivalText1Font) {
            this.arrivalText1Font = arrivalText1Font;
        }*/
    public String getQrcodes() {
        return qrcodes;
    }

    public void setQrcodes(String qrcodes) {
        this.qrcodes = qrcodes;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardExpirationDate() {
        return cardExpirationDate;
    }

    public void setCardExpirationDate(String cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

}