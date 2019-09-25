package com.ichi2yiji.anki.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.DownloadTests;
import com.ichi2yiji.anki.adapter.SectionedSpanSizeLookup;
import com.ichi2yiji.anki.adapter.TestsGroupAdapter;
import com.ichi2yiji.anki.bean.TestGroupBean;
import com.ichi2yiji.anki.dialogs.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 模考列表页面
 */
public class DeckTestFragment2 extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_SHARE_LIBRARY = 0;

    private TextView text_load_online_test;
    private TextView text_share_library;
    private RecyclerView rv_test_group;

    private List<TestGroupBean> dirTestsList;
    private TestsGroupAdapter mAdapter;

    public DeckTestFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadTestList();
        return inflater.inflate(R.layout.fragment_deck_test_new_scroll, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setListener();
        initData();
    }

    private void initView(View view) {
        text_load_online_test = (TextView) view.findViewById(R.id.text_load_online_test);
        text_share_library = (TextView) view.findViewById(R.id.text_share_library);
        rv_test_group = (RecyclerView) view.findViewById(R.id.rv_test_group);
    }
    private void setListener() {
        text_load_online_test.setOnClickListener(this);
        text_share_library.setOnClickListener(this);
    }
    private void initData() {
        mAdapter = new TestsGroupAdapter(getActivity(), dirTestsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new SectionedSpanSizeLookup(mAdapter,gridLayoutManager));
        rv_test_group.setLayoutManager(gridLayoutManager);
        rv_test_group.setAdapter(mAdapter);
    }


    private void loadTestList(){
        dirTestsList = getDirTests();
    }

    /**
     * 获取dirTests目录下所有文件名重新组装数据
     * @return  返回所有试题组
     */
    private List<TestGroupBean> getDirTests() {
        HashMap<String, List<Pair<String,String>>> dirTestsMap = new HashMap<>();
        List<Pair<String, String>> testsList;
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dirTests = new File(rootPath + "/Chaojiyiji/dirTests");
        if (!dirTests.exists()) {
            dirTests.mkdirs();
        }
        File[] files = dirTests.listFiles();
        if (files != null){
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".mtest")){
                    String[] split = file.getName().split("__");
                    testsList = dirTestsMap.get(split[0]);
                    if (testsList == null) {
                        testsList = new ArrayList<>();
                    }
                    testsList.add(new Pair<>(split[0].replace(".mtest",""), file.getAbsolutePath()));
                    dirTestsMap.put(split[0],testsList);
                }
            }
        }

        // 重新组合数据

        // 模考组集合
        List<TestGroupBean> testGroupBeanList = new ArrayList<>();
        // 模考一组数据
        TestGroupBean testGroupBean;
        // 模考一组数据中的试题集合
        List<TestGroupBean.TestBean> testBeanList;
        // 试题
        TestGroupBean.TestBean testBean;

        Set<Map.Entry<String, List<Pair<String, String>>>> entries = dirTestsMap.entrySet();
        for (Map.Entry<String, List<Pair<String, String>>> entry : entries) {
            testGroupBean = new TestGroupBean();
            testBeanList = new ArrayList<>();

            testGroupBean.setGroupName(entry.getKey());
            List<Pair<String, String>> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                testBean = new TestGroupBean.TestBean();
                testBean.setTypeName(entry.getKey());
                testBean.setTestName(value.get(i).first);
                testBean.setFilePath(value.get(i).second);
                testBean.setFullName(testBean.getTypeName() + "__" + testBean.getTestName());
                testBeanList.add(testBean);
            }
            testGroupBean.setTestList(testBeanList);
            testGroupBeanList.add(testGroupBean);
        }
        return testGroupBeanList;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.text_load_online_test:
                // 在线导入
                loadOnlineTest();
                break;
            case R.id.text_share_library:
                // 共享题库
                shareLibrary();
                break;
        }
    }

    /**
     * 共享题库点击事件
     */
    private void shareLibrary() {
        Intent intent = new Intent(getActivity(), DownloadTests.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, REQUEST_CODE_SHARE_LIBRARY);
        getActivity().overridePendingTransition(R.anim.in_bottom_to_top_translate_anim, 0);
    }

    /**
     * 在线导入点击事件
     */
    private void loadOnlineTest() {
        new AlertDialog(getActivity()).builder()
                .setTitle("没有待导入考题")
                .setMsg("请进入官方网站上传考题")
                .setCancelable(false)
                .setNegativeButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SHARE_LIBRARY && resultCode == Activity.RESULT_OK){
            // 共享题库页面下载完成后，回到共享题库列表页面，更新模考列表
            dirTestsList = getDirTests();
            mAdapter.setData(dirTestsList);
        }
    }
}
