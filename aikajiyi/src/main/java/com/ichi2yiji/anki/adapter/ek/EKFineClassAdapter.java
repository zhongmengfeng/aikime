package com.ichi2yiji.anki.adapter.ek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;

import java.util.List;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKFineClassAdapter extends BaseAdapter {
    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;

    public EKFineClassAdapter(Context context) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5 ;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position );
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = inflater.inflate(R.layout.ek_item_fineclass, null);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }


        return convertView;
    }


    class viewHolder {
        TextView tv;
    }
}
