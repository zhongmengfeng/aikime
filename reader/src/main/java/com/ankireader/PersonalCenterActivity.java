package com.ankireader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/25.
 */

public class PersonalCenterActivity extends AppCompatActivity implements View.OnClickListener {

    private String mem_id;
    private String clientid;
    private SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personalcenter);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mem_id = pref.getString("MEM_ID","");
        clientid = pref.getString("CLIENTID","");

        loadPersonalInfo();

        RelativeLayout my_information = (RelativeLayout) findViewById(R.id.my_information);
        RelativeLayout my_download = (RelativeLayout) findViewById(R.id.my_download);
        RelativeLayout my_read = (RelativeLayout) findViewById(R.id.my_read);
        RelativeLayout my_points = (RelativeLayout) findViewById(R.id.my_points);
        my_information.setOnClickListener(this);
        my_download.setOnClickListener(this);
        my_read.setOnClickListener(this);
        my_points.setOnClickListener(this);

    }

    private void loadPersonalInfo() {
        Map<String, String> map = new HashMap<>();

        map.put("mem_id", mem_id);
        map.put("type_id", String.valueOf(1));
        map.put("client_id", clientid);

        String url = "http://192.168.1.13/ankioss/index.php/Home/App/memberMsg/";
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("result>>>PersonalCenter", result);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("PersonalInfoResult", result);
                editor.commit();

                int code = 0;
                JSONObject dataObject;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("code");
                    dataObject = jsonObject.getJSONObject("data");
                    if (code == 1000) {
                        int id = dataObject.getJSONObject("member").getInt("id");
                        String email = dataObject.getJSONObject("member").getString("email");
                        String password = dataObject.getJSONObject("member").getString("password");
                        String face = dataObject.getJSONObject("member").getString("face");
                        String addtime = dataObject.getJSONObject("member").getString("addtime");
                        String email_code = dataObject.getJSONObject("member").getString("email_code");
                        int jifen = dataObject.getJSONObject("member").getInt("jifen");
                        int jyz = dataObject.getJSONObject("member").getInt("jyz");
                        String honeyname = dataObject.getJSONObject("member").getString("honeyname");
                        String name = dataObject.getJSONObject("member").getString("name");
                        String company = dataObject.getJSONObject("member").getString("company");
                        String telephone = dataObject.getJSONObject("member").getString("telephone");
                        String qq = dataObject.getJSONObject("member").getString("qq");

                        List<String[]> paizus_list = new ArrayList<String[]>();
                        JSONArray paizusArray = dataObject.getJSONArray("paizus");
                        for (int i = 0; i < paizusArray.length(); i++) {
                            String[] paizus_info = new String[2];
                            JSONObject paizusObject = paizusArray.getJSONObject(i);
                            int goods_id = paizusObject.getInt("goods_id");
                            String goods_name = paizusObject.getString("goods_name");
                            paizus_info[0] = String.valueOf(goods_id);
                            paizus_info[1] = goods_name;
                            paizus_list.add(paizus_info);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("PersonalCenter", ">>>>>>>" + e.toString());
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("onError>>>>>>>>>>>>", ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.my_information) {
            Toast.makeText(this, "跳转至我的资料页面", Toast.LENGTH_SHORT).show();

        } else if (i == R.id.my_download) {
            Intent intent = new Intent(PersonalCenterActivity.this, DownloadDecksActivity.class);
            startActivity(intent);

        } else if (i == R.id.my_read) {
            Toast.makeText(this, "跳转至我的阅读页面", Toast.LENGTH_SHORT).show();

        } else if (i == R.id.my_points) {
            Toast.makeText(this, "跳转至我的积分页面", Toast.LENGTH_SHORT).show();

        }

    }
}



