package com.ankireader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;

/**
 * Created by Administrator on 2016/11/18.
 */

public class CardInfoActivity extends AppCompatActivity {

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardinfo);
        mTextView = (TextView) this.findViewById(R.id.textview);
        applyKitKatTranslucency();
        initState();//设置沉浸式状态栏

        Intent intent = getIntent();
        String word = intent.getStringExtra("TEXT");
        int mStartParagraphIndex = intent.getIntExtra("StartParagraphIndex", 0);
        int mStartElementIndex = intent.getIntExtra("StartElementIndex", 0);
        int mStartCharIndex = intent.getIntExtra("StartCharIndex", 0);
        int mEndParagraphIndex = intent.getIntExtra("EndParagraphIndex", 0);
        int mEndElementIndex = intent.getIntExtra("EndElementIndex", 0);
        int mEndCharIndex = intent.getIntExtra("EndCharIndex", 0);

        String sentence = intent.getStringExtra("sentence");
        int StartParagraphOfSentence =intent.getIntExtra("StartParagraphOfSentence", 0);
        int StartElementOfSentence =intent.getIntExtra("StartElementOfSentence", 0);
        int StartCharOfSentence =intent.getIntExtra("StartCharOfSentence", 0);
        int EndParagraphOfSentence =intent.getIntExtra("EndParagraphOfSentence", 0);
        int EndElementOfSentence =intent.getIntExtra("EndElementOfSentence", 0);
        int EndCharOfSentence =intent.getIntExtra("EndCharOfSentence", 0);



        mTextView.setText(word + "\n段下标: (" + mStartParagraphIndex+ "," + mEndParagraphIndex + ")"
                + "  词下标: (" + mStartElementIndex+ "," + mEndElementIndex + ")"
                + "  字符下标: (" + mStartCharIndex+ "," + mEndCharIndex + ")\n" + sentence +
                "\n段下标: (" + StartParagraphOfSentence+ "," + EndParagraphOfSentence + ")"
                + "  词下标: (" + StartElementOfSentence+ "," + EndElementOfSentence + ")"
                + "  字符下标: (" + StartCharOfSentence+ "," + EndCharOfSentence + ")");

    }

    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            mTintManager.setStatusBarTintResource(R.color.colorPrimaryDark);//通知栏所需颜色
        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initState() {
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
