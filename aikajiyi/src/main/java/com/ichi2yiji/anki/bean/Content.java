package com.ichi2yiji.anki.bean;

/**
 * Created by ekar01 on 2017/5/19.
 */

public class Content {
    private String Name;
    private String desc;
    private String xia;
    private String time;

    public Content() {
    }

    @Override
    public String toString() {
        return "Content{" +
                "Name='" + Name + '\'' +
                ", desc='" + desc + '\'' +
                ", xia='" + xia + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Content(String name, String desc, String xia, String time) {
        Name = name;
        this.desc = desc;
        this.xia = xia;
        this.time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getXia() {
        return xia;
    }

    public void setXia(String xia) {
        this.xia = xia;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
