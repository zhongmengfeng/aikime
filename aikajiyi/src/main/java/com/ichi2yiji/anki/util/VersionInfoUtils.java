package com.ichi2yiji.anki.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;



/**
 * Created by Administrator on 2017/3/24.
 */

public class VersionInfoUtils {

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo("com.chaojiyiji.geometerplus.zlibrary.ui.android", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getVerCode", e.getMessage());
        }
        return verCode;
    }
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo("com.chaojiyiji.geometerplus.zlibrary.ui.android", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getVerName", e.getMessage());
        }
        return verName;
    }
}
