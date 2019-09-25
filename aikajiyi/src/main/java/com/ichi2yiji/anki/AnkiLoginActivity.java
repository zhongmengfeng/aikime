package com.ichi2yiji.anki;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.utils.SPUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnkiLoginActivity extends AnkiActivity {
    private static final String TAG = "LoginActivity";
    private EditText accountText;
    private EditText passText;
    private Button loginBtn;
    private ImageView backImg;
    private String mem_id;
    private int load_personal_info_times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);//不加这句,setContentView的时候会报错
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anki_login);
        ApplyTranslucency.applyKitKatTranslucency(this);
//        applyKitKatAndLollipopTranslucency();
        AnkiDroidApp.activityManager.put("AnkiLoginActivity",this);
        initUI();
        back();
        ankiLogin();
    }
    public void ankiLogin(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = accountText.getText().toString();
                String pass = passText.getText().toString();
                if (username.isEmpty()){
                    Toast.makeText(AnkiLoginActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!isHoneyname(username)&& isPhone(username)&&!isEmail(username)){
                    Toast.makeText(AnkiLoginActivity.this,"用户名输入不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.isEmpty()){
                    Toast.makeText(AnkiLoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                attemptLogin(username,pass);
            }
        });
    }
    public void initUI(){
        accountText = (EditText)findViewById(R.id.ankilogin_account_text);
        passText = (EditText)findViewById(R.id.ankilogin_pass_text);
        passText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginBtn = (Button)findViewById(R.id.ankilogin_login_btn);
        backImg = (ImageView)findViewById(R.id.ankilogin_back);
    }

    public void back(){
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnkiLoginActivity.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });
    }
    // 验证是否为昵称
    private boolean isHoneyname(String honeyname) {
        if (honeyname == null || honeyname.length() == 0) {
            return false;
        }
        int len = 0;
        char[] honeychar = honeyname.toCharArray();
        for (int i = 0; i < honeychar.length; i++) {
            if (isChinese(honeychar[i])) {
                len += 2;
            } else {
                len += 1;
            }
        }
        if (len < 4 || len > 18) {
            //"正确的昵称应该为\n1、4-18个字符\n2、2-6个汉字\n3、不能是邮箱和手机号"
            return false;
        }
        return true;
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    // 判断是否为手机号
    private boolean isPhone(String inputText) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");//需新增号段181等
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    // 判断格式是否为email
    private boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public void attemptLogin(final String username, final String password) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String clientid = pref.getString("CLIENTID","");
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

        Map<String, String> map = new HashMap<>();
        //判断输入的是邮箱、手机号还是昵称
//        if (isEmail(username)){
//            map.put("email", username);
//        }else if (isPhone(username)){
//            map.put("tele", username);//此处对手机号的的判断有bug，如181等新号段未添加，容易导致判断成昵称，需后期修正
//        }else if(isHoneyname(username)) {
//            map.put("honeyname", username);
//
//        }
        map.put("tele", username);
        map.put("client_id", clientid);
        map.put("password", password);
        map.put("client_type", String.valueOf(1));


//        String url = "http://192.168.1.13/ankioss/index.php/Home/App/login/";//url中不能有www
        String url = AnkiDroidApp.BASE_DOMAIN +"Home/App/login/";//url中不能有www


        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                //做个标记,标明是从登录窗口登录过来的
                SPUtil.setPreferences(SPUtil.TYPE_WELCOME_CONFIG,"isLoginFromWindow", true);

                Log.e(TAG,"艾卡登录：onSuccess>>>>>>>>>>>" + result);
                int code = 0 ;
                String message = null;

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.e("jsonObject",  jsonObject.toString());
                    code = jsonObject.getInt("code");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("jsonArray",  jsonArray.toString());
                    mem_id = jsonArray.getJSONObject(0).getString("mem_id");
                    message = jsonArray.getJSONObject(0).getString("msg");

                    Log.e("jsonparse", "code is "+ code);
                    Log.e("jsonparse", "mem_id is "+ mem_id);
                    Log.e("jsonparse", "message is "+ message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
                if(code == 1000){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("USERNAME", username);
                    editor.putString("PASSWORD", password);
                    editor.putString("MEM_ID", mem_id);
                    editor.commit();

                    loadPersonalInfo("1");
                    loadPersonalInfo("2");
                    loadPersonalInfo("3");


                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, DeckPicker.class);
                    /*if(!isFromSildingMenu){
                        Intent intent = new Intent(LoginActivity.this, AikaActivity.class);
                        startActivity(intent);
                        //dx   add
                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                    }*/
                    finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }else{
                    Toast.makeText(getApplicationContext(), "手机号或密码错误", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("attemptLogin","onError>>>>>>>>>>>>"+ ex.toString());
                Toast.makeText(getApplicationContext(), "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AnkiLoginActivity.this, AikaActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                Intent intent = new Intent(AnkiLoginActivity.this,AikaActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
            }
        });
    }
    private void loadPersonalInfo(final String type_id) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//        String clientid = pref.getString("CLIENTID","");
        Map<String, String> map = new HashMap<>();
        map.put("mem_id", mem_id);
        map.put("type_id", type_id);


//        String url = "http://192.168.1.13/ankioss/index.php/Home/App/memberMsg/";
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/memberMsg/";
        com.ankireader.util.ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("result>>>PersonalCenter", type_id +">>>>>>>>>>>"+ result);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("PersonalInfoResult"+ type_id, result);
                editor.commit();
                load_personal_info_times++;
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
                if(load_personal_info_times == 1){
                    //发送广播提醒更新侧边栏头像和用户名信息
                    Intent i = new Intent("Update Username And User Head Image");
                    sendBroadcast(i);
                    Log.e("loadPersonalInfo", "Update Username And User Head Image ,Broadcast has sent!");
                }


            }
        });
    }
    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatAndLollipopTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            mTintManager.setStatusBarTintResource(R.color.aika_theme);//通知栏所需颜色
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initState();
        }

    }

    @TargetApi(19)  //Android4.4
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  //Android5.0
    private void initState() {
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
