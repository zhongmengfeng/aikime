package com.ichi2yiji.anki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.adapter.AttentAdapter;
import com.ichi2yiji.anki.bean.AttentBean;
import com.ichi2yiji.anki.dialogs.ProgressDialog;
import com.ichi2yiji.anki.fragment.YaoqingmaFragment;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.utils.GsonUtil;
import com.ichi2yiji.utils.SPUtil;

import org.xutils.common.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关注班级
 */
public class AikaAttentClassActivity2 extends AppCompatActivity {
    private WebView webView;
    private ImageView iv_aika_attend_back;
    private ListView lv_aika_attend_listview;
    private ImageView iv_aika_attend_guanzhu;
    private RelativeLayout rl_aika_attend_back;
    private String mem_id = null;
    private String class_id = null;
    private String in_class = null;
    String attentJSONData = "";
    private AttentAdapter attentAdapter;
    public static final int REQUEST_CODE = 100;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aika_attent_class2);
        ApplyTranslucency.applyKitKatTranslucency(this);

        progressDialog = new ProgressDialog(this).builder();
        mem_id = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        if (!TextUtils.isEmpty(mem_id)){
            getAttentionJsonData(mem_id);
        }else {
            Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
        }
        /**
         * WebView关注老师
         */
//        webView=(WebView)findViewById(R.id.attent_class_webview);
        iv_aika_attend_back = (ImageView)findViewById(R.id.iv_aika_attend_back);
        lv_aika_attend_listview = (ListView)findViewById(R.id.lv_aika_attend_listview);
        iv_aika_attend_guanzhu = (ImageView)findViewById(R.id.iv_aika_attend_guanzhu);
        rl_aika_attend_back = (RelativeLayout)findViewById(R.id.rl_aika_attend_back);

        rl_aika_attend_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_aika_attend_guanzhu.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AikaAttentClassActivity2.this, YaoqingmaActivity.class);
                intent.putExtra("mem_id",mem_id);
//                startActivity(intent);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        attentAdapter = new AttentAdapter(AikaAttentClassActivity2.this);

    }


    /**
     * 从后台请求关注老师页面的数据
     */
    public void getAttentionJsonData(final String memId){
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/myGuanzhu/";
        Map myMap = new HashMap<String,String>();
        myMap.put("mem_id", mem_id);
        ZXUtils.Post(url, myMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAttentionJsonData", ">>>onSuccess>>>" + result);
                try {

/*
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if(code == 1000){
                        SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG,"AttentionJsonData", jsonObject.toString());
                        //canGoToAttentionPage = true;
                        attentJSONData = jsonObject.toString();
                        JSONArray data = jsonObject.getJSONArray("data");
*/
                    List<AttentBean.DataBean> data = null;
                    AttentBean attentBean = GsonUtil.json2Bean(result, AttentBean.class);
                    int code = attentBean.getCode();
                    if(code == 1000){
                        data = attentBean.getData();
                        Log.e("getAttentionJsonData", "AttentBeanList: " + data);
//                        DataSupport.saveAll(data);
                        lv_aika_attend_listview.setAdapter(attentAdapter);
                        attentAdapter.setData(data,memId);

                    }else{
                        //标志符 让用户先登录
                        //canGoToAttentionPage = false;
                    }
                }catch (Exception e){
                    e.printStackTrace();

//                    List<AttentBean.DataBean> data = DataSupport.findAll(AttentBean.DataBean.class);
//                    if(null != data){
//                        attentAdapter.setData(data,memId);
//                    }
                    attentAdapter.setData(null,memId);
                    Log.e("JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                List<AttentBean.DataBean> data = loadData();
                Log.e("getAttentionJsonData", "onError>>>>>>>>>>>>>>>>>>>>>>>>>" + data.toString());
//                Log.e("onError", ">>>>>>>>>>" + dataJsonStr);
//                List<AttentBean.DataBean> data = GsonUtil.JsonToList(dataJsonStr);

//                List<AttentBean.DataBean> data = DataSupport.findAll(AttentBean.DataBean.class);
                if(null != data){
                    lv_aika_attend_listview.setAdapter(attentAdapter);
                    attentAdapter.setData(data,memId);
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
                Log.e("attentJSONData",attentJSONData);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getAttentionJsonData(mem_id);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == YaoqingmaFragment.RESULT_OK){
            if(requestCode == REQUEST_CODE){
                //刷新的代码写在这里
                Log.e("onActivityResult", "调用了");
                getAttentionJsonData(mem_id);
            }
        }
    }

    public ArrayList<AttentBean.DataBean> loadData() {

        //取出数据
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator +"Chaojiyiji"+ File.separator + "tempcache" + File.separator + ("EKAttentModel" + "_" + mem_id));
            FileInputStream fileInputStream = new FileInputStream(file.toString());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Log.e("getAttentionJsonData", "loadData>>>>readObject前>>>>>>>>try>>>>>>>>>>>>>" + objectInputStream.toString());
            ArrayList<AttentBean.DataBean> data = (ArrayList<AttentBean.DataBean>) objectInputStream.readObject();
            Log.e("getAttentionJsonData", "loadData>>>>>>>>>>>>try>>>>>>>>>>>>>" + data.toString());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getAttentionJsonData", "loadData>>>>>>>catch>>>>>>>>>>>>>" + e.getMessage());
        } finally {
        }
        return null;
    }
//        FileInputStream in = null;
//        BufferedReader reader = null;
//        StringBuilder sb = new StringBuilder();
//        String attentFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/tempcache/" + "EkAttentModel";
//        try
//        {
//            File file = new File(attentFilePath);
//            in = new FileInputStream(file);
////            in = openFileInput(file);
//            reader = new BufferedReader(new InputStreamReader(in));
//            String line = "";
//            while ((line = reader.readLine()) != null)
//            {
//                sb.append(line);
//            }
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            if (reader != null)
//            {
//                try
//                {
//                    reader.close();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return sb.toString();
//    }
}
