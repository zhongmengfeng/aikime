package com.ichi2yiji.anki.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ichi2yiji.anki.DictionaryPage;

/**
 * Created by Administrator on 2016/12/28.
 */

public class TextInfoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TextInfoReceiver", ">>>>>>>>>>onReceived!");
       /* String text = intent.getStringExtra("TEXT");
        String sentence = intent.getStringExtra("sentence");
        String bookname = intent.getStringExtra("BOOK_NAME");
        Intent i = new Intent(context, DictionaryPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("TEXT", text);
        i.putExtra("sentence", sentence);
        i.putExtra("BOOK_NAME", bookname);
        context.startActivity(i);*/

        intent.setClass(context, DictionaryPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
