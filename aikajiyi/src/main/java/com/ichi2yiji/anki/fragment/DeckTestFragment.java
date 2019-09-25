package com.ichi2yiji.anki.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.DeckTest;
import com.ichi2yiji.anki.DeckTestReal;
import com.ichi2yiji.anki.DownloadBooks;
import com.ichi2yiji.anki.DownloadTests;
import com.ichi2yiji.anki.dialogs.AlertDialog;
import com.ichi2yiji.anki.widgets.DeckTestFragmentAdapter;
import com.ichi2yiji.anki.widgets.DeckTestGridViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class DeckTestFragment extends Fragment {
    private ListView listView;
    private TextView text_load_online_test;
    private TextView text_share_library;
    private List list;
    private ArrayList<String[]> data_original;
    private ArrayList<String[]> data;
    private DeckTestGridViewAdapter deckTestGridViewAdapter;

    private TextView more;
    private GridView gridView;
    private boolean isGridViewShowAllItems;

    public DeckTestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        initData();
//        return inflater.inflate(R.layout.fragment_deck_test, container, false);
        initGridViewOriginalData();
        data = new ArrayList<>();
        data.add(data_original.get(0));
        data.add(data_original.get(1));
        data.add(data_original.get(2));
        return inflater.inflate(R.layout.fragment_deck_test_new, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        listView = (ListView)view.findViewById(R.id.decktest_listview);
        text_load_online_test = (TextView) view.findViewById(R.id.text_load_online_test);
        text_share_library = (TextView) view.findViewById(R.id.text_share_library);

//        DeckTestFragmentAdapter adapter = new DeckTestFragmentAdapter(listView, getActivity(), list);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(getActivity(), DeckTestReal.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//            }
//        });

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

        more = (TextView) view.findViewById(R.id.more);
        gridView = (GridView) view.findViewById(R.id.grid_view_test);
        deckTestGridViewAdapter = new DeckTestGridViewAdapter(getActivity(), data);
        gridView.setAdapter(deckTestGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DeckTestReal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGridViewShowAllItems){
                    data.clear();
                    data.addAll(data_original);
                    deckTestGridViewAdapter.notifyDataSetChanged();
                    more.setText("收起");
                    isGridViewShowAllItems = true;
                }else{
                    data.clear();
                    data.add(data_original.get(0));
                    data.add(data_original.get(1));
                    data.add(data_original.get(2));
                    deckTestGridViewAdapter.notifyDataSetChanged();
                    more.setText("更多");
                    isGridViewShowAllItems = false;
                }
            }
        });
    }

    private void initData(){
        list = new ArrayList();
        list.add("科目一之题库001");
        list.add("科目一之题库002");

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

}
