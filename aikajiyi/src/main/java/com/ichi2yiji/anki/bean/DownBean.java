package com.ichi2yiji.anki.bean;

/**
 * Created by ekar01 on 2017/5/24.
 */

public class DownBean {
    private int code;
    private String data;

    @Override
    public String   toString() {
        return "DownBean{" +
                "code=" + code +
                ", data='" + data + '\'' +
                '}';
    }

    public DownBean(int code, String data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
