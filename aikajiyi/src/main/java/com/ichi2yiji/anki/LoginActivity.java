package com.ichi2yiji.anki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;
import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.bean.AttentBean;
import com.ichi2yiji.anki.bean.DistributeBean;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.anki.bean.SharedDecksBean;
import com.ichi2yiji.anki.features.login.UserTypeActivity;
import com.ichi2yiji.anki.util.CommonUtils;
import com.ichi2yiji.common.SPConstants;
import com.ichi2yiji.common.SettingUtil;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.libanki.FileUtils;
import com.ichi2yiji.utils.GsonUtil;
import com.ichi2yiji.utils.SPUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/11/24.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "LoginActivity";
    @Bind(R.id.login_back)
    ImageView backImg;
    @Bind(R.id.login_username_text)
    EditText userNameText;
    @Bind(R.id.login_pass_text)
    EditText passText;
    @Bind(R.id.login_toRegist_text)
    TextView regist;
    @Bind(R.id.login_forgetpass_text)
    TextView forgetPwd;
    @Bind(R.id.login_login_btn)
    Button loginBtn;
    @Bind(R.id.tv_login_anki)
    TextView tvLoginAnki;
    @Bind(R.id.tv_login_qq)
    TextView tvLoginQq;
    @Bind(R.id.tv_login_weixin)
    TextView tvLoginWeixin;
    @Bind(R.id.tv_login_weibo)
    TextView tvLoginWeibo;
    @Bind(R.id.rl_login_back)
    RelativeLayout rl_login_back;

    private String clientid;
    private String third_login_icon_url;
    private String third_login_user_name;
    private String mem_id;
    private boolean isFromSildingMenu;
    private int load_personal_info_times;
    private ImageView hideBtn;
    private int clickTimes = 5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mShareAPI = UMShareAPI.get(LoginActivity.this);
        isFromSildingMenu = intent.getBooleanExtra("isFromSildingMenu", false);//这个界面是否是从侧边栏的登录点击事件进来的?
        clientid = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "CLIENTID", "");
//        SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "CLIENTID", "");


        Log.e(TAG, "clientId: " + clientid);
        AnkiDroidApp.activityManager.put("LoginActivity", this);

        initUI();

        //设置隐藏按钮点击5次,进入测试账号;
        initHideButton();
    }

    private void initHideButton() {
        //当点击隐藏按钮五次后,则自动输入用户名和密码,供测试人员使用.
        hideBtn = (ImageView) findViewById(R.id.hide_btn);
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTimes--;
                if (clickTimes == 0) {
                    //Toast.makeText(getApplicationContext(),"当你点击三次之后才会出现", Toast.LENGTH_SHORT).show();
                    //自动设置用户名和密码
                    setUserAndPwd();
                    //删除Chaojiyiji文件夹
                    deleteAPPDir();
                    clickTimes = 5;
                }
            }
        });
    }

    private void deleteAPPDir() {
        String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileUtils.deleteAllFile(ROOT_PATH + "/Chaojiyiji");
    }

    //自动设置用户名和密码
    private void setUserAndPwd() {
        userNameText.setText("s1");
        passText.setText("123456");
        userNameText.setFocusable(true);
        userNameText.setSelection(userNameText.getText().length());
    }


    //初始化登录页面
    private void initUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        setContentView(R.layout.login_webview);
        ButterKnife.bind(this);  //它帮你找到各个View,避免重复使用findViewById之类的方法
        passText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvLoginAnki.setOnClickListener(this);
        tvLoginQq.setOnClickListener(this);
        tvLoginWeixin.setOnClickListener(this);
        tvLoginWeibo.setOnClickListener(this);
        backImg.setOnClickListener(this);
        regist.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        rl_login_back.setOnClickListener(this);

