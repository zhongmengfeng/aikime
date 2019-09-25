package com.ichi2yiji.anki.bean;

/**
 * Created by ekar01 on 2017/6/12.
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
