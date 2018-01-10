/**
 * Copyright Venuetize 2017
 * <TextHolder>
 * created by Venuetize
 */
package com.venue.emkitproximity.holder;

import java.io.Serializable;

public class TextHolder implements Serializable {

    public String color;

    public String font;

    public String value;

    public String action_id;

    public String activity_id;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

}
