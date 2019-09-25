package com.ichi2yiji.anki.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ek.CardAdapter;
import com.ichi2yiji.anki.helper.ek.CardScaleHelper;
import com.ichi2yiji.anki.view.BaseListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekar01 on 2017/7/4.
 */

public class EKFragment1 extends Fragment implements View.OnClickListener {


    private static final String TAG = "AddDeckfragment";
    private TextView tv_more;
    private ImageView img_add_return;
    private BaseListView mListview;
    private RelativeLayout rl_wrong;
    private RelativeLayout add_rl;
    private RelativeLayout rl_add_deck_back;
    private static String fragmentText;
    private List<Integer> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper;
    private int mLastPos = -1;
    private ImageView iv_ek_fragment_study_filterdeck;
    private ImageView iv_ek_fragment_study_changestudyplan;
    private RecyclerView srv_ek_fragment_study_recyclerview;
    private TextView tv_ek_fragment_study_studyprogress;
    private TextView tv_ek_fragment_study_startstudying;
    private TextView tv_ek_fragment_study_test;
    private TextView tv_ek_fragment_study_read;

    public static EKFragment1 newInstance(String fragmentName) {

        EKFragment1 ekFragment1 = new EKFragment1();
        return ekFragment1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ek_fragment_study, null);
        initView(view);
//        TextView tv = (TextView) view.findViewById(R.id.tv);
//        tv.setText(fragmentText);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

        initData();
    }

    private void initView(View view) {
        iv_ek_fragment_study_filterdeck = (ImageView) view.findViewById(R.id.iv_ek_fragment_study_filterdeck);
        iv_ek_fragment_study_changestudyplan = (ImageView) view.findViewById(R.id.iv_ek_fragment_study_changestudyplan);
        srv_ek_fragment_study_recyclerview = (RecyclerView) view.findViewById(R.id.srv_ek_fragment_study_recyclerview);
        tv_ek_fragment_study_studyprogress = (TextView) view.findViewById(R.id.tv_ek_fragment_study_studyprogress);
        tv_ek_fragment_study_startstudying = (TextView) view.findViewById(R.id.tv_ek_fragment_study_startstudying);
        tv_ek_fragment_study_test = (TextView) view.findViewById(R.id.tv_ek_fragment_study_test);
        tv_ek_fragment_study_read = (TextView) view.findViewById(R.id.tv_ek_fragment_study_read);

        iv_ek_fragment_study_filterdeck.setOnClickListener(this);
        iv_ek_fragment_study_changestudyplan.setOnClickListener(this);
        tv_ek_fragment_study_startstudying.setOnClickListener(this);
        tv_ek_fragment_study_test.setOnClickListener(this);
        tv_ek_fragment_study_read.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_ek_fragment_study_filterdeck:
                Toast.makeText(getActivity(), "点击了筛选牌组", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_ek_fragment_study_changestudyplan:
                Toast.makeText(getActivity(), "点击了更改学习计划", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_ek_fragment_study_startstudying:
                Toast.makeText(getActivity(), "点击了开始学习", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_ek_fragment_study_test:
                Toast.makeText(getActivity(), "点击了开始测试", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_ek_fragment_study_read:
                Toast.makeText(getActivity(), "点击了开始阅读", Toast.LENGTH_SHORT).show();
                break;
            
        }
    }

    private void init() {
        for (int i = 0; i < 3; i++) {
            mList.add(R.drawable.aika);
            mList.add(R.drawable.aika);
            mList.add(R.drawable.aika);
        }
        srv_ek_fragment_study_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CardAdapter cardAdapter = new CardAdapter(getActivity(), mList);
        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int childAdapterPosition = srv_ek_fragment_study_recyclerview.getChildAdapterPosition(view);
                Toast.makeText(getActivity(), "item click index = "+childAdapterPosition, Toast.LENGTH_SHORT).show();
            }
        });
        srv_ek_fragment_study_recyclerview.setAdapter(cardAdapter);


        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(srv_ek_fragment_study_recyclerview);
        initBlurBackground();
    }

    private void initData() {

    }

    private void initBlurBackground() {
//        mBlurView = (ImageView) findViewById(R.id.blurView);
        srv_ek_fragment_study_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                }
            }
        });

        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        final int resId = mList.get(mCardScaleHelper.getCurrentItemPos());
//        mBlurView.removeCallbacks(mBlurRunnable);
//        mBlurRunnable = new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
//                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
//            }
//        };
//        mBlurView.postDelayed(mBlurRunnable, 500);
    }

}
