package com.ichi2yiji.anki.adapter.ek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKExpertClassAdapter extends BaseAdapter {

    private Context mContext;

    public EKExpertClassAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= View.inflate(mContext, R.layout.ek_item_expertclass,null);
            holder=new ViewHolder();
            holder.tv_ek_item_expertclass_classname = (TextView) convertView.findViewById(R.id.tv_ek_item_expertclass_classname);
            holder.tv_ek_item_expertclass_summary = (TextView) convertView.findViewById(R.id.tv_ek_item_expertclass_summary);
            holder.tv_ek_item_expertclass_number = (TextView) convertView.findViewById(R.id.tv_ek_item_expertclass_number);
            holder.tv_ek_item_expertclass_download = (TextView) convertView.findViewById(R.id.tv_ek_item_expertclass_download);
            holder.tv_ek_item_expertclass_presubscribe = (TextView) convertView.findViewById(R.id.tv_ek_item_expertclass_presubscribe);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(position ==0){
            holder.tv_ek_item_expertclass_presubscribe.setVisibility(View.VISIBLE);
            holder.tv_ek_item_expertclass_download.setVisibility(View.GONE);
        }else{
            holder.tv_ek_item_expertclass_presubscribe.setVisibility(View.GONE);
            holder.tv_ek_item_expertclass_download.setVisibility(View.VISIBLE);
        }

        holder.tv_ek_item_expertclass_classname.setText("我的课程" + position);
        holder.tv_ek_item_expertclass_summary.setText("课程描述" + position);
        holder.tv_ek_item_expertclass_number.setText("100" + position);
        return convertView;
    }


    class ViewHolder{
        TextView tv_ek_item_expertclass_classname;
        TextView tv_ek_item_expertclass_summary;
        TextView tv_ek_item_expertclass_number;
        TextView tv_ek_item_expertclass_download;
        TextView tv_ek_item_expertclass_presubscribe;
    }
}
