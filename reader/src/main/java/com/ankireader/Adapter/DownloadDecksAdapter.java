package com.ankireader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/26.
 */

public class DownloadDecksAdapter extends BaseAdapter {

    private  Context context;
    private List<String[]> arrayList;
    private myViewHolder holder;

    public  DownloadDecksAdapter(Context context, List<String[]> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    public class myViewHolder{
        TextView goodsName;
        TextView share;
        TextView delete;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView  != null){
            holder = (myViewHolder) convertView.getTag();
        }else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_downloaddecks, null);
            holder = new myViewHolder();
            holder.goodsName = (TextView) convertView.findViewById(R.id.goods_name);
            holder.share = (TextView) convertView.findViewById(R.id.share);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }
        final String[] paizus_info = arrayList.get(position);
        holder.goodsName.setText(paizus_info[1]);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "分享：" + paizus_info[0], Toast.LENGTH_SHORT).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "删除：" + paizus_info[0], Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
