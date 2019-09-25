package com.ichi2yiji.anki.bean;

/**
 * Created by ekar01 on 2017/6/13.
 */
public class DistributeEvent{

    public DistributeEvent(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
