package com.ichi2yiji.anki.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ankireader.CopyRawtodata;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.DeckTestReal;
import com.ichi2yiji.anki.DownloadTests;
import com.ichi2yiji.anki.dialogs.AlertDialog;
import com.ichi2yiji.anki.widgets.DeckTestGridViewAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class DeckTestFragment2_copy extends Fragment {
    private ListView listView;
    private TextView text_load_online_test;
    private TextView text_share_library;
    private List<String[]> list;
    private ArrayList<String[]> data_original;
    private ArrayList<String[]> data;
    private DeckTestGridViewAdapter deckTestGridViewAdapter;

    private TextView more;
    private TextView test_course_name;
    private TextView detail_lyt;
    private GridView gridView;
    private boolean isGridViewShowAllItems;

    private ArrayList<String[]> group_data;
    // private LinearLayout linear_lyt_in_scroll_view;

    private boolean hasJiazhao;
    private boolean hasYixue;
    private boolean hasZaojiashi;

    public DeckTestFragment2_copy() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadTestList();
        initGroupData();
        initGridViewOriginalData();
        return inflater.inflate(R.layout.fragment_deck_test_new_scroll, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        listView = (ListView)view.findViewById(R.id.decktest_listview);
        text_load_online_test = (TextView) view.findViewById(R.id.text_load_online_test);
        text_share_library = (TextView) view.findViewById(R.id.text_share_library);


        text_load_online_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        text_share_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DownloadTests.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_bottom_to_top_translate_anim, 0);
            }
        });

        /*linear_lyt_in_scroll_view = (LinearLayout) view.findViewById(R.id.linear_lyt_in_scroll_view);
        for(int i = 0; i <list.size();i++){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.fragment_deck_test_dyn_view,null);
            linear_lyt_in_scroll_view.addView(v, i);

            more = (TextView) v.findViewById(R.id.more);
            test_course_name = (TextView) v.findViewById(R.id.test_course_name);
            detail_lyt = (TextView) v.findViewById(R.id.detail_lyt);
            gridView = (GridView) v.findViewById(R.id.grid_view_test);


            deckTestGridViewAdapter = new DeckTestGridViewAdapter(getActivity(), data_original);
            gridView.setAdapter(deckTestGridViewAdapter);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DeckTestReal.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    String json = "";
                    try {
                        FileInputStream inputStream = new FileInputStream(
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirTests/" + "中医助理医师考试模拟试题.mtest");
                        json = CopyRawtodata.readTextFromRaw(inputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("testjson",json);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                }
            });

        }

        View childview = linear_lyt_in_scroll_view.getChildAt(0);*/

    }


    private void initGridViewOriginalData(){
        data_original = new ArrayList<>();
        String[] item1 = new String[]{"科目一", "题库001"};
        String[] item2 = new String[]{"科目一", "题库002"};
        String[] item3 = new String[]{"科目一", "题库003"};
        String[] item4 = new String[]{"科目一", "题库004"};
        String[] item5 = new String[]{"科目一", "题库005"};
        String[] item6 = new String[]{"科目一", "题库006"};
        String[] item7 = new String[]{"科目一", "题库007"};
        String[] item8 = new String[]{"科目一", "题库008"};
        String[] item9 = new String[]{"科目一", "题库009"};
        String[] item10 = new String[]{"科目一", "题库010"};
        data_original.add(item1);
        data_original.add(item2);
        data_original.add(item3);
        data_original.add(item4);
        data_original.add(item5);
        data_original.add(item6);
        data_original.add(item7);
        data_original.add(item8);
        data_original.add(item9);
        data_original.add(item10);
    }

    private void initGroupData(){
        group_data = new ArrayList<>();

        String[] item1 = new String[]{"驾照考试", "内含200道经典题目，是2016年新版题库"};
        String[] item2 = new String[]{"医学考试", "内含医学考试题目，是2017年新版题库"};
        String[] item3 = new String[]{"日语考试", "内含日语考试题目，是2016年新版题库"};
        String[] item4 = new String[]{"法语考试", "内含法语考试题目，是2017年新版题库"};
        String[] item5 = new String[]{"英语考试", "内含英语考试题目，是2016年新版题库"};


        group_data.add(item1);
        group_data.add(item2);
        group_data.add(item3);
        group_data.add(item4);
        group_data.add(item5);
    }

    private void loadTestList(){
        final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        //在dirTests文件夹下预放考题
        CopyRawtodata.readFromRaw(getActivity(), R.raw.jiaozhaokaoshishiti2, ROOT_PATH + "/Chaojiyiji/dirTests", "驾照考试-试题二.mtest");
        CopyRawtodata.readFromRaw(getActivity(), R.raw.zhongyikaoshimonishiti, ROOT_PATH + "/Chaojiyiji/dirTests", "中医助理医师考试模拟试题.mtest");

        list = getMTESTFiles();

    }

    /**
     * 以下为自动扫描指定文件夹下mtest文件的代码
     */
    File dirTests;
    File downloadTests;
    public List<String[]> getMTESTFiles(){
        final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e("ROOT_PATH>>>>", ROOT_PATH);
        dirTests = new File(ROOT_PATH + "/Chaojiyiji/dirTests");
        downloadTests = new File(ROOT_PATH + "/Chaojiyiji/downloadTests");
        if (!dirTests.exists()) {
            boolean b = dirTests.mkdirs();
        }
        if (!downloadTests.exists()) {
            boolean b =downloadTests.mkdirs();
        }


        File[] files = dirTests.listFiles();
        List<String[]> list = new ArrayList<>();
        if (files != null){
            for(int i = 0; i < files.length; i++){
                String [] testInfo = new String[2];
                if (files[i].getName().toLowerCase().endsWith(".mtest")){
                    testInfo[0] = files[i].getName();
                    testInfo[1] = files[i].getAbsolutePath();
                }
                list.add(testInfo);
            }
            Log.e("DeckTest--list", list.toString());
        }

        return list;

    }

}
