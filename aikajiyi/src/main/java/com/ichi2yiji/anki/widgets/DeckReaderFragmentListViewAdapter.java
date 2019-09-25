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

public class DeckReaderFragmentListViewAdapter extends BaseAdapter {

    private ListView listView;
    private Context context;
    private List<String[]> data;

    public DeckReaderFragmentListViewAdapter(ListView listView, Context context, List<String[]> data) {
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
            convertView =  LayoutInflater.from(context).inflate(R.layout.deckreader_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.bookname = (TextView) convertView.findViewById(R.id.text_book_name);
            viewHolder.progress = (TextView) convertView.findViewById(R.id.text_progress);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        viewHolder.bookname.setText("ian.txt");
//        viewHolder.bookname.setText((String)data.get(position));
        viewHolder.bookname.setText(data.get(position)[0]);
        int progress = 34;
        viewHolder.progress.setText("进度：" + progress + "%");

        return convertView;
    }

    private final class ViewHolder{
        ImageView book_image;
        TextView bookname;
        TextView progress;
        ImageView arrow_right;
    }

}
