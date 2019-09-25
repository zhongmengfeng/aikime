package com.ichi2yiji.anki.activity.ek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ek.EKSimpleTreeAdapter;
import com.ichi2yiji.anki.treeview.Bean;
import com.ichi2yiji.anki.treeview.FileBean;
import com.ichi2yiji.anki.treeview.Node;
import com.ichi2yiji.anki.treeview.TreeListViewAdapter;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekar01 on 2017/7/6.
 */

public class EKSelectChapterActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rl_ek_activity_selectchapter_back;
    private ListView lv_ek_activity_selectchapter_listview;

    private List<Bean> mDatas = new ArrayList<Bean>();
    private List<FileBean> mDatas2 = new ArrayList<FileBean>();
    private TreeListViewAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ek_activity_selectchapter);

        ApplyTranslucency.applyKitKatTranslucency(this);

        initViews();

        initDatas();

        try {
            mAdapter = new EKSimpleTreeAdapter<FileBean>(lv_ek_activity_selectchapter_listview, this, mDatas2, 10);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Node node, int position) {
                    if (node.isLeaf()) {
                        Toast.makeText(getApplicationContext(), node.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        lv_ek_activity_selectchapter_listview.setAdapter(mAdapter);

    }

    private void initViews() {
        rl_ek_activity_selectchapter_back = (RelativeLayout) findViewById(R.id.rl_ek_activity_selectchapter_back);
        lv_ek_activity_selectchapter_listview = (ListView) findViewById(R.id.lv_ek_activity_selectchapter_listview);

        rl_ek_activity_selectchapter_back.setOnClickListener(this);
    }



    private void initDatas() {

        requestData();

        mDatas.add(new Bean(1, 0, "根目录1"));
        mDatas.add(new Bean(2, 0, "根目录2"));
        mDatas.add(new Bean(3, 0, "根目录3"));
        mDatas.add(new Bean(4, 0, "根目录4"));
        mDatas.add(new Bean(5, 1, "子目录1-1"));
        mDatas.add(new Bean(6, 1, "子目录1-2"));

        mDatas.add(new Bean(7, 5, "子目录1-1-1"));
        mDatas.add(new Bean(8, 2, "子目录2-1"));

        mDatas.add(new Bean(9, 4, "子目录4-1"));
        mDatas.add(new Bean(10, 4, "子目录4-2"));

        mDatas.add(new Bean(11, 10, "子目录4-2-1"));
        mDatas.add(new Bean(12, 10, "子目录4-2-3"));
        mDatas.add(new Bean(13, 10, "子目录4-2-2"));
        mDatas.add(new Bean(14, 9, "子目录4-1-1"));
        mDatas.add(new Bean(15, 9, "子目录4-1-2"));
        mDatas.add(new Bean(16, 9, "子目录4-1-3"));

        mDatas2.add(new FileBean(1, 0, "刑事诉讼法"));
        mDatas2.add(new FileBean(2, 0, "刑法"));
        mDatas2.add(new FileBean(3, 2, "刑法第一章节"));
        mDatas2.add(new FileBean(4, 3, "刑法第一章节第一小节"));
        mDatas2.add(new FileBean(5, 3, "刑法第一章节第二小节"));
        mDatas2.add(new FileBean(6, 2, "刑法第二章节"));
        mDatas2.add(new FileBean(7, 2, "刑法第三章节"));
        mDatas2.add(new FileBean(8, 2, "刑法第四章节"));

        mDatas2.add(new FileBean(9, 0, "民法"));
        mDatas2.add(new FileBean(10, 0, "商法"));
        mDatas2.add(new FileBean(11, 0, "商法"));

//        mDatas.add(new Bean(1, 0, "根目录1"));
//        mDatas.add(new Bean(2, 0, "根目录2"));
//        mDatas.add(new Bean(3, 0, "根目录3"));
//        mDatas.add(new Bean(4, 0, "根目录4"));
//        mDatas.add(new Bean(5, 1, "子目录1-1"));
//        mDatas.add(new Bean(6, 1, "子目录1-2"));
//
//        mDatas.add(new Bean(7, 5, "子目录1-1-1"));
//        mDatas.add(new Bean(8, 2, "子目录2-1"));
//
//        mDatas.add(new Bean(9, 4, "子目录4-1"));
//        mDatas.add(new Bean(10, 4, "子目录4-2"));
//
//        mDatas.add(new Bean(11, 10, "子目录4-2-1"));
//        mDatas.add(new Bean(12, 10, "子目录4-2-3"));
//        mDatas.add(new Bean(13, 10, "子目录4-2-2"));
//        mDatas.add(new Bean(14, 9, "子目录4-1-1"));
//        mDatas.add(new Bean(15, 9, "子目录4-1-2"));
//        mDatas.add(new Bean(16, 9, "子目录4-1-3"));
//
//        mDatas2.add(new FileBean(1, 0, "文件管理系统"));
//        mDatas2.add(new FileBean(2, 1, "游戏"));
//        mDatas2.add(new FileBean(3, 1, "文档"));
//        mDatas2.add(new FileBean(4, 1, "程序"));
//        mDatas2.add(new FileBean(5, 2, "war3"));
//        mDatas2.add(new FileBean(6, 2, "刀塔传奇"));
//
//        mDatas2.add(new FileBean(7, 4, "面向对象"));
//        mDatas2.add(new FileBean(8, 4, "非面向对象"));
//
//        mDatas2.add(new FileBean(9, 7, "C++"));
//        mDatas2.add(new FileBean(10, 7, "JAVA"));
//        mDatas2.add(new FileBean(11, 7, "Javascript"));
//        mDatas2.add(new FileBean(12, 8, "C"));

    }

    private void requestData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_ek_activity_selectchapter_back:
                finish();
                break;
        }
    }
}
