package com.ichi2yiji.anki.adapter.ek;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.activity.ek.EKPrepareExamActivity;
import com.ichi2yiji.anki.activity.ek.EKSelectChapterActivity;
import com.ichi2yiji.anki.helper.ek.CardAdapterHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jameson on 8/30/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final Context mContext;
    private List<Integer> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private OnItemClickListener mOnItemClickListener;

    public CardAdapter(Context context, List<Integer> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ek_view_card_item, parent, false);
//        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        RelativeLayout rl_ek_view_card_item_selectchapter = (RelativeLayout) holder.itemView.findViewById(R.id.rl_ek_view_card_item_selectchapter);
        RelativeLayout rl_ek_view_card_item_prepareexam = (RelativeLayout) holder.itemView.findViewById(R.id.rl_ek_view_card_item_prepareexam);
        rl_ek_view_card_item_selectchapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EKSelectChapterActivity.class);
                mContext.startActivity(intent);
            }
        });
        rl_ek_view_card_item_prepareexam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EKPrepareExamActivity.class);
                mContext.startActivity(intent);
            }
        });

//        holder.mImageView.setImageResource(mList.get(position));
//        holder.mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toasts.showShort(holder.mImageView.getContext(), "" + position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view);
    }

}
