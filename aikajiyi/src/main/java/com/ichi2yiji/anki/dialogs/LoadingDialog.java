package com.ichi2yiji.anki.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;

import com.chaojiyiji.yiji.R;

import org.xutils.common.util.LogUtil;

/**
 * Created by Administrator on 2017/5/12.
 */

public class LoadingDialog extends Dialog {


    private AnimationDrawable animationDrawable;
    private ImageView ivLoading;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog_loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        } else {
            LogUtil.e("onCreate: window == null");
        }
        setContentView(R.layout.dialog_loading);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        animationDrawable = (AnimationDrawable) ivLoading.getDrawable();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }

    @Override
    public void show() {
        super.show();
        ivLoading.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
    }
}




















