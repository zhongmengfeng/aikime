package com.ichi2yiji.anki.util;

import android.app.Activity;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.common.SettingUtil;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ThemeChangeUtil {

    public static void changeTheme(Activity activity){

        SettingsBean settings = SettingUtil.getSettings();
        if (settings.getThemeIndex() == 0){
            activity.setTheme(R.style.DarkTheme);
        }else if(settings.getThemeIndex() == 1){
            activity.setTheme(R.style.BlueTheme);
        }
    }

    /**
     * 主题标志位
     * @return  0:灰色主题    1:蓝色主题
     */
    public static int getCurrentThemeName(){
        SettingsBean settings = SettingUtil.getSettings();
        return settings.getThemeIndex();
    }

    /**
     * 获取主题颜色
     * @param activity
     * @return  0 灰色 1 蓝色
     */
    public static int getCurrentThemeTag(Activity activity){
        SettingsBean settings = SettingUtil.getSettings();
        return settings.getThemeIndex();
    }
}
