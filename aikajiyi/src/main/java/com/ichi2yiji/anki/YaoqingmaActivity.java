package com.ichi2yiji.anki;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.fragment.YaoqingmaFragment;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;


/**
 * Created by ekar01 on 2017/5/31.
 */

public class YaoqingmaActivity extends FragmentActivity {

    private ImageView iv_yaoqingma_back;
    private FrameLayout fl_content;
    private RelativeLayout rl_yaoqingma_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaoqingma);
        ApplyTranslucency.applyKitKatTranslucency(this);

        String mem_id = getIntent().getStringExtra("mem_id");
        iv_yaoqingma_back = (ImageView) findViewById(R.id.iv_yaoqingma_back);
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        rl_yaoqingma_back = (RelativeLayout) findViewById(R.id.rl_yaoqingma_back);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, YaoqingmaFragment.newInstance(mem_id)).commit();

        rl_yaoqingma_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
