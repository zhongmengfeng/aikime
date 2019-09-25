package com.ichi2yiji.anki.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AnkiDroidApp;
import com.ichi2yiji.anki.bean.DistributeBean;
import com.ichi2yiji.utils.xUtilsImageUtils;

import java.util.List;

/**
 * Created by ekar01 on 2017/6/13.
 */

public class ClassListAdapter extends BaseAdapter {

    private Context context;
    private List<DistributeBean.DataBean.ClassInfoBean.StudentsBean> studentsBeanList;

    public ClassListAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<DistributeBean.DataBean.ClassInfoBean.StudentsBean> studentsBeanList){
        this.studentsBeanList = studentsBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(null == studentsBeanList){
            return 0;
        }
        return studentsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentsBeanList.get(position);
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
            convertView = View.inflate(context, R.layout.item_classlist, null);//null改为parent也会
            holder.tv_item_classlist_tagStr = (TextView) convertView.findViewById(R.id.tv_item_classlist_tagStr);
            holder.iv_item_classlist_icon = (ImageView) convertView.findViewById(R.id.iv_item_classlist_icon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        DistributeBean.DataBean.ClassInfoBean.StudentsBean studentsBean = studentsBeanList.get(position);
        holder.tv_item_classlist_tagStr.setText(studentsBean.getHoneyname());

        ViewGroup.LayoutParams params = holder.iv_item_classlist_icon.getLayoutParams();
        params.width = (int)(AnkiDroidApp.screenWidth / 6);
        params.height = (int)(AnkiDroidApp.screenWidth / 6);
        if (!TextUtils.isEmpty(studentsBean.getFace())) {
			xUtilsImageUtils.display(holder.iv_item_classlist_icon,studentsBean.getFace(),8);
        }
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_item_classlist_tagStr;
        ImageView iv_item_classlist_icon;
    }
}
