package com.ichi2yiji.anki.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.view.BaseListView;

/**
 * Created by ekar01 on 2017/7/4.
 */

public class EKFragment3 extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddDeckfragment";
    private TextView tv_more;
    private ImageView img_add_return;
    private BaseListView mListview;
    private RelativeLayout rl_wrong;
    private RelativeLayout add_rl;
    private RelativeLayout rl_add_deck_back;

    public static EKFragment3 newInstance(String mMusicFragment) {
        EKFragment3 ekFragment3 = new EKFragment3();
        return ekFragment3;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ek_fragment_share, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

//        tv_more = (TextView) view.findViewById(R.id.tv_more);
//        img_add_return = (ImageView) view.findViewById(R.id.img_add_return);
//        rl_add_deck_back = (RelativeLayout) view.findViewById(R.id.rl_add_deck_back);
//        mListview = (BaseListView) view.findViewById(R.id.addRecyclerview);
//        rl_wrong = (RelativeLayout) view.findViewById(R.id.add_rl_wrong);
//        add_rl = (RelativeLayout) view.findViewById(R.id.add_rl);
//        tv_more.setOnClickListener(this);
//        rl_add_deck_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_more:
                break;
            case R.id.rl_add_deck_back:
                break;
        }
    }
}
