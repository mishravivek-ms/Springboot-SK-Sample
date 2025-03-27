package com.sk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequest {

    private String message;

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
