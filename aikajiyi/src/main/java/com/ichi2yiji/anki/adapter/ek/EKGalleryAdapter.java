package com.ichi2yiji.anki.adapter.ek;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.List;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKGalleryAdapter extends RecyclerView.Adapter<EKGalleryAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Integer> mDatas;

    public EKGalleryAdapter(Context context, List<Integer> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        ImageView mImg;
        TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.ek_item_classdetails, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.course_iv);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final EKGalleryAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.mImg.setImageResource(mDatas.get(i));
    }
}
