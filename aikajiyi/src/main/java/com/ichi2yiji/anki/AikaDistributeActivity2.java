package com.ichi2yiji.anki;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.ClassListAdapter;
import com.ichi2yiji.anki.adapter.MyTextPagerAdapter;
import com.ichi2yiji.anki.bean.DistributeBean;
import com.ichi2yiji.anki.dialogs.ProgressDialog;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AikaDistributeActivity2 extends Activity {

    private String class_id;
    private String goods_id;
    private String mem_id = null;
    private String distribute_json_data;
    private Button bt_aika_distribute_courseware_confirm;
    private RelativeLayout rl_aika_distribute_back;
    private ViewPager vp_aika_distribute_stu;
    private LinearLayout ll_aika_distribute_stu_dots;
    private List<GridView> gvList = new ArrayList<>();
    private MyTextPagerAdapter myTextPagerAdapter;

    private List<DistributeBean.DataBean.ClassInfoBean.StudentsBean> subList;
    private String currentThemeTag;
    private LinearLayout ll_aika_distribute_class;
    private LinearLayout ll_aika_distribute_courseware;
    private TextView tv_aika_distribute_class;
    private TextView tv_aika_distribute_courseware;
    private DistributeBean distributeBean;
    private int classPosition = 0;
    private int coursewarePosition = 0;
    private int pagerIndex = 0;
    private ProgressDialog progressDialog;
    private DistributeBroadcastReceiver distributeBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aika_distribute2);

        ApplyTranslucency.applyKitKatTranslucency(this);

        initView();

        getDistributeJsonData();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//        final String distribute_json_data = pref.getString("DistributeJsonData","");
        mem_id = pref.getString("MEM_ID", "");

        //注册广播
        IntentFilter intentFilter_deckDownload = new IntentFilter("distribute_broadcast");
        distributeBroadcastReceiver = new DistributeBroadcastReceiver();
        registerReceiver(distributeBroadcastReceiver, intentFilter_deckDownload);

