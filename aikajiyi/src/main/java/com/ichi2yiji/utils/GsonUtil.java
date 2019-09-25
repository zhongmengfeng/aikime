package com.ichi2yiji.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ichi2yiji.anki.bean.AttentBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class GsonUtil {


    /**
     * 返回实例
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, clazz);
        return t;
    }

    public static String createJson(List list) {
        Gson gson = new Gson();
        JsonObject jsonobject = new JsonObject();
        JsonArray array = new JsonArray();
        Iterator listIter = list.listIterator();
        if (listIter.hasNext()) {
            Object obj = listIter.next();
            jsonobject = (JsonObject) gson.toJsonTree(obj);
            array.add(jsonobject);
        }
        return array.toString();
    }

    public static List<AttentBean.DataBean> JsonToList(String json) {
        Gson gson = new Gson();
        List<AttentBean.DataBean> jsonlist = gson.fromJson(json, new TypeToken<List<AttentBean.DataBean>>() {}.getType());
        return jsonlist;
    }

}
