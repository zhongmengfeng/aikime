package com.ichi2yiji.anki.activity.ek;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.anki.features.login.UserTypeActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.CommonUtils;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.common.SPConstants;
import com.ichi2yiji.common.SettingUtil;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;


public class EKLoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher, View.OnClickListener {
    private static final String TAG = "EKLoginActivity";
    private RelativeLayout iv_ek_activity_login_back;
    private ImageView iv_ek_activity_login_img;
    private EditText et_ek_activity_login_username;
    private EditText et_ek_activity_login_password;
    private TextView tv_ek_activity_login_forgetpass;
    private TextView tv_ek_activity_login_login;

    private ImageView iv_ek_activity_login_ankilogin;
    private ImageView iv_ek_activity_login_wxlogin;
    private ImageView iv_ek_activity_login_qqlogin;
    private ImageView iv_ek_activity_login_wblogin;
    private RelativeLayout rl_ek_activity_login_regist;
    private AnimatedVectorDrawable anim1;
    private AnimatedVectorDrawable anim2;
    private AnimatedVectorDrawable anim3;
    private AnimatedVectorDrawable anim3_back1;
    private AnimatedVectorDrawable anim_judge;
    private AnimatedVectorDrawable anim3_back1_no_pass;
    private AnimatedVectorDrawable anim3_no_pass;
    private boolean isGoBack;
    private boolean isShowTrue;
    private boolean isPass;
    private String clientid;
    private String mem_id;
    private long load_personal_info_times;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错
        setContentView(R.layout.ek_activity_login);
        ApplyTranslucency.applyKitKatTranslucency(this);

        init();
        initView();

    }

    private void init() {
        clientid = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "CLIENTID", "");
    }

    private void initView() {
        iv_ek_activity_login_back = ((RelativeLayout) findViewById(R.id.iv_ek_activity_login_back));
        iv_ek_activity_login_img = ((ImageView) findViewById(R.id.iv_ek_activity_login_img));
        et_ek_activity_login_username = ((EditText) findViewById(R.id.et_ek_activity_login_username));
        et_ek_activity_login_password = ((EditText) findViewById(R.id.et_ek_activity_login_password));
        tv_ek_activity_login_forgetpass = ((TextView) findViewById(R.id.tv_ek_activity_login_forgetpass));
        tv_ek_activity_login_login = ((TextView) findViewById(R.id.tv_ek_activity_login_login));
        iv_ek_activity_login_ankilogin = ((ImageView) findViewById(R.id.iv_ek_activity_login_ankilogin));
        iv_ek_activity_login_wxlogin = ((ImageView) findViewById(R.id.iv_ek_activity_login_wxlogin));
        iv_ek_activity_login_qqlogin = ((ImageView) findViewById(R.id.iv_ek_activity_login_qqlogin));
        iv_ek_activity_login_wblogin = ((ImageView) findViewById(R.id.iv_ek_activity_login_wblogin));
        rl_ek_activity_login_regist = ((RelativeLayout) findViewById(R.id.rl_ek_activity_login_regist));
        anim1 = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim1);
        anim2 = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim2);
        anim3 = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim3);
        anim3_back1 = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim3_back1);
        anim3_back1_no_pass = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim3_back1_no_pass);
        anim3_no_pass = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim3_no_pass);
        anim_judge = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim_judge);
        // 设置焦点变化的监听
        et_ek_activity_login_username.setOnFocusChangeListener(this);
        et_ek_activity_login_password.setOnFocusChangeListener(this);
        // 文本变化的监听
        et_ek_activity_login_username.addTextChangedListener(this);


        tv_ek_activity_login_login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ek_activity_login_login:
                login();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_ek_activity_login_username:
                if (hasFocus) {
                    if (isGoBack) {
                        if (isPass) {
                            isGoBack = false;
                            iv_ek_activity_login_img.setImageDrawable(anim3_back1);
                            anim3_back1.start();
                        } else {
                            isGoBack = false;
                            iv_ek_activity_login_img.setImageDrawable(anim3_back1_no_pass);
                            anim3_back1_no_pass.start();
                        }
                    } else {
                        iv_ek_activity_login_img.setImageDrawable(anim1);
                        anim1.start();
                    }
                }

                break;
            case R.id.et_ek_activity_login_password:
                if (hasFocus) {
                    isGoBack = true;
                    if (isPass){
                        iv_ek_activity_login_img.setImageDrawable(anim3);
                        anim3.start();
                    }else {
                        iv_ek_activity_login_img.setImageDrawable(anim3_no_pass);
                        anim3_no_pass.start();
                    }

                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!TextUtils.isEmpty(editable) && et_ek_activity_login_username.getText().toString().equals("123")) {
            isPass = true;
            iv_ek_activity_login_img.setImageDrawable(anim2);
            anim2.start();
            isShowTrue = true;
        } else if (isShowTrue) {
            isPass = false;
            iv_ek_activity_login_img.setImageDrawable(anim_judge);
            anim_judge.start();
            isShowTrue = false;
        }
    }

    /**
     * 普通登录
     */
    public void login() {
        String username = et_ek_activity_login_username.getText().toString();
        String pass = et_ek_activity_login_password.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastAlone.showShortToast("用户名不能为空");
            return;
        } else if (!CommonUtils.isHoneyname(username) && CommonUtils.isPhone(username) && !CommonUtils.isEmail(username)) {
            ToastAlone.showShortToast("用户名输入不正确");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            ToastAlone.showShortToast("密码不能为空");
            return;
        }
        attemptLogin(username, pass);
    }

    /**
     * 普通登录
     *
     * @param username
     * @param password
     */
    public void attemptLogin(final String username, final String password) {
        Map<String, String> map = new HashMap<>();
        map.put("tele", username);
        map.put("client_id", this.clientid);
        map.put("password", password);
        map.put("client_type", String.valueOf(1));
        Log.e(TAG, "LoginUI>>>>>>>>>>>>>map: " + map);
        ZXUtils.Post(Urls.URL_APP_LOGIN, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "普通登录：onSuccess>>>>>>>>>>>" + result);
                //做个标记,标明是从登录窗口登录过来的
                SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "isLoginFromWindow", true);

                LogUtil.e("onSuccess: result = " + result);
                int code = 0;
                String message = null;
                String version = null;
                String is_update = null;

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("code");
                    /**
                     * 6.6
                     * code == 1的时候，登陸錯誤
                     */
                    if (code == 1) {
                        String data = jsonObject.getString("data");
                        LogUtil.e("onSuccess: data = " + data);
                        ToastAlone.showShortToast(data);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 1000) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        JSONArray data = jsonObject.getJSONArray("data");
                        mem_id = data.getJSONObject(0).getString("mem_id");
                        Log.e("mem_id", mem_id);
                        message = data.getJSONObject(0).getString("msg");
                        version = data.getJSONObject(0).getString("version");
                        is_update = data.getJSONObject(0).getString("is_update");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_USER_NAME, username);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_USER_PASSWORD, password);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_MEM_ID, mem_id);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_VERSION, version);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_IS_UPDATE, is_update);


                    loadPersonalInfo(false);