//        EventBus.getDefault().register(this);

    }

    private void initView() {
        rl_aika_distribute_back = (RelativeLayout) findViewById(R.id.rl_aika_distribute_back);
        ll_aika_distribute_class = (LinearLayout) findViewById(R.id.ll_aika_distribute_class);
        bt_aika_distribute_courseware_confirm = (Button) findViewById(R.id.bt_aika_distribute_courseware_confirm);
        ll_aika_distribute_courseware = (LinearLayout) findViewById(R.id.ll_aika_distribute_courseware);

        tv_aika_distribute_class = (TextView) findViewById(R.id.tv_aika_distribute_class);
        tv_aika_distribute_courseware = (TextView) findViewById(R.id.tv_aika_distribute_courseware);

        vp_aika_distribute_stu = (ViewPager) findViewById(R.id.vp_aika_distribute_stu);
        ll_aika_distribute_stu_dots = (LinearLayout) findViewById(R.id.ll_aika_distribute_stu_dots);

        rl_aika_distribute_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击返回键
                finish();
            }
        });
        ll_aika_distribute_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击选择班级
                if(null != distributeBean){
                    DistributeBean.DataBean data = distributeBean.getData();
                    Intent intent = new Intent(AikaDistributeActivity2.this, SelectClassActivity.class);
                    intent.putExtra("data", data);
                    intent.putExtra("type", 1);
                    intent.putExtra("classPosition", classPosition);
                    startActivity(intent);
                }
            }
        });
        ll_aika_distribute_courseware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击选择课件
                if(null != distributeBean) {
                    DistributeBean.DataBean data = distributeBean.getData();
                    Intent intent = new Intent(AikaDistributeActivity2.this, SelectClassActivity.class);
                    intent.putExtra("data", data);
                    intent.putExtra("type", 2);
                    intent.putExtra("coursewarePosition", coursewarePosition);
                    startActivity(intent);
                }

            }
        });
        bt_aika_distribute_courseware_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确认分发按钮
                Toast.makeText(AikaDistributeActivity2.this, "开始分发", Toast.LENGTH_SHORT).show();
                comfirmDistribute();
            }
        });

        myTextPagerAdapter = new MyTextPagerAdapter();

        progressDialog = new ProgressDialog(this).builder();

        vp_aika_distribute_stu.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pagerIndex = position;
                for (int i = 0; i < ll_aika_distribute_stu_dots.getChildCount(); i++) {
                    if (i == position) {
                        ((ImageView) ll_aika_distribute_stu_dots.getChildAt(i)).setImageResource(R.drawable.dot_checked);
                    } else {
                        ((ImageView) ll_aika_distribute_stu_dots.getChildAt(i)).setImageResource(R.drawable.dot_unchecked);
                    }
                }
            }


            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 添加指示的圆点
     */
    private void addDot(LinearLayout ll, int size) {
        ll.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView mPointImg = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.setMargins(5, 0, 5, 0);
            mPointImg.setLayoutParams(params);
            if (i == 0) {
                mPointImg.setImageResource(R.drawable.dot_checked);
            } else {
                mPointImg.setImageResource(R.drawable.dot_unchecked);
            }
            ll.addView(mPointImg);
        }
    }

    /**
     * 确认分发接口
     */
    public void comfirmDistribute() {
        if(null != distributeBean){
            class_id = distributeBean.getData().getClassInfo().get(0).getClass_id();
            goods_id = distributeBean.getData().getDecks().get(0).getGoods_id();
        }

        String url = AnkiDroidApp.BASE_DOMAIN + "Home/Application/pushGroup/";
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("class_id", class_id);
        map.put("goods_id", goods_id);
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e("comfirmDistribute", ">>>>>>>>>>onSuccess>>>" + result);
                Toast.makeText(getApplicationContext(), "课件分发成功！", Toast.LENGTH_SHORT).show();
                AikaDistributeActivity2.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("comfirmDistribute", ">>>>>>>>>>onError>>>" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //从后台请求分发作业页面的数据
    public void getDistributeJsonData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = pref.edit();
        String mem_id = pref.getString("MEM_ID", "");
        Log.e("getDistributeJsonData", ">>>onSuccess>>>" + "mem_id: " + mem_id);
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/Application/messageList/";
        Map map = new HashMap<String, String>();
        map.put("mem_id", mem_id);
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

//                if(result.length() > 4000) {
//                    for(int i=0;i<result.length();i+=4000){
//                        if(i+4000<result.length())
//                            Log.i("getDistributeJsonData" + i,">>>onSuccess>>>" + result.substring(i, i+4000));
//                        else
//                            Log.i("getDistributeJsonData" + i,">>>onSuccess>>>" + result.substring(i, result.length()));
//                    }
//                } else{
//                    Log.i("getDistributeJsonData",">>>onSuccess>>>" + result);
//                }

                try {
                    Log.e("getDistributeJsonData", ">>>onSuccess>>>" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    editor.putString("DistributeJsonData", jsonObject.toString());
                    editor.commit();
                    distributeBean = GsonUtil.json2Bean(result, DistributeBean.class);
                    int code = distributeBean.getCode();
                    if (code == 1000) {
                        refreshView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                distributeBean = loadData();
                if(null != distributeBean){
                    int code = distributeBean.getCode();
                    if (code == 1000) {
                        Log.e("getDistributeJsonData", ">>>refreshView>>>" + "class_name: goods_name: ");
                        refreshView();
                    }
                }else{
                    progressDialog.showErrorWithStatus();
                    progressDialog.setDownloadTip("网络错误");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void refreshView() {
        Log.e("getDistributeJsonData", ">>>refreshView>>>");
        if(null != distributeBean){
            Log.e("getDistributeJsonData", ">>>refreshView>>>" + "null != distributeBean");
            String class_name = distributeBean.getData().getClassInfo().get(classPosition).getClass_name();
            String goods_name = distributeBean.getData().getDecks().get(coursewarePosition).getGoods_name();
            tv_aika_distribute_class.setText(class_name);
            tv_aika_distribute_courseware.setText(goods_name);
            Log.e("getDistributeJsonData", ">>>refreshView>>>" + "class_name: " + class_name + "goods_name: " + goods_name);

            List<DistributeBean.DataBean.ClassInfoBean.StudentsBean> studentsList = distributeBean.getData().getClassInfo().get(classPosition).getStudents();
            //设置含有tag的GridView+ViewPager数据
            final int studentsSize = studentsList.size();
            gvList.clear();//或者gvList不要设置成全局变量,而设置成局部变量,这行代码就可以不要
            for (int i = 0; i <= (studentsSize - 1) / 9; i++) {//每9个标签换一页
                final ClassListAdapter classListAdapter = new ClassListAdapter(this);
    //				GridView gv = new GridView(getContext());
    //				gv.setGravity(Gravity.CENTER);
    //				gv.setClickable(true);
    //				gv.setFocusable(true);
    //				gv.setNumColumns(2);
                View contentView = View.inflate(this, R.layout.grid_view, null);
                GridView gv = (GridView) contentView.findViewById(R.id.gv);
                int totalItems = 9;
                gv.setNumColumns(totalItems / 3);

                if (i == (studentsSize - 1) / totalItems) {

                    subList = studentsList.subList(totalItems * i, studentsSize);
                } else {
                    subList = studentsList.subList(totalItems * i, totalItems * i + totalItems);
                }
                gv.setAdapter(classListAdapter);
                classListAdapter.setData(subList);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        DistributeBean.DataBean.ClassInfoBean.StudentsBean studentsBean = (DistributeBean.DataBean.ClassInfoBean.StudentsBean) classListAdapter.getItem(position);
                        int isLast = (studentsSize > 8) ? studentsSize / 8 - 1 : studentsSize -1;
                        if(studentsBean != null){
                            if(position == isLast && (studentsSize - 1) / 8 == pagerIndex){
//                                Toast.makeText(AikaDistributeActivity2.this, "点中了第" + (pagerIndex + 1) + "页的" + "第" + (position + 1) + "条", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(AikaDistributeActivity2.this, "点中了第" + (pagerIndex + 1) + "页的" + "第" + (position + 1) + "条", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                gvList.add(gv);
            }
        }

        vp_aika_distribute_stu.setAdapter(myTextPagerAdapter);
        myTextPagerAdapter.setData(gvList);
        addDot(ll_aika_distribute_stu_dots, gvList.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        unregisterReceiver(distributeBroadcastReceiver);
    }



//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getDistributeMessage(DistributeEvent distributeEvent){
//        String message = distributeEvent.getMessage();
//        String[] split = message.split("_");
//        int type = Integer.parseInt(split[0]);
//        int position = Integer.parseInt(split[1]);
//
//        if(type == 1){
//            String class_name = distributeBean.getData().getClassInfo().get(position).getClass_name();
//            classPosition = position;//这个是要的
//        }else if(type == 2){
//            String goods_name = distributeBean.getData().getDecks().get(position).getGoods_name();
//            coursewarePosition = position;//加不加无所谓
//        }
//        refreshView();
//    }

    public class DistributeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String distribute_message = intent.getStringExtra("distribute_message");
            String[] split = distribute_message.split("_");
            int type = Integer.parseInt(split[0]);
            int position = Integer.parseInt(split[1]);

            if(type == 1){
                String class_name = distributeBean.getData().getClassInfo().get(position).getClass_name();
                classPosition = position;//这个是要的
            }else if(type == 2){
                String goods_name = distributeBean.getData().getDecks().get(position).getGoods_name();
                coursewarePosition = position;//加不加无所谓
            }
            refreshView();
        }
    }


    public DistributeBean loadData() {

        //取出数据
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator +"Chaojiyiji"+ File.separator + "tempcache" + File.separator + ("EKDistributeModel" + "_" + mem_id));
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            Log.e("getAttentionJsonData", "loadData>>>>readObject前>>>>>>>>try>>>>>>>>>>>>>" + objectInputStream.toString());
            DistributeBean data = (DistributeBean) objectInputStream.readObject();
            Log.e("getAttentionJsonData", "loadData>>>>>>>>>>>>try>>>>>>>>>>>>>" + data.toString());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getAttentionJsonData", "loadData>>>>>>>catch>>>>>>>>>>>>>" + e.getMessage());
        } finally {
            if (objectInputStream!=null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream!=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
