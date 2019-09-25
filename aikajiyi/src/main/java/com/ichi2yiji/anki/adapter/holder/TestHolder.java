package com.ichi2yiji.anki.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

/**
 * Created by Administrator on 2017/4/4.
 */
public class TestHolder extends RecyclerView.ViewHolder{
    public RelativeLayout rlTestsItem;
    public TextView text1;
    public TextView text2;
    
    public TestHolder(View itemView) {
        super(itemView);
        rlTestsItem = (RelativeLayout) itemView.findViewById(R.id.rl_tests_item);
        text1 = (TextView) itemView.findViewById(R.id.text_1);
        text2 = (TextView) itemView.findViewById(R.id.text_2);
    }

}
