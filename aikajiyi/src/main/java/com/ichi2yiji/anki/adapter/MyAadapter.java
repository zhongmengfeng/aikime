package com.ichi2yiji.anki.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.custom.vg.list.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekar01 on 2017/5/24.
 */

public class MyAadapter extends CustomAdapter {
    private Context context;
    private List<String> list=new ArrayList<>();

    public MyAadapter(Context context, List<String> list) {
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
        convertView= View.inflate(context, R.layout.tag_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tag);
        textView.setText(list.get(position));

        return convertView;
    }
}
