package com.ichi2yiji.anki.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/29.
 */

public class Http {
    /**
     *
     * @param urlStr String urlStr="http://172.17.54.91:8080/download/1.mp3";
     * @param path  String path= Environment.getExternalStorageDirectory() + "/Chaojiyiji";
     * @param fileName  String fileName="2.mp3";
     */

    public static void httpURLConnection(final String urlStr, final String path, final String fileName){

        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream output=null;
                try {

                    URL url= new URL(urlStr);
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    //取得inputStream，并将流中的信息写入SDCard


                    File file=new File(path +"/"+ fileName);
                    InputStream input=conn.getInputStream();

                    if(file.exists()){
                        Log.e("httpURLConnection" , "file.exists()==true");
                        System.out.println("exits");
                        return;
                    }else{
                        String dir= path;
                        new File(dir).mkdir();//新建文件夹
                        file.createNewFile();//新建文件
                        output=new FileOutputStream(file);
                        //读取大文件
                        byte[] buffer=new byte[4*1024];
                        while(input.read(buffer)!=-1){
                            output.write(buffer);
                        }
                        output.flush();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{

                }
            }
        }).start();

    }

}


