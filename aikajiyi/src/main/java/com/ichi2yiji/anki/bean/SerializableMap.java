package com.ichi2yiji.anki.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ekar01 on 2017/6/28.
 */

public class SerializableMap implements Serializable{

    private static final long serialVersionUID = 1L;

    private Map<Long, String> map;

    public Map<Long, String> getMap() {
        return map;
    }

    public void setMap(Map<Long, String> map) {
        this.map = map;
    }
}
