package com.ichi2yiji.anki.activity.ek;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ek.EKExpertClassAdapter;
import com.ichi2yiji.anki.view.ek.CircleImageView;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKExpertClassActivity extends AppCompatActivity {

    private ImageView iv_ek_activity_expertclass_clear;
    private CircleImageView civ_ek_activity_expertclass_icon;
    private TextView tv_ek_activity_expertclass_name;
    private TextView tv_ek_activity_expertclass_level;
    private TextView tv_ek_activity_expertclass_focus;
    private TextView tv_ek_activity_expertclass_amount;

    private ListView lv_ek_activity_expertclass_listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTranlucency();
//        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ek_activity_expertclass);
//        ApplyTranslucency.applyKitKatTranslucency(this);

        initView();
    }

    private void initView() {
        iv_ek_activity_expertclass_clear = (ImageView) findViewById(R.id.iv_ek_activity_expertclass_clear);
        civ_ek_activity_expertclass_icon = (CircleImageView) findViewById(R.id.civ_ek_activity_expertclass_icon);
        tv_ek_activity_expertclass_name = (TextView) findViewById(R.id.tv_ek_activity_expertclass_name);
        tv_ek_activity_expertclass_level = (TextView) findViewById(R.id.tv_ek_activity_expertclass_level);
        tv_ek_activity_expertclass_focus = (TextView) findViewById(R.id.tv_ek_activity_expertclass_focus);
        tv_ek_activity_expertclass_amount = (TextView) findViewById(R.id.tv_ek_activity_expertclass_amount);
        lv_ek_activity_expertclass_listview = (ListView) findViewById(R.id.lv_ek_activity_expertclass_listview);


        EKExpertClassAdapter ekExpertClassAdapter = new EKExpertClassAdapter(this);
        lv_ek_activity_expertclass_listview.setAdapter(ekExpertClassAdapter);
    }

    public void setTranlucency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
