package com.ankireader;


import android.content.Context;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Administrator on 2016/10/28.
 */

public class CopyRawtodata {


    /**
     * 从raw中读取txt
     * @param inputPath raw 文件的资源路径（如R.raw.ian）
     * @param outputPath 输出的文件路径
     * @param outputName 输出的自定义文件名
     */
    public static  void readFromRaw(Context context, int inputPath, String outputPath, String outputName) {
        try {
            InputStream is = context.getResources().openRawResource(inputPath);//此处传入所需读取的Raw文件夹下的文件名

            String text = readTextFromRaw(is);
            save(text, outputPath, outputName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static String readTextFromRaw(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * 将输入流获得的字符串data写入输出流，保存为相应的文件
     * @param data 输入流数据
     * @param outputPath 文件保存的路径
     * @param outputName 文件名
     */
    private static void save(String data, String outputPath, String outputName){
        FileOutputStream out = null;
        BufferedWriter write = null;
        File file = new File(outputPath + "/" + outputName);

        try {
            out = new FileOutputStream(file);
            write = new BufferedWriter(new OutputStreamWriter(out));
            write.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(write != null){
                    write.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    //保存到data/data/包名/files/目录下的写入方法
//    private static void save(Context context, String data, String outputName){
//        FileOutputStream out = null;
//        BufferedWriter write = null;
//
//        try {
//            out = context.openFileOutput(outputName, Context.MODE_PRIVATE);//此处设置保存到内部data/data/包名/files/目录下的文件名
//            write = new BufferedWriter(new OutputStreamWriter(out));
//            write.write(data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try{
//                if(write != null){
//                    write.close();
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
}
