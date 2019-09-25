package com.ichi2yiji.common;

import com.google.gson.Gson;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.utils.SPUtil;

/**
 * Created by Administrator on 2017/4/25.
 */

public class SettingUtil {

    /**
     * 获取所有设置
     * @return
     */
    public static SettingsBean getSettings(){
        String settings = SPUtil.getPreferences(SPUtil.TYPE_SETTINGS_DATA, "Settings", "");
        Gson gson = new Gson();
        SettingsBean settingsBean = gson.fromJson(settings, SettingsBean.class);
        if (settingsBean == null) {
            // 默认设置
            settingsBean = new SettingsBean();
        }
        return settingsBean;
    }

    /**
     * 获取所有设置
     * @return
     */
    public static String getSettingsJson(){
        SettingsBean settings = getSettings();
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        return json;
    }

    /**
     * 更新设置
     * @param settingsBean
     */
    public static void upDateSettings(SettingsBean settingsBean){
        Gson gson = new Gson();
        String settings = gson.toJson(settingsBean);
        SPUtil.setPreferences(SPUtil.TYPE_SETTINGS_DATA,"Settings", settings);
    }


}
