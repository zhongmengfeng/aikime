package com.ankireader.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ViewShot {

    /**
     * 获取指定view的截图
     */
    public static Bitmap getShot(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        bitmap = bitmap.createBitmap(bitmap);  //拷贝图片，创建副本
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 获取整个屏幕的截图
     */
    public static Bitmap captureScreen(Activity context){
//        View view = context.getWindow().getDecorView();
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//        return  bitmap;

        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(b1);
        view.destroyDrawingCache();

        return  bitmap;
    }


    /**
     * 将获取到的view的截图以指定的文件名保存到指定路径下
     */
    public static void saveBitmap(String path, String name, Bitmap bitmap){
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String filename = name + ".png";
        File file = new File(dir, filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
