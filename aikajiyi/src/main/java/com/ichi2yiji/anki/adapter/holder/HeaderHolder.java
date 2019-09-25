package com.ichi2yiji.anki.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

/**
 * Created by Administrator on 2017/4/4.
 */
public class HeaderHolder extends RecyclerView.ViewHolder{

    public RelativeLayout courseNameLyt;
    public TextView testCourseName;
    public ImageView arrow;
    public TextView more;
    public TextView detailLyt;
    
    public HeaderHolder(View itemView) {
        super(itemView);
        courseNameLyt = (RelativeLayout) itemView.findViewById(R.id.course_name_lyt);
        testCourseName = (TextView) itemView.findViewById(R.id.test_course_name);
        arrow = (ImageView) itemView.findViewById(R.id.arrow);
        more = (TextView) itemView.findViewById(R.id.more);
        detailLyt = (TextView) itemView.findViewById(R.id.detail_lyt);
    }
}
