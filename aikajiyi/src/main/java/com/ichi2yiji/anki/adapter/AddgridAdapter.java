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

public class AddgridAdapter extends BaseAdapter {
    private Context context;
    private List<SharedDecksBean.DataBean.ClassCreatedByMeBean> clist=new ArrayList<>();
    private List<SharedDecksBean.DataBean.ProfessionalsBean> plist=new ArrayList<>();
    private List<String> list=new ArrayList<>();
    private int clickTemp=1;

    public void setSecletion(int position){
        clickTemp=position;
    }

    public AddgridAdapter(Context context, List<SharedDecksBean.DataBean.ClassCreatedByMeBean> clist, List<SharedDecksBean.DataBean.ProfessionalsBean> plist, List<String> list) {
        this.context = context;
        this.clist = clist;
        this.plist = plist;
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
        view= View.inflate(context, R.layout.item_addgrid,null);
        TextView item_addtext = (TextView) view.findViewById(R.id.item_addtext);
        item_addtext.setText(list.get(position));
        if(clickTemp==position){
            item_addtext.setBackgroundResource(R.drawable.addgrid_select);
            item_addtext.setTextColor(Color.WHITE);
            //判断哪个是Professionals
            if(list.get(position).equals(plist.get(position).getClassname())){
                item_addtext.setBackgroundResource(R.drawable.addpro_select);
                item_addtext.setTextColor(Color.WHITE);
            }
        }else{
            item_addtext.setBackgroundResource(R.drawable.addgrid);
            item_addtext.setTextColor(Color.parseColor("#a0d468"));
            if(list.get(position).equals(plist.get(position).getClassname())){
                item_addtext.setBackgroundResource(R.drawable.add_pro);
                item_addtext.setTextColor(Color.parseColor("#ffce54"));
            }

        }
        return view;
    }
}
