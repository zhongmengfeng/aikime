package com.ankireader;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2016/11/18.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance(){
        return  instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