//        if(TextUtils.isEmpty(clientid)) {
//            Toast.makeText(this,"需要重启应用",Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent1 = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
//                    PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_ONE_SHOT);
//                    AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                    mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent); // 1秒钟后重启应用
//                    System.exit(0);
//                }
//            },1000);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_btn:
                // 普通登录
                login();
                break;
            case R.id.tv_login_anki:
                // 艾卡登录
                toAnkiLogin();
                break;
            case R.id.tv_login_qq:
                // qq登录
                qqLogin();
                break;
            case R.id.tv_login_weixin:
                // 微信登录
                wxLogin();
                break;
            case R.id.tv_login_weibo:
                // 微博登录
                wbLogin();
                break;
            case R.id.login_back:
                // 返回
                finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                break;
            case R.id.login_toRegist_text:
                // 注册
                registerUser();
                break;
            case R.id.login_forgetpass_text:
                // 忘记密码
                forgetPassword();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RegisterActivity.REGIST_REQUEST) {
                String tele = data.getStringExtra("TeleFromRegister");
                String pass = data.getStringExtra("passwordFromRegister");
                Log.e("LoginActivity", "TeleFromRetrieveRegister>>>>>>>>>>>>" + "tele: " + tele + "  pass:" + pass);
                if (!TextUtils.isEmpty(tele)) {
                    userNameText.setText(tele);
                }
                if (!TextUtils.isEmpty(pass)) {
                    passText.setText(pass);
                }
            }
        }
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
                    if (!isFromSildingMenu) {
                        Intent intent = new Intent(LoginActivity.this, AikaActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                        finish();
                    }
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
                    Intent intent = new Intent(LoginActivity.this, UserTypeActivity.class);
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
                Intent intent = new Intent(LoginActivity.this, AikaActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                finish();
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

        saveLocalCache();

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
                    Intent intent = new Intent(LoginActivity.this, AikaActivity.class);
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
//    /**
//     * 获取用户信息
//     * @param type_id   1为牌组，2为阅读，3为模考
//     * @param isFromThirdLogin
//     */
//    private void loadPersonalInfo(final String type_id, final boolean isFromThirdLogin) {
//        Map<String, String> map = new HashMap<>();
//        map.put("mem_id", mem_id);
////        map.put("type_id", type_id);
//        ZXUtils.Post(Urls.URL_APP_MEMBER_MSG, map, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//
//                Log.e(TAG,"onSuccess>>>>>>>>>>loadPersonalInfo:  " + result);
//
//                //返回个人信息后,就马上把个人信息保存在偏好设置文件的"TYPE_DEFAULT_CONFIG"中的"PersonalInfoResult1或2或3中"
//                SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG,"PersonalInfoResult"+ type_id, result);
//
//                load_personal_info_times++;
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    /**
//                    "data": {
//                        "mem": {
//                            "user_name": "f123456",
//                            "email": "1419692004@qq.com",
//                            "face": "http://q.qlogo.cn/qqapp/1105783665/FFC0D1F7C618710DE4FE43B42BF9AFD6/100",
//                            "jifen": "355",
//                            "jyz": "0",
//                            "honeyname": "KD_小小曼",
//                            "gender": "男",
//                            "telephone": "18135673854",
//                            "qq": "1419692005",
//                            "school_id": "999",
//                            "is_teacher": "0",
//                            "member_level": "1",
//                            "school_logo": "http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/58df4dcadf943.png",
//                            "deck": "",
//                            "class_ids": "6,3,8,11"
//                        },
//
//                    **/
//
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    JSONObject mem = data.getJSONObject("mem");  //这里的mem是会员信息:包括
//                    String schoolId = mem.getString("school_id");
//                    if(!TextUtils.equals(schoolId,"0") && !TextUtils.equals(schoolId,"999")){
//                        // 既不是普通用户也不是开发人员,而是合作机构用户
//                        SettingsBean settings = SettingUtil.getSettings();
//                        settings.setHomePageStyle(3);
//                        SettingUtil.upDateSettings(settings);
//                    }
//                    if(TextUtils.equals(type_id,"1")){
//                        Intent intent = new Intent(LoginActivity.this, AikaActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e("onError>>>>>>>>>>>>", ex.toString());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                if(isFromThirdLogin){
//                    //如果是第三方登录的请求，则不启动广播
//                }else {
//                    //如果是正常的注册账号登录，则启动广播
//                    if(load_personal_info_times == 1){
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //发送广播提醒更新侧边栏头像和用户名信息，延迟1s发送广播，确保主页面已经显示出来了
//                                Intent i = new Intent("Update Username And User Head Image");
//                                sendBroadcast(i);
//                                Log.e("loadPersonalInfo", "Update Username And User Head Image ,Broadcast has sent! From normal login");
//                            }
//                        }, 1000);
//                    }
//                }
//            }
//        });
//    }

    /**
     * 跳转注册界面
     */
    public void registerUser() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, RegisterActivity.REGIST_REQUEST);
        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
    }

    /**
     * 忘记密码
     */
    public void forgetPassword() {
        Intent intent = new Intent(LoginActivity.this, RetrievePasswordActivity.class);
        startActivityForResult(intent, RetrievePasswordActivity.RETRIEVEPASSWORD_REQUEST);
        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
    }

    /**
     * aika登录和普通登录 attemptLogin()
     * QQ登录和微博登录  requestLogin()
     */

    /**
     * 普通登录
     */
    public void login() {
        String username = userNameText.getText().toString();
        String pass = passText.getText().toString();
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
     * 艾卡登录
     */
    public void toAnkiLogin() {
        Intent intent = new Intent(LoginActivity.this, AnkiLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
    }

    /////////////////////////// 第三方登录板块
    UMShareAPI mShareAPI;

    /**
     * qq登录
     */
    public void qqLogin() {
        mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.e(TAG, "第三方登录第一步：onSuccess>>>>>>>>>>>" + "i: " + i + "   map: " + map);
                String uid = map.get("uid").substring(12);
                String name = map.get("screen_name");
                String iconurl = map.get("profile_image_url");
                String gender = map.get("gender");
                String login_type = "qq";
                third_login_icon_url = iconurl;
                third_login_user_name = name;
                if (!uid.equals("")) {
                    requestLogin(uid, name, iconurl, gender, login_type);
                } else {
                    Toast.makeText(getApplicationContext(), "网络异常，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    /**
     * 微信登录
     */
    public void wxLogin(){
        mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
//                Toast.makeText(getApplicationContext(),"授权开始",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();
                getPlatFormInfo();
//                // 获取uid
//                String uid = map.get("unionid");
//                if (!TextUtils.isEmpty(uid)) {
//                    Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();
//                    // uid不为空，获取用户信息
//                    getPlatFormInfo();
//                } else {
//                    Toast.makeText(LoginActivity.this, "授权失败...", Toast.LENGTH_LONG).show();
//                }
//                Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();
//                String content = "";
//                //注意遍历map的方法
//                for(Map.Entry<String, ?>  entry : map.entrySet()){
//                    content+=("       " +entry.getKey()+ "==>" +entry.getValue());
//                }
//                Log.e("map>>>>>>",">>>>>>>>>>>>>>"+ content);
//
//                String uid = map.get("unionid");
//                String name = map.get("screen_name");
//                String iconurl = map.get("profile_image_url");
//                String gender = map.get("gender");
//                if("0".equals(gender)){
//                    gender = "男";
//                }
//                String login_type = "wechat";
//                third_login_icon_url = iconurl;
//                third_login_user_name = name;
//                if(!uid.equals("")){
//                    requestLogin(uid, name, iconurl, gender, login_type);
//                }else{
//                    Toast.makeText(getApplicationContext(),"网络异常，请重试",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                Toast.makeText( getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
//                Toast.makeText( getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();
            }
        });
//        getPlatFormInfo();

    }

    private void getPlatFormInfo() {
        mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                String content = "";
                //注意遍历map的方法
                for(Map.Entry<String, ?>  entry : map.entrySet()){
                    content+=("       " +entry.getKey()+ "==>" +entry.getValue());
                }
                Log.e("map>>>>>>",">>>>>>>>>>>>>>"+ content);

                String uid = map.get("unionid");
                String name = map.get("screen_name");
                String iconurl = map.get("profile_image_url");
                String gender = map.get("gender");
                if("0".equals(gender)){
                    gender = "男";
                }
                String login_type = "wechat";
                third_login_icon_url = iconurl;
                third_login_user_name = name;
                if(!uid.equals("")){
                    requestLogin(uid, name, iconurl, gender, login_type);
                }else{
                    Toast.makeText(getApplicationContext(),"网络异常，请重试",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
            }
        });
    }

    /**
     * 微博登陆
     */
    public void wbLogin() {
        mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                String content = "";
                //注意遍历map的方法
                for (Map.Entry<String, ?> entry : map.entrySet()) {
                    content += ("       " + entry.getKey() + "==>" + entry.getValue());
                }
                Log.e("map>>>>>>", ">>>>>>>>>>>>>>" + content);

                String uid = map.get("id");
                String name = map.get("screen_name");
                String iconurl = map.get("profile_image_url");
                String gender = map.get("gender");
                String login_type = "sina";
                third_login_icon_url = iconurl;
                third_login_user_name = name;
                if (!uid.equals("")) {
                    requestLogin(uid, name, iconurl, gender, login_type);
                } else {
                    Toast.makeText(getApplicationContext(), "网络异常，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
//                        Toast.makeText(LoginActivity.this,"登录取消",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 第三方用户在后台注册
     *
     * @param uid
     * @param name
     * @param iconurl
     * @param gender
     * @param login_type
     */
    String user_names, passwords, versions, is_updates;

    private void requestLogin(String uid, final String name, final String iconurl, String gender, String login_type) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("name", name);
        map.put("iconurl", iconurl);
        map.put("gender", gender);
        map.put("login_type", login_type);
        ZXUtils.Post(Urls.URL_APP_THIDLOGO, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "第三方登录第二步：onSuccess>>>>>>>>>>>" + result);
                int code = 0;
                try {
                    JSONObject jo = new JSONObject(result);
                    code = jo.getInt("code");
                    mem_id = jo.getJSONObject("data").getString("mem_id");
                    user_names = name;
                    third_login_icon_url = iconurl;
                    passwords = jo.getJSONObject("data").getString("password");
                    versions = jo.getJSONObject("data").getString("version");
                    is_updates = jo.getJSONObject("data").getString("is_update");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 1000) {
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "USERNAME", user_names);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "PASSWORD", passwords);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", mem_id);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "VERSION", versions);
                    SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "IS_UPDATE", is_updates);

                    loadPersonalInfo(true);
//                    loadPersonalInfo("2", true);
//                    loadPersonalInfo("3", true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //发送广播提醒更新侧边栏头像和用户名信息，延迟1s发送广播，确保主页面已经显示出来了
                            Intent i = new Intent("Update Username And User Head Image");
                            i.putExtra("isFromThirdLogin", true);
                            i.putExtra("third_login_icon_url", third_login_icon_url);
                            i.putExtra("third_login_user_name", third_login_user_name);
                            sendBroadcast(i);
                            Log.e("postMsgToBackStage", "Update Username And User Head Image ,Broadcast has sent! From Third Login");
                        }
                    }, 1000);
                    //做个标记,标明是从登录窗口登录过来的
                    SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG, "isLoginFromWindow", true);
