package com.ichi2yiji.anki.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.AikaAttentClassActivity2;
import com.ichi2yiji.anki.AnkiDroidApp;
import com.ichi2yiji.anki.bean.AttentBean;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.utils.xUtilsImageUtils;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ekar01 on 2017/5/26.
 */

public class AttentAdapter extends BaseAdapter {
    private Context context;
    private List<AttentBean.DataBean> list;
    private String mem_id;
    private String class_id;


    public AttentAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<AttentBean.DataBean> list, String mem_id){
        this.mem_id = mem_id;
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list != null){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= View.inflate(context, R.layout.aika_attent_list_item, null);
            holder=new ViewHolder();
            holder.tv_class = (TextView) convertView.findViewById(R.id.tv_class);
            holder.tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
            holder.tv_attent_number = (TextView) convertView.findViewById(R.id.tv_attent_number);
            holder.iv_attent = (ImageView) convertView.findViewById(R.id.iv_attent);
            holder.iv_user_pic = (ImageView) convertView.findViewById(R.id.iv_user_pic);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final AttentBean.DataBean dataBean = list.get(position);
        if(dataBean != null){

            holder.tv_class.setText(dataBean.getClass_name());
            holder.tv_teacher.setText(dataBean.getTeacher_name());
            holder.tv_attent_number.setText(dataBean.getNumber());
            xUtilsImageUtils.display(holder.iv_user_pic,dataBean.getFace(),8);

        }

        final String class_id = dataBean.getClass_id();
        holder.iv_attent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(list.size() > 1){
                    offAttentOneClass(class_id,"0000");
                }
            }
        });

        return convertView;
    }

    class ViewHolder{

        TextView tv_class;
        TextView tv_teacher;
        TextView tv_attent_number;

        ImageView iv_attent;
        ImageView iv_user_pic;
    }

    /**
     * 关注班级
     * @param classId
     * @param index
     */
    public void attentOneClass(String classId, final String index){
        class_id = classId;
        Log.e("class_id======",class_id+"");
        String attentedUrl = AnkiDroidApp.BASE_DOMAIN + "Home/App/guanzhuClass/";
        Log.e("attentedUrl=====",attentedUrl);
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("class_id", class_id);
        ZXUtils.Post(attentedUrl, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                AikaAttentClassActivity2 activity = (AikaAttentClassActivity2) context;
                activity.getAttentionJsonData(mem_id);
            }
        });
    }

    /**
     * 取消关注
     * @param classId
     * @param index
     */
    public void offAttentOneClass(String classId, final String index){
        class_id = classId;
        Log.e("class_id$$$$$$",class_id);
        String unattentedUrl = AnkiDroidApp.BASE_DOMAIN + "Home/App/removeGuanzhu/";
        Log.e("unattentedUrl",unattentedUrl);
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("class_id", class_id);
        ZXUtils.Post(unattentedUrl, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                AikaAttentClassActivity2 activity = (AikaAttentClassActivity2) context;
                activity.getAttentionJsonData(mem_id);
            }
        });
    }
}
