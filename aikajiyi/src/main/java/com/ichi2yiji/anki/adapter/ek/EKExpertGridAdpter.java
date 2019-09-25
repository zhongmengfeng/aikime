package com.ichi2yiji.anki.adapter.ek;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ScreenUtils;
import com.ichi2yiji.anki.util.ek.DisplayUtils;
import com.ichi2yiji.anki.view.ek.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ekar01 on 2017/7/8.
 */

public class EKExpertGridAdpter extends BaseAdapter {

    List<String> textList;
    private Context mContext;

    public EKExpertGridAdpter(Context context) {
        super();
        this.mContext = context;
        textList = new ArrayList<>();
        textList.add("姓名1");
        textList.add("姓名2");
        textList.add("姓名3");
        textList.add("姓名4");
        textList.add("姓名5");
        textList.add("姓名6");
    }

    public void setData(List<String> textList){
        this.textList = textList;
    }

    @Override
    public int getCount() {
        if(null == textList){
            return 0;
        }
        return textList.size();
    }

    @Override
    public Object getItem(int position) {
        return textList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.ek_item_expertlist, null);
//			holder.iv_fashion_information_list_item_bg = (ImageView) convertView.findViewById(R.id.iv_fashion_information_list_item_bg);
            holder.civ_ek_item_expertlist_iamgeview = (CircleImageView) convertView.findViewById(R.id.civ_ek_item_expertlist_iamgeview);
            holder.tv_ek_item_expertlist_textview = (TextView) convertView.findViewById(R.id.tv_ek_item_expertlist_textview);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //给图片设置颜色过滤器
        holder.civ_ek_item_expertlist_iamgeview.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        ViewGroup.LayoutParams params = holder.civ_ek_item_expertlist_iamgeview.getLayoutParams();
//		params.height = (int)(MyApplication.screenWidth / 5);
//		params.width = (int)(MyApplication.screenWidth / 5);
        params.width = (int) ((ScreenUtils.getScreenWidth(mContext) - DisplayUtils.dip2px(mContext, Float.parseFloat(2 * 10 + "")) - (4 - 1) * 20) / 4);
        params.height = (int) ((ScreenUtils.getScreenWidth(mContext) - DisplayUtils.dip2px(mContext, Float.parseFloat(2 * 10 + "")) - (4 - 1) * 20) / 4);
        holder.civ_ek_item_expertlist_iamgeview.setLayoutParams(params);

//		LayoutParams paramsBg = holder.iv_fashion_information_list_item_bg.getLayoutParams();
//		paramsBg.height = (int)(MyApplication.screenWidth / 6);
//		holder.iv_fashion_information_list_item_bg.setLayoutParams(params);

        holder.tv_ek_item_expertlist_textview.setText(textList.get(position));

        return convertView;
    }

    public static class ViewHolder{
        //		ImageView iv_fashion_information_list_item_bg;
        CircleImageView civ_ek_item_expertlist_iamgeview;
        TextView tv_ek_item_expertlist_textview;
    }
}
