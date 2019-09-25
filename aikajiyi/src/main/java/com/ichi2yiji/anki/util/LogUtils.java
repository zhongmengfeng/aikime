package com.ichi2yiji.anki.util;

import android.util.Log;

/**
 * Created by ekar01 on 2017/6/27.
 */

public class LogUtils {
    /**
     * 分段打印出较长log文本
     * @param log        原log文本
     * @param showCount  规定每段显示的长度（最好不要超过eclipse限制长度）
     */
    public static void showLogCompletion(String log,int showCount){
        if(log.length() >showCount){
            String show = log.substring(0, showCount);
//          System.out.println(show);
            Log.i("TAG", show+"");
            if((log.length() - showCount)>showCount){//剩下的文本还是大于规定长度
                String partLog = log.substring(showCount,log.length());
                showLogCompletion(partLog, showCount);
            }else{
                String surplusLog = log.substring(showCount, log.length());
//              System.out.println(surplusLog);
                Log.i("TAG", surplusLog+"");
            }

        }else{
//          System.out.println(log);
            Log.i("TAG", log+"");
        }
    }


    /**
     * 分段打印出较长log文本
     * @param log        原log文本
     * @param showCount  规定每段显示的长度（最好不要超过eclipse限制长度）
     */
    public static void showLogCompletion(String TAG, String log,int showCount){
        if(log.length() >showCount){
            String show = log.substring(0, showCount);
//          System.out.println(show);
            Log.i(TAG, show+"");
            if((log.length() - showCount)>showCount){//剩下的文本还是大于规定长度
                String partLog = log.substring(showCount,log.length());
                showLogCompletion(TAG, partLog, showCount);
            }else{
                String surplusLog = log.substring(showCount, log.length());
//              System.out.println(surplusLog);
                Log.i(TAG, surplusLog+"");
            }

        }else{
//          System.out.println(log);
            Log.i(TAG, log+"");
        }
    }
}
