package com.ichi2yiji.anki.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ekar01 on 2017/6/14.
 */

public class TextListAdapter extends BaseAdapter{

    private List<String> mTextList;
    private int mType;
    private int mSelectPosition;
    private Context mContext;
    private Map<Integer, Integer> textSelectedMap;

    public TextListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<String> textList, int type, int selectPosition) {
        this.mTextList = textList;
        mType = type;
        mSelectPosition = selectPosition;

        textSelectedMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < textList.size(); i++) {
            textSelectedMap.put(i, View.GONE);
        }
    }

    @Override
    public int getCount() {
        if(null == mTextList){
            return 0;
        }
        return mTextList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTextList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_textcheckedlist, null);
            mHolder = new ViewHolder();
            mHolder.tv_item_textcheckedlist = (TextView) convertView.findViewById(R.id.tv_item_textcheckedlist);
            mHolder.iv_item_textcheckedlist = (ImageView) convertView.findViewById(R.id.iv_item_textcheckedlist);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.tv_item_textcheckedlist.setText(mTextList.get(position));


        if(position == mSelectPosition){
            mHolder.iv_item_textcheckedlist.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }else{
            mHolder.iv_item_textcheckedlist.setVisibility(View.GONE);
            notifyDataSetChanged();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //选中班级List的某个条目,返回到上个界面

                Intent intent = new Intent();
                intent.setAction("distribute_broadcast");
                intent.putExtra("distribute_message", mType + "_" + position);
                ((Activity)mContext).sendBroadcast(intent);
                ((Activity)mContext).finish();

//                EventBus.getDefault().post(new DistributeEvent(mType + "_" + position));
//                ((Activity)mContext).finish();
            }
        });

//        mOnMyCheckListener.OnMyCheck(position);

        return convertView;
    }

    class ViewHolder{
        TextView tv_item_textcheckedlist;
        ImageView iv_item_textcheckedlist;
    }

    private OnMyCheckListener mOnMyCheckListener;
    public interface OnMyCheckListener{
        void OnMyCheck(int positon);
    }
    public void setOnMyCheckListener(OnMyCheckListener onMyCheckListener){
        this.mOnMyCheckListener = onMyCheckListener;
    }

}
