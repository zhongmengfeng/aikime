package com.ichi2yiji.anki;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.TextListAdapter;
import com.ichi2yiji.anki.bean.DistributeBean;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by ekar01 on 2017/6/13.
 */
public class SelectClassActivity extends Activity {

    public final static String TAG = "SelectClassActivity";
    private TextView tv_select_class;
    private ListView lv_select_class;
    private RelativeLayout rl_select_class_back;
    private List<String> data;
    private int type;
    private int classPosition;
    private int coursewarePosition;
    private TextListAdapter textListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错
        Timber.d("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_class);
        ApplyTranslucency.applyKitKatTranslucency(this);
        data = new ArrayList<>();
        initView();
        getIntentData();
    }

    private void getIntentData() {

        type = getIntent().getIntExtra("type", 0);
        DistributeBean.DataBean dataBean = (DistributeBean.DataBean) getIntent().getSerializableExtra("data");

        if (type == 1) {
            Log.e(TAG, "type: " + type);
            classPosition = getIntent().getIntExtra("classPosition", 0);
            tv_select_class.setText("选择班级");
            List<DistributeBean.DataBean.ClassInfoBean> classInfoBeanList = dataBean.getClassInfo();
            for (DistributeBean.DataBean.ClassInfoBean classInfoBean : classInfoBeanList) {
                String class_name = classInfoBean.getClass_name();
                data.add(class_name);
                textListAdapter.setData(data,type,classPosition);
            }

        } else if (type == 2){
            Log.e(TAG, "type: " + type);
            coursewarePosition = getIntent().getIntExtra("coursewarePosition", 0);
            tv_select_class.setText("选择课件");
            List<DistributeBean.DataBean.DecksBean> decks = dataBean.getDecks();
            for (DistributeBean.DataBean.DecksBean deck : decks) {
                String goods_name = deck.getGoods_name();
                data.add(goods_name);
                textListAdapter.setData(data,type,coursewarePosition);
            }
        }
    }

    private void initView() {
        tv_select_class = (TextView) findViewById(R.id.tv_select_class);
        rl_select_class_back = (RelativeLayout) findViewById(R.id.rl_select_class_back);
        lv_select_class = (ListView) findViewById(R.id.lv_select_class);

        textListAdapter = new TextListAdapter(this);
        lv_select_class.setAdapter(textListAdapter);

        rl_select_class_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
