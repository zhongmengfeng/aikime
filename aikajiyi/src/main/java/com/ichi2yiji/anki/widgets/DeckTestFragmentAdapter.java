package com.ichi2yiji.anki.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class DeckTestFragmentAdapter extends BaseAdapter {

    private ListView listView;
    private Context context;
    private List<Object> data;

    public DeckTestFragmentAdapter(ListView listView, Context context, List<Object> data) {
        super();
        this.listView = listView;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView =  LayoutInflater.from(context).inflate(R.layout.decktest_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tiku_name = (TextView) convertView.findViewById(R.id.text_tiku_name);
            viewHolder.explain = (TextView) convertView.findViewById(R.id.text_explain);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tiku_name.setText((String)data.get(position));

        viewHolder.explain.setText("内含200道经典题目，是2016年新...");

        return convertView;
    }

    private final class ViewHolder{
        ImageView image;
        TextView tiku_name;
        TextView explain;
        ImageView arrow_right;
    }

}
