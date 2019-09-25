package com.ichi2yiji.anki.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.bean.SearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekar01 on 2017/5/19.
 */

public class ShakerecyclerAdapter extends BaseAdapter {
    private Context context;
    private List<SearchBean.DataBean> list=new ArrayList<>();
    private SVProgressHUD svProgressHUD;
    private String url;
    private String title;
    private String good_id;
    private String decode;
    private ItemDownClicklistener item_shakerecycler;

    public ItemDownClicklistener getItem_shakerecycler() {
        return item_shakerecycler;
    }

    public void setItem_shakerecycler(ItemDownClicklistener item_shakerecycler) {
        this.item_shakerecycler = item_shakerecycler;
    }

    public ShakerecyclerAdapter(Context context, List<SearchBean.DataBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= View.inflate(context, R.layout.item_shakerecycler,null);
            holder=new ViewHolder();
            holder.item_shake_desc = (TextView) convertView.findViewById(R.id.item_shake_desc);
            holder.item_shake_name = (TextView) convertView.findViewById(R.id.item_shake_name);
            holder.item_shake_time = (TextView) convertView.findViewById(R.id.item_shake_time);
            holder.item_shake_xia = (TextView) convertView.findViewById(R.id.item_shake_xia);
            holder.img_download = (ImageView) convertView.findViewById(R.id.img_download);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final SearchBean.DataBean dataBean = list.get(position);
        holder.item_shake_name.setText(dataBean.getTitle());
        holder.item_shake_desc.setText(dataBean.getShort_desc());
        holder.item_shake_time.setText(dataBean.getAddtime());
        holder.item_shake_xia.setText(dataBean.getXia());
        holder.img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_shakerecycler.itemdownclicklistener(v,position);

            }
        });
        return convertView;
    }
    public interface ItemDownClicklistener{
        public void itemdownclicklistener(View view,int position);
    }
    class ViewHolder{
        TextView item_shake_desc;
        TextView item_shake_name;
        TextView item_shake_time;
        TextView item_shake_xia;
        ImageView img_download;
    }
}
