package com.ichi2yiji.anki;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ek.CardAdapter;
import com.ichi2yiji.anki.helper.ek.CardScaleHelper;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageView mBlurView;
    private List<Integer> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.ek_activity_recycle_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        init();
    }

    private void init() {
        for (int i = 0; i < 3; i++) {
            mList.add(R.drawable.aika);
            mList.add(R.drawable.aika);
            mList.add(R.drawable.aika);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecycleViewActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new CardAdapter(this, mList));
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);
        initBlurBackground();
    }

    private void initBlurBackground() {
//        mBlurView = (ImageView) findViewById(R.id.blurView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
