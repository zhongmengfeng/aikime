package com.ichi2yiji.anki.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class DeckReaderFragmentGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String[]> data;

    public DeckReaderFragmentGridViewAdapter(Context context, List<String[]> data){
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
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.deckreader_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.bookname = (TextView) convertView.findViewById(R.id.book_name_grid_view);
            viewHolder.progress = (TextView) convertView.findViewById(R.id.progress_grid_view);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bookname.setText(data.get(position)[0]);
        int progress = 34;
        viewHolder.progress.setText("进度：" + progress + "%");
        return convertView;
    }

    private class ViewHolder{
        TextView bookname;
        TextView progress;
    }
}
