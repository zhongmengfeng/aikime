package com.ichi2yiji.anki.fragment;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.treeview.Bean;
import com.ichi2yiji.anki.treeview.FileBean;
import com.ichi2yiji.anki.treeview.Node;
import com.ichi2yiji.anki.treeview.SimpleTreeAdapter;
import com.ichi2yiji.anki.treeview.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class DeckPickerFragment extends Fragment {
    private ListView listView;
    private List<Bean> mDatas = new ArrayList<Bean>();
    private List<FileBean> mDatas2 = new ArrayList<FileBean>();

    private TreeListViewAdapter mAdapter;
    private TextView text_share_deck;

    public DeckPickerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_picker, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.deckpicker_listview);
        text_share_deck = (TextView)view.findViewById(R.id.text_share_deck);
        //共享牌组的点击事件
        text_share_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        initDatas();

        try
        {
            mAdapter = new SimpleTreeAdapter<FileBean>(listView, getActivity(), mDatas2, 10);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
            {
                @Override
                public void onClick(Node node, int position)
                {
                    if (node.isLeaf())
                    {
                        Toast.makeText(getActivity(), node.getName(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        listView.setAdapter(mAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initDatas()
    {
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





        mDatas2.add(new FileBean(1, 0, "Default"));
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

}
