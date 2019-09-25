package com.ichi2yiji.anki.activity.ek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ek.EKGalleryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKClassDetailsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private EKGalleryAdapter mAdapter;
    private List<Integer> mDatas;
    private ImageView ivback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_classdetails);
        initDatas();
        ivback = (ImageView) findViewById(R.id.iv_ek_activity_selectchapter_back);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EKClassDetailsActivity.this, EKFineClassActivity.class));
            }
        });
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        //设置适配器
        mAdapter = new EKGalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList(R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
    }
}
