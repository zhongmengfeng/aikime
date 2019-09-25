package com.ichi2yiji.anki.activity.ek;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ek.EKDetailsAdapter;

/**
 * Created by ekar01 on 2017/7/7.
 */

public class EKDetailsActivity extends AppCompatActivity {
    private ListView my_etail_lst;
    private View content;
    private PopupWindow popupWindow;
    private TextView spinner_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_details);
        spinner_tv = (TextView) findViewById(R.id.spinner_tv);
        my_etail_lst = (ListView) findViewById(R.id.my_etail_lst);
        my_etail_lst.setAdapter(new EKDetailsAdapter(EKDetailsActivity.this));
        spinner_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSex();
            }
        });
    }
    private void changeSex() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        content = inflater.inflate(R.layout.ek_popupwindow_chosesex, null);
        getPopu();
        content.setFocusable(true);
        content.setFocusableInTouchMode(true);
        popupWindow = new PopupWindow(content, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置点击空白处消失
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        backgroundAlpha(EKDetailsActivity.this, 1f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(content, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setAnimationStyle(R.style.ek_anim_menu_bottombar);
        popupWindow.update();
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = EKDetailsActivity.this.getWindow().getAttributes();
        lp.alpha = 0.5f;
        EKDetailsActivity.this.getWindow().setAttributes(lp);
    }
    /**
     * Eventbus 传递事件 进行更新
     */
    private void getPopu() {
        LinearLayout tvNan = (LinearLayout) content.findViewById(R.id.edit_ll_pai);
        LinearLayout tvNv = (LinearLayout) content.findViewById(R.id.edit_ll_xuan);
        LinearLayout  tvCancle = (LinearLayout) content.findViewById(R.id.edit_ll_cancel);
        tvNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSex(1);
                popupWindow.dismiss();

            }
        });
        tvNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSex(0);
                popupWindow.dismiss();

            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
    /**
     * 更新
     *
     * @param state
     */
    private void updateSex(final int state) {


    }
}