//                    loadPersonalInfo("2", false);
//                    loadPersonalInfo("3", false);
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    if (!isFromSildingMenu) {
//                        Intent intent = new Intent(EKLoginActivity.this, AikaActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
//                        finish();
//                    }
                } else if (code == 2000) {
                    // 新用户,选择专业,邀请码和学历请选择一个
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        JSONArray data = jsonObject.getJSONArray("data");
                        mem_id = data.getJSONObject(0).getString("mem_id");
                        // JSONObject data = jsonObject.getJSONObject("data");
                        // LoginActivity.this.mem_id = data.getString("mem_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(EKLoginActivity.this, UserTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isFromThirdLogin", false);
                    bundle.putString("mem_id", mem_id);
                    bundle.putString("client_type", "1");
                    bundle.putString("client_id", clientid);
                    bundle.putString("userName", username);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "手机号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplicationContext(), "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(EKLoginActivity.this, AikaActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
//                finish();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param isFromThirdLogin
     */
    private void loadPersonalInfo(final boolean isFromThirdLogin) {

//        saveLocalCache();

        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
//        map.put("type_id", type_id);
        ZXUtils.Post(Urls.URL_APP_MEMBER_MSG, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("huoqu", result);
                Log.e(TAG, "onSuccess>>>>>>>>>>loadPersonalInfo:  " + result);

                //返回个人信息后,就马上把个人信息保存在偏好设置文件的"TYPE_DEFAULT_CONFIG"中的"PersonalInfoResult1或2或3中"
                SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PersonalInfoResult1", result);

                load_personal_info_times++;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    /**
                     "data": {
                     "mem": {
                     "user_name": "f123456",
                     "email": "1419692004@qq.com",
                     "face": "http://q.qlogo.cn/qqapp/1105783665/FFC0D1F7C618710DE4FE43B42BF9AFD6/100",
                     "jifen": "355",
                     "jyz": "0",
                     "honeyname": "KD_小小曼",
                     "gender": "男",
                     "telephone": "18135673854",
                     "qq": "1419692005",
                     "school_id": "999",
                     "is_teacher": "0",
                     "member_level": "1",
                     "school_logo": "http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/58df4dcadf943.png",
                     "deck": "",
                     "class_ids": "6,3,8,11"
                     },

                     **/

                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject mem = data.getJSONObject("mem");  //这里的mem是会员信息:包括
                    String schoolId = mem.getString("school_id");
                    if (!TextUtils.equals(schoolId, "0") && !TextUtils.equals(schoolId, "999")) {
                        // 既不是普通用户也不是开发人员,而是合作机构用户
                        SettingsBean settings = SettingUtil.getSettings();
                        settings.setHomePageStyle(3);
                        SettingUtil.upDateSettings(settings);
                    }
//                    if(TextUtils.equals(type_id,"1")){
                    Intent intent = new Intent(EKLoginActivity.this, EKMainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                    finish();
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                if (isFromThirdLogin) {
                    //如果是第三方登录的请求，则不启动广播
                } else {
                    //如果是正常的注册账号登录，则启动广播
                    if (load_personal_info_times == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //发送广播提醒更新侧边栏头像和用户名信息，延迟1s发送广播，确保主页面已经显示出来了
                                Intent i = new Intent("Update Username And User Head Image");
                                sendBroadcast(i);
                                Log.e("loadPersonalInfo", "Update Username And User Head Image ,Broadcast has sent! From normal login");
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

}
