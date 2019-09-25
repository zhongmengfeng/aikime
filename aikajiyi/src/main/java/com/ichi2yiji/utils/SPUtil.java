package com.ichi2yiji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.ichi2yiji.anki.AnkiDroidApp;

import java.util.Map;
import java.util.Set;


/**
 * SharedPreferences 工具类
 * Created by Administrator on 2017/4/9.
 */

public class SPUtil {

    public final static String TYPE_DEFAULT_CONFIG = "default";
    public final static String TYPE_APP_CONFIG = "app_config";
    public final static String TYPE_WELCOME_CONFIG = "showWelcome";
    public final static String TYPE_DECK_LIST_DATA = "DeckListData";
    public final static String TYPE_SETTINGS_DATA = "Settings_Data";
    public final static int TYPE_THEME = 0;


    /**
     * 获取SharedPreferences
     * @param type
     * @return
     */
    private static SharedPreferences getPreferences(String type) {
        Context context = AnkiDroidApp.getContext();
        SharedPreferences preferences = null;
        if(TextUtils.equals(type, TYPE_DEFAULT_CONFIG)){
            // 默认配置文件
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }else if (TextUtils.equals(type,TYPE_WELCOME_CONFIG)) {
            // 用户配置信息
            preferences = context.getSharedPreferences(TYPE_WELCOME_CONFIG, Context.MODE_PRIVATE);
            return preferences;
        }else if (TextUtils.equals(type,TYPE_APP_CONFIG)) {
            // 软件配置信息
            preferences = context.getSharedPreferences(TYPE_APP_CONFIG,Context.MODE_PRIVATE);
            return preferences;
        }else if(TextUtils.equals(type,TYPE_DECK_LIST_DATA)){
            // 牌组
            preferences = context.getSharedPreferences(TYPE_DECK_LIST_DATA, Context.MODE_PRIVATE);
        }else if(TextUtils.equals(type,TYPE_SETTINGS_DATA)){
            // 设置
            preferences = context.getSharedPreferences(TYPE_SETTINGS_DATA, Context.MODE_PRIVATE);
        }
        return preferences;
    }


    /**
     * 保存String类型值
     * @param key
     * @param values
     */
    public static void setPreferences(String type,String key, String values) {
        SharedPreferences.Editor editor = getPreferences(type).edit();
        editor.putString(key, values);
        editor.commit();
    }

    /**
     * 获取String类型值
     * @param key
     * @param defaultValues
     * @return
     */
    public static String getPreferences(String type,String key,String defaultValues) {
        return getPreferences(type).getString(key, defaultValues);
    }


    /**
     * 保存int类型值
     * @param key
     * @param values
     */
    public static void setPreferences(String type,String key, int values) {
        SharedPreferences.Editor editor = getPreferences(type).edit();
        editor.putInt(key, values);
        editor.commit();
    }

    /**
     * 获取int类型值
     * @param key
     * @param defaultValues
     * @return
     */
    public static int getPreferences(String type,String key, int defaultValues) {
        return getPreferences(type).getInt(key, defaultValues);
    }

    /**
     * 保存long类型值
     * @param key
     * @param values
     */
    public static void setPreferences(String type,String key, long values) {
        SharedPreferences.Editor editor = getPreferences(type).edit();
        editor.putLong(key, values);
        editor.commit();
    }

    /**
     * 获取long类型值
     * @param key
     * @param defaultValues
     * @return
     */
    public static long getPreferences(String type,String key, long defaultValues) {
        return getPreferences(type).getLong(key, defaultValues);
    }


    /**
     * 保存boolean类型值
     * @param key
     * @param values
     */
    public static void setPreferences(String type,String key, boolean values) {
        SharedPreferences.Editor editor = getPreferences(type).edit();
        editor.putBoolean(key, values);
        editor.commit();
    }

    /**
     * 获取boolean类型值
     */
    public static boolean getPreferences(String type,String key, boolean defaultValues) {
        return getPreferences(type).getBoolean(key, defaultValues);
    }

    /**
     * 是否包含此键
     * @param type
     * @param key
     * @return
     */
    public static boolean isContainKey(String type,String key) {
        if (getPreferences(type).contains(key)) {
            return true;
        }
        return false;
    }

    public static void setStringset(String type, String key, Set<String> set){
        SharedPreferences.Editor editor = getPreferences(type).edit();
        editor.putStringSet(key,set);
        editor.commit();
    }


    public static void remove(String type,String key){
        getPreferences(type).edit().remove(key).commit();
    }

    public static void clear(String type){
        getPreferences(type).edit().clear().commit();
    }


    public static Map<String, ?> getAll(String type){
        Map<String, ?> all = getPreferences(type).getAll();
        return all;
    }



























}
