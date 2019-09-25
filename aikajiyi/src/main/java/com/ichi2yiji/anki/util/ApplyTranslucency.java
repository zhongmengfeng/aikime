package com.ichi2yiji.anki.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chaojiyiji.yiji.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by 金鹏 on 2016/12/19.
 */

public class ApplyTranslucency {

    public static  void applyKitKatTranslucency(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // android 4.4 版本以上进入该方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // android 5.0 版本以上进入该方法
                initState(activity);
            }else {
                // android 5.0 版本以下进入该方法
                setTranslucentStatus(activity, true);
                SystemBarTintManager mTintManager = new SystemBarTintManager(activity);
                mTintManager.setStatusBarTintEnabled(true);

//                mTintManager.setStatusBarTintResource(R.color.material_top_blue);//通知栏所需颜色
//                mTintManager.setStatusBarTintResource(R.color.aika_theme);//通知栏所需颜色

                // 获取自定义属性值
                int[] attrs = new int[]{R.attr.layout_background};
                TypedArray typedArray = activity.obtainStyledAttributes(attrs);
                int backgroundResource = typedArray.getResourceId(0, 0);
                typedArray.recycle();
                // 设置通知栏背景颜色
                mTintManager.setStatusBarTintResource(backgroundResource);
            }
        }


    }

    public static  void applyKitKatTranslucency2(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                initState2(activity);
            }else {
                setTranslucentStatus(activity, true);
                SystemBarTintManager mTintManager = new SystemBarTintManager(activity);
                mTintManager.setStatusBarTintEnabled(true);

                mTintManager.setStatusBarTintResource(R.color.mmmmmmmmmmmmmmmmm);//通知栏所需颜色
            }
        }

    }

    @TargetApi(19)  //Android4.4
    public static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  //Android5.0
    public static void initState2(Activity activity) {
        Window window = activity.getWindow();
        //window.setStatusBarColor(Color.TRANSPARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.mmmmmmmmmmmmmmmmm));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  //Android5.0
    private static void initState(Activity activity) {
        Window window = activity.getWindow();
        //window.setStatusBarColor(Color.TRANSPARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(activity.getResources().getColor(R.color.material_top_blue));
//        window.setStatusBarColor(activity.getResources().getColor(R.color.aika_theme));


        //获取attr的int值
        int defaultColor = 0xFF000000;
        int[] attrsArray = { R.attr.layout_background };
        TypedArray typedArray = activity.obtainStyledAttributes(attrsArray);
        int accentColor = typedArray.getColor(0, defaultColor);
        // don't forget the resource recycling
        typedArray.recycle();

        window.setStatusBarColor(accentColor);//设置状态栏颜色


//        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
