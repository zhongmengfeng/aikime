package com.ichi2yiji.anki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.PayOnlineActivity;
import com.ichi2yiji.anki.bean.SharedDecksBean;

import java.util.ArrayList;
import java.util.List;

import static com.chaojiyiji.yiji.R.id.img_lock;

/**
 * Created by ekar01 on 2017/5/23.
 */

public class ProfessionDecksAdapter extends BaseAdapter {
    private final String isPay;
    private final String free_course_number;
    private final String classname;
    private final String total_course;
    private final String class_price;
    private Context context;
    private List<SharedDecksBean.DataBean.ProfessionalsBean.DecksBean> list=new ArrayList<>();
    private int clickTemp=1;
    private ItemDownClicklistener item_shakerecycler;
    private SharedDecksBean.DataBean.ProfessionalsBean.DecksBean decksBean;

    public ItemDownClicklistener getItem_shakerecycler() {
        return item_shakerecycler;
    }

    public void setItem_shakerecycler(ItemDownClicklistener item_shakerecycler) {
        this.item_shakerecycler = item_shakerecycler;
    }


    public void setSecletion(int position){
        clickTemp=position;
    }

    public ProfessionDecksAdapter(Context context,
                                  List<SharedDecksBean.DataBean.ProfessionalsBean.DecksBean> list,
                                  String isPay,
                                  String free_course_number,
                                  String classname,
                                  String class_price,
                                  String total_course) {
        this.context = context;
        this.list = list;
        this.isPay = isPay;
        this.free_course_number = free_course_number;
        this.classname = classname;
        this.class_price = class_price;
        this.total_course = total_course;
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
            convertView= View.inflate(context,R.layout.item_shakerecycler,null);
            holder=new ViewHolder();
            holder.item_shake_desc = (TextView) convertView.findViewById(R.id.item_shake_desc);
            holder.item_shake_name = (TextView) convertView.findViewById(R.id.item_shake_name);
            holder.item_shake_time = (TextView) convertView.findViewById(R.id.item_shake_time);
            holder.item_shake_xia = (TextView) convertView.findViewById(R.id.item_shake_xia);
            holder.img_download = (ImageView) convertView.findViewById(R.id.img_download);
            holder.img_lock = (ImageView) convertView.findViewById(img_lock);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        decksBean = list.get(position);
        if(decksBean == null){
            return null;
        }
        holder.item_shake_name.setText(decksBean.getTitle());
        holder.item_shake_desc.setText(decksBean.getShort_desc());
        holder.item_shake_time.setText(decksBean.getAddtime());
        holder.item_shake_xia.setText(decksBean.getXia());

        if("1".equals(isPay)){
            holder.img_lock.setVisibility(View.GONE);
            holder.img_download.setVisibility(View.VISIBLE);
            holder.img_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item_shakerecycler.itemdownclicklistener(v,position);

                }
            });
        }else{
            if(position < Integer.parseInt(free_course_number)){
                holder.img_lock.setVisibility(View.GONE);
                holder.img_download.setVisibility(View.VISIBLE);
                holder.img_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item_shakerecycler.itemdownclicklistener(v,position);

                    }
                });
            }else{
                holder.img_lock.setVisibility(View.VISIBLE);
                holder.img_download.setVisibility(View.GONE);
                holder.img_download.setOnClickListener(null);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(isPay)){
                    return;
                }
                if(!"1".equals(isPay) && position < Integer.parseInt(free_course_number)){
                    return;
                }
                //跳转到支付方式页面
                Intent intent = new Intent(context, PayOnlineActivity.class);
                intent.putExtra("class_id", decksBean.getClass_id());
                intent.putExtra("classname", classname);
                intent.putExtra("total_course", total_course);
                intent.putExtra("price", class_price);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
    public interface ItemDownClicklistener{
        public void itemdownclicklistener(View view, int position);
    }
    class ViewHolder{
        TextView item_shake_desc;
        TextView item_shake_name;
        TextView item_shake_time;
        TextView item_shake_xia;
        ImageView img_download;
        ImageView img_lock;

    }
}
