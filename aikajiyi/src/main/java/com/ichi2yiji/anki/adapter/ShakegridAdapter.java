package com.ichi2yiji.anki.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.bean.SharedDecksBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekar01 on 2017/5/19.
 */

public class ShakegridAdapter extends BaseAdapter {
    private Context context;
    private List<SharedDecksBean.DataBean.CatsBean> list=new ArrayList<>();
    private int clickTemp=1;
    private int defItem;


    public void setSecletion(int position){
        clickTemp=position;
    }

    public ShakegridAdapter(Context context, List<SharedDecksBean.DataBean.CatsBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        view = View.inflate(context, R.layout.item_shakegrid, null);
        TextView textView = (TextView) view.findViewById(R.id.item_shaketext);
        textView.setText(list.get(position).getCat_name());
        if(clickTemp==position){
            textView.setBackgroundResource(R.drawable.shakegrid_select);
            textView.setTextColor(Color.BLACK);
        }else{
            textView.setBackgroundResource(R.drawable.shakegrid);
        }
        return view;
    }
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

}
