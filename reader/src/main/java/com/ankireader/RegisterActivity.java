package com.ankireader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/25.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mRegister;

    private String registerUsername;
    private String registerEmail;
    private String registerPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        mUsernameView = (EditText) findViewById(R.id.register_uesername);
        mEmailView = (EditText) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mRegister = (Button) findViewById(R.id.register_button);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private void attemptRegister() {
        registerUsername = mUsernameView.getText().toString();
        registerEmail = mEmailView.getText().toString();
        registerPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        if (TextUtils.isEmpty(registerUsername)){
            mUsernameView.setError("用户名/手机号不能为空");
            cancel = true;
        }
        if (TextUtils.isEmpty(registerEmail)){
            mEmailView.setError("邮箱不能为空");
            cancel = true;
        }
        if(TextUtils.isEmpty(registerPassword)){
            mPasswordView.setError("密码不能为空");
            cancel = true;
        }

        if (!cancel){
            Map<String, String> map = new HashMap<>();
            map.put("honeyname", registerUsername);
//            map.put("email", registerEmail);
            map.put("tele", registerEmail);
            map.put("password", registerPassword);
            String url = "http://192.168.1.13/anki/index.php/Home/App/regist/";
            ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    Log.e("result>>>>>>>>>>>>", result);
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
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
