package com.ankireader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;



import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/11/24.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mLogin;
    private CheckBox mRemeberPass;
    private TextView mRegister;
    private TextView mForgetPass;

    private String mUsername;
    private String mPassword;
    private String mem_id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        mUsernameView = (EditText) findViewById(R.id.login_username);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mLogin = (Button) findViewById(R.id.login_button);
        mRemeberPass = (CheckBox) findViewById(R.id.remember_password);
        mRegister = (TextView) findViewById(R.id.login_register);
        mForgetPass = (TextView) findViewById(R.id.login_forgetpassword);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });


    }

    public void attemptLogin() {
        mUsername = mUsernameView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        /**
         * 遍历SharedPreferences的方法
         */
//        //读取所有数据
//        String content = "";
//        Map<String, ?> allContent = pref.getAll();
//        //注意遍历map的方法
//        for(Map.Entry<String, ?>  entry : allContent.entrySet()){
//          content+=("       " +entry.getKey()+ "==>" +entry.getValue());
//        }
//        Log.e("pref>>>>>>",">>>>>>>>>>>>>>"+ content);

        boolean cancel = false;
        if (TextUtils.isEmpty(mUsername)) {
            mUsernameView.setError("内容不能为空");
            cancel = true;
        }
        if (TextUtils.isEmpty(mPassword)) {
            mUsernameView.setError("内容不能为空");
            cancel = true;
        }
        if (!cancel) {
            Map<String, String> map = new HashMap<>();
            map.put("email", mUsername);
            map.put("password", mPassword);
            String url = "http://192.168.1.13/anki/index.php/Home/App/login/";//url中不能有www

            ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("resultLogin>>>>>>>", result);

                    int code = 0 ;
                    String message = null;

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.getInt("code");
                        mem_id = jsonObject.getString("mem_id");
                        message = jsonObject.getString("message");
                        Log.e("jsonparse", "code is"+code);
                        Log.e("jsonparse", "mem_id is"+mem_id);
                        Log.e("jsonparse", "message is"+message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(code == 1000){
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("USERNAME", mUsername);
                        editor.putString("PASSWORD", mPassword);
                        editor.putString("MEM_ID", mem_id);
                        editor.commit();


                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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

    }
}






