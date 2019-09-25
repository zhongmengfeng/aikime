package com.ankireader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ankireader.Adapter.DownloadDecksAdapter;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/26.
 */

public class DownloadDecksActivity extends AppCompatActivity {

    private ListView listView;
    private DownloadDecksAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_downloaddecks);

        //从SharedPreferences中获得网络请求个人信息的返回值，对其进行解析，获得一个List
        List<String[]> paizus_list = new ArrayList<String[]>();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String personalInfoResult = pref.getString("PersonalInfoResult", "");
        try {
            JSONObject jsonObject = new JSONObject(personalInfoResult);
            JSONObject dataObject = jsonObject.getJSONObject("data");

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
        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView = (ListView) findViewById(R.id.listview_downloaddecks);
        myAdapter = new DownloadDecksAdapter(this, paizus_list);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "牌组：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
