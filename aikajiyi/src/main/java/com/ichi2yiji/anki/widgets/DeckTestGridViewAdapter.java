package com.ichi2yiji.anki.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/4.
 */

public class DeckTestGridViewAdapter extends BaseAdapter {

    private  Context context;
    private  ArrayList<String[]> list;

    public DeckTestGridViewAdapter(Context context, ArrayList<String[]> list){
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.decktest_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView_1 = (TextView) convertView.findViewById(R.id.text_1);
            viewHolder.textView_2 = (TextView) convertView.findViewById(R.id.text_2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView_1.setText(list.get(position)[0]);
        viewHolder.textView_2.setText(list.get(position)[1]);

        return convertView;
    }

    private class ViewHolder{
        TextView textView_1;
        TextView textView_2;
    }
}