//                    postMsgToBackStage(mem_id, "1", clientid);
                } else if (code == 2000) {
                    // 新用户,选择专业,邀请码和学历请选择一个
                    Intent intent = new Intent(LoginActivity.this, UserTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isFromThirdLogin", true);
                    bundle.putString("mem_id", mem_id);
                    bundle.putString("client_type", "1");
                    bundle.putString("client_id", clientid);
                    bundle.putString("userName", third_login_user_name);
                    bundle.putString("third_login_icon_url", third_login_icon_url);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("requestLogin", "onError>>>>>>>>>>>" + ex);
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
     * 第三方授权成功后,那这些信息进行注册,注册后再次登录.
     *
     * @param mem_id
     * @param client_type
     * @param client_id
     */
    private void postMsgToBackStage(final String mem_id, final String client_type, final String client_id) {
//        Map<String, String> map = new HashMap<>();
//        map.put("mem_id", mem_id);
//        map.put("client_type", client_type);
//        map.put("client_id", client_id);
//
//        // String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/logo3/";
//        // 第三方授权后,注册,---然后进行的登录.
//        ZXUtils.Post(Urls.URL_APP_LOGO3, map, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.e(TAG,"第三方登录第三步：onSuccess>>>>>>>>>>>" + result);
//                int code = 0;
//                String data = "登录失败";
//                String user_name = null;
//                String password = null;
//                String version = null;
//                String is_update = null;
//
//                JSONObject jo = null;
//                try {
//                    jo = new JSONObject(result);
//                    code = jo.getInt("code");
//                    data = jo.getString("data");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if(code == 1000){
//                    try {
//                        user_name = jo.getJSONObject("data").getString("user_name");
//                        password = jo.getJSONObject("data").getString("password");
//                        version = jo.getJSONObject("data").getString("version");
//                        is_update = jo.getJSONObject("data").getString("is_update");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }else{
//                    ToastAlone.showShortToast(data);
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e("postMsgToBackStage","onError>>>>>>>>>>>" + ex);
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
    }

    private long exitTime = 0;

    /**
     * 重写回退键方法
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    private void saveLocalCache() {
        //请求我的关注接口,并持久化
        getAttentionJsonData();
        //请求分发作业接口,并持久化
        getDistributeJsonData();
        //请求共享牌组接口,并持久化
        getAddOrShakeDecksData();
    }

    public void getAttentionJsonData(){
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/myGuanzhu/";
        Map myMap = new HashMap<String,String>();
        myMap.put("mem_id", mem_id);
        ZXUtils.Post(url, myMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    List<AttentBean.DataBean> data = null;
                    AttentBean attentBean = GsonUtil.json2Bean(result, AttentBean.class);
                    int code = attentBean.getCode();
                    if(code == 1000){
                        data = attentBean.getData();
//                        String dataJsonStr = GsonUtil.createJson(data);
                        saveData(data, "EKAttentModel" + "_" + mem_id);

                    }else{
                        //标志符 让用户先登录
                        //canGoToAttentionPage = false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
        final String mem_id = pref.getString("MEM_ID", "");
        Log.e("getDistributeJsonData", ">>>onSuccess>>>" + "mem_id: " + mem_id);
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/Application/messageList/";
        Map map = new HashMap<String, String>();
        map.put("mem_id", mem_id);
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    DistributeBean distributeBean = GsonUtil.json2Bean(result, DistributeBean.class);
                    int code = distributeBean.getCode();
                    if (code == 1000) {
                        saveData(distributeBean, "EKDistributeModel" + "_" + mem_id);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getAddOrShakeDecksData() {
        HashMap<String, String> map = new HashMap<String, String>();
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        map.put("mem_id", memId);
        ZXUtils.Post(Urls.URL_APP_PICKER, map, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                SharedDecksBean sharedDecksBean = gson.fromJson(result, SharedDecksBean.class);
                int code = sharedDecksBean.getCode();
                SharedDecksBean.DataBean dataBean = sharedDecksBean.getData();
                if (code == 1000) {
                    saveData(dataBean, "EKShareDeckModel" + "_" + mem_id);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void saveData(Object object, String filename) {

        //存入数据
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator +"Chaojiyiji"+ File.separator + "tempcache" + File.separator + filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            Log.e("getAttentionJsonData", "file.getParentFile().mkdirs()>>>>>>>>>>   " + file);
        }

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
                Log.e("getAttentionJsonData", "file.createNewFile()>>>>>>>>>>   " + file);
            }
            Log.e("getAttentionJsonData", "异常前()>>>>>>>>>>   " + file);
            fileOutputStream = new FileOutputStream(file.toString());
            objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            Log.e("getAttentionJsonData", "objectOutputStream>>>>>>>>>>   " + objectOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("getAttentionJsonData", "objectOutputStream>>>>>>>>>>   " + e.getMessage());
        } finally{
            if (objectOutputStream!=null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream!=null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


//        FileOutputStream out = null;
//        BufferedWriter writer = null;
//        try
//        {
//            //MODE_PRIVATE:默认模式，表示当指定同样文件名的时候，所写的内容将会覆盖原文件中的内容
//            //MODE_APPEND:表示如果文件存在，就往文件里追加内容，不存在就创建
//
//            String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/tempcache/" + "EkAttentModel";
//            out = openFileOutput(file, Context.MODE_APPEND);
//            writer = new BufferedWriter(new OutputStreamWriter(out));
//            writer.write(data);
//            Log.e("LoginActivity", "saveData::try:: " + "IO流错误");
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            Log.e("LoginActivity", "saveData::catch:: " + "IO流错误");
//        }
//        finally
//        {
//            try {
//                if (writer != null) {
//                    Log.e("LoginActivity", "saveData::writer != null:: " + "IO流错误");
//
//                    writer.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


}






