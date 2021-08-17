package com.consultmais.consultmais.BuilderFoms.model;

import com.consultmais.consultmais.BuilderFoms.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BFTextView extends BFView {

    @JsonProperty(Constants.JSON_KEY_TEXT)
    private String text;

    @JsonProperty(Constants.JSON_KEY_TEXT_SIZE)
    private int textSize;

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
