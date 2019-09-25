package com.ichi2yiji.anki.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.view.RoundProgressBar;


public class ProgressDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private LinearLayout ll_progress;
    private RoundProgressBar rpb_progressbar;
    private TextView tv_download_tip;
    private ImageView ivBigLoading;
    private ImageView ivSmallLoading;
    private Display display;

    private RotateAnimation mRotateAnimation;
    private boolean clickable;


    public ProgressDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ProgressDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_progressdialog, null);

        initViews(view);
        init();

//		// 调整dialog背景大小
//		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
//				.getWidth() * 0.75 ), LinearLayout.LayoutParams.MATCH_PARENT));

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);//不允许在外面点击取消
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {//不允许点击返回键取消
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });

        return this;
    }

    private void initViews(View view) {
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        ll_progress = (LinearLayout) view.findViewById(R.id.ll_progress);
        rpb_progressbar = (RoundProgressBar) view.findViewById(R.id.rpb_progressbar);
        tv_download_tip = (TextView) view.findViewById(R.id.tv_download_tip);

        ivBigLoading = (ImageView) view.findViewById(R.id.ivBigLoading);
        ivSmallLoading = (ImageView) view.findViewById(R.id.ivSmallLoading);

    }

    private void init() {
        mRotateAnimation = new RotateAnimation(0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(1000L);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatCount(-1);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }

    public void setProgress(int progress) {
        rpb_progressbar.setProgress(progress);
    }

    public ProgressDialog setDownloadTip(String title) {
        tv_download_tip.setText(title);
        return this;
    }

    public String getDownLoadTip(){
        return tv_download_tip.getText().toString().trim();
    }

    public ProgressDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

	/*public void showWithStatus(String string) {
        if (string == null) {
			show();
			return;
		}
		showBaseStatus(resBigLoading, string);
		//开启旋转动画
		ivSmallLoading.startAnimation(mRotateAnimation);
	}*/

	/*public void show() {
		clearAnimations();
		ivBigLoading.setImageResource(resBigLoading);
		ivBigLoading.setVisibility(View.VISIBLE);
		ivSmallLoading.setVisibility(View.GONE);
		rpb_progressbar.setVisibility(View.GONE);
		//开启旋转动画
		ivBigLoading.startAnimation(mRotateAnimation);
		dialog.show();
	}*/

	/*public void showBaseStatus(int res, String string) {
		clearAnimations();
		ivSmallLoading.setImageResource(res);
		ivBigLoading.setVisibility(View.GONE);
		rpb_progressbar.setVisibility(View.GONE);
		ivSmallLoading.setVisibility(View.VISIBLE);
		dialog.show();
	}*/

    public void show() {
//        if(isShowing())return;
        clearAnimations();
        ivBigLoading.setVisibility(View.GONE);
        ivSmallLoading.setVisibility(View.VISIBLE);
        rpb_progressbar.setVisibility(View.GONE);
        ivSmallLoading.setImageResource(R.drawable.ic_progress_status_loading);
        ivSmallLoading.startAnimation(mRotateAnimation);
        dialog.show();
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public void showWithProgress() {
//        if(isShowing())return;
        clearAnimations();
        ivBigLoading.setVisibility(View.GONE);
        ivSmallLoading.setVisibility(View.GONE);
        rpb_progressbar.setVisibility(View.VISIBLE);
        dialog.show();
    }

    public void showErrorWithStatus() {
//        if(isShowing())return;
        clearAnimations();
        ivBigLoading.setVisibility(View.GONE);
        ivSmallLoading.setVisibility(View.VISIBLE);
        rpb_progressbar.setVisibility(View.GONE);
        ivSmallLoading.setImageResource(R.drawable.ic_progress_status_error);
        dialog.show();
    }

    public void showSuccessWithStatus() {
//        if(isShowing())return;
        clearAnimations();
        ivBigLoading.setVisibility(View.GONE);
        ivSmallLoading.setVisibility(View.VISIBLE);
        rpb_progressbar.setVisibility(View.GONE);
        ivSmallLoading.setImageResource(R.drawable.ic_progress_status_success);
        dialog.show();
    }

    public void dismiss() {
        clearAnimations();
        dialog.dismiss();
    }


    private void clearAnimations() {
        ivBigLoading.clearAnimation();
        ivSmallLoading.clearAnimation();
    }

    public void hide(){
        dialog.hide();
    }

}
