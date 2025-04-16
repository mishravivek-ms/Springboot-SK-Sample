package com.sk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Items {
    @JsonProperty("Text") // Ensures the field is serialized as "Text"
    private String text; // Renamed to follow Java conventions

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}