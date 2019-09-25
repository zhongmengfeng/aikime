package com.ichi2yiji.anki.exception;

@SuppressWarnings("serial")
public class UnknownHttpResponseException extends Exception {
    private Integer mCode;
    public UnknownHttpResponseException(String message, Integer code) {
        super(message);
        mCode = code;
    }
    public int getResponseCode() {
        return mCode;
    }
}