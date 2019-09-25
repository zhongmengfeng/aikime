package com.ichi2yiji.anki.activity.ek;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;

/**
 * Created by ekar01 on 2017/7/6.
 */

public class EKPrepareExamActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_ek_activity_prepareexam_back;
    private TextView tv_ek_activity_prepareexam_classshare;
    private RadioGroup rg_ek_activity_prepareexam_radiogroup;
    private RadioButton rb_ek_activity_prepareexam_radiobutton1;
    private RadioButton rb_ek_activity_prepareexam_radiobutton2;
    private RadioButton rb_ek_activity_prepareexam_radiobutton3;
    private RadioButton rb_ek_activity_prepareexam_radiobutton4;
    private RelativeLayout rl_ek_activity_prepareexam_confirm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_prepareexam);

        ApplyTranslucency.applyKitKatTranslucency(this);

        initViews();

        initDatas();


    }

    private void initViews() {
        rl_ek_activity_prepareexam_back = (RelativeLayout) findViewById(R.id.rl_ek_activity_prepareexam_back);
        tv_ek_activity_prepareexam_classshare = (TextView) findViewById(R.id.tv_ek_activity_prepareexam_classshare);
        rg_ek_activity_prepareexam_radiogroup = (RadioGroup) findViewById(R.id.rg_ek_activity_prepareexam_radiogroup);
        rb_ek_activity_prepareexam_radiobutton1 = (RadioButton) findViewById(R.id.rb_ek_activity_prepareexam_radiobutton1);
        rb_ek_activity_prepareexam_radiobutton2 = (RadioButton) findViewById(R.id.rb_ek_activity_prepareexam_radiobutton2);
        rb_ek_activity_prepareexam_radiobutton3 = (RadioButton) findViewById(R.id.rb_ek_activity_prepareexam_radiobutton3);
        rb_ek_activity_prepareexam_radiobutton4 = (RadioButton) findViewById(R.id.rb_ek_activity_prepareexam_radiobutton4);
        rl_ek_activity_prepareexam_confirm = (RelativeLayout) findViewById(R.id.rl_ek_activity_prepareexam_confirm);

        tv_ek_activity_prepareexam_classshare.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        rl_ek_activity_prepareexam_back.setOnClickListener(this);
        tv_ek_activity_prepareexam_classshare.setOnClickListener(this);
        rl_ek_activity_prepareexam_confirm.setOnClickListener(this);
        rg_ek_activity_prepareexam_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ek_activity_prepareexam_radiobutton1:

                        break;
                    case R.id.rb_ek_activity_prepareexam_radiobutton2:
                        break;
                    case R.id.rb_ek_activity_prepareexam_radiobutton3:
                        break;
                    case R.id.rb_ek_activity_prepareexam_radiobutton4:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initDatas() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_ek_activity_prepareexam_back:
                break;
            case R.id.tv_ek_activity_prepareexam_classshare:
                break;
            case R.id.rl_ek_activity_prepareexam_confirm:
                break;
            default:
                break;
        }
    }
}
