package com.ichi2yiji.anki;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */

public class FilteDeckFile {
    static String fileName = "FilteDeckFile";
    static FileOutputStream outputStream = null;
    static Context context;
    static List<String> names = null;

    public FilteDeckFile(Context c){
        this.context = c;
        try {
            outputStream = c.openFileOutput(fileName, Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save(String name) {
        try {
            outputStream.write(name.toString().getBytes("UTF-8"));
            outputStream.close();
            Log.e("outputStream","outputStream.close()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read() {
        byte[] bytes = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            bytes = new byte[1024*16];
            fis.read(bytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes != null ? bytes : new byte[0]);
    }

}
