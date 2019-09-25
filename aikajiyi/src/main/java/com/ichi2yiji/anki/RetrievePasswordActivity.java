package com.ichi2yiji.anki;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ankireader.util.ZXUtils;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.common.Urls;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/12.
 */

public class RetrievePasswordActivity extends AppCompatActivity {
    private static final String TAG = "RetrievePwActivity";
    //private WebView webView;
    private EditText teleText;
    private EditText newPassText;
    private EditText codeText;
    private Button sendCodeBtn;
    private Button submitBtn;
    private ImageView backImg;
    private RelativeLayout rl_retrieve_back_img;
    public static int RETRIEVEPASSWORD_REQUEST = 2;
    private TimeCount time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏
        setContentView(R.layout.retrievepassword_webview);
        initUI();
        time = new TimeCount(60000, 1000);
//        applyKitKatAndLollipopTranslucency();//设置沉浸式状态栏
        getVerifyCode();
        confirmModify();
        back();
        /**
         * WebView忘记密码页面
         */
        /*webView=(WebView)findViewById(R.id.retrievepassword_webview);
        webView.loadUrl("file:///android_asset/testMUI_login/Aika_retrieve_password.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:initFirWindow('"+null+"','"+null+"')");
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");*/

    }
    public void initUI(){
        teleText = (EditText)findViewById(R.id.retrieve_tele_text);
        newPassText = (EditText)findViewById(R.id.retrieve_pass_text);
        newPassText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        codeText = (EditText)findViewById(R.id.retrieve_code_text);
        sendCodeBtn = (Button)findViewById(R.id.retrieve_sendCode_btn);
        submitBtn = (Button)findViewById(R.id.retrieve_regist_btn);
        backImg = (ImageView)findViewById(R.id.retrieve_back_img);
        rl_retrieve_back_img = (RelativeLayout) findViewById(R.id.rl_retrieve_back_img);
    }

    /**
     * 点击“回退按钮，返回上一页面”
     */
    public void back(){
        rl_retrieve_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrievePasswordActivity.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });
    }
    public void getVerifyCode(){
        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=teleText.getText().toString();
                if (phone.isEmpty()){
                    Toast.makeText(RetrievePasswordActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
                }
                attemptGetVerifyCode(phone);
                time.start();
            }
        });
    }

    /**
     * 修改密码
     */
    public void confirmModify(){
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tele=teleText.getText().toString();
                String newPass=newPassText.getText().toString();
                String code=codeText.getText().toString();
                if (tele.isEmpty()){
                    Toast.makeText(RetrievePasswordActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!isPhone(tele)){
                    Toast.makeText(RetrievePasswordActivity.this,"手机号输入不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPass.isEmpty()){
                    Toast.makeText(RetrievePasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if (code.isEmpty()){
                    Toast.makeText(RetrievePasswordActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }else if(verifyCode!=null&&code.equals(verifyCode)){
                    Toast.makeText(RetrievePasswordActivity.this,"验证码输入不正确，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                //@TODO---6.3---startActivityForResult?
                retrievePassword(tele,newPass,code);
//                Intent intent=new Intent(RetrievePasswordActivity.this,LoginActivity.class);
//                startActivityForResult(intent,RetrievePasswordActivity.RETRIEVEPASSWORD_REQUEST);
//                intent.putExtra("TeleFromRetrievePassword",tele);
//                intent.putExtra("PassFromRetrievePassword",newPass);
//                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
            }
        });
    }
    // 判断是否为手机号
    private boolean isPhone(String inputText) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");//需新增号段181等
        Matcher m = p.matcher(inputText);
        return m.matches();
    }
    /**
     * JS调用原生的接口类，忘记密码
     */
    class MyObject {
       /* @JavascriptInterface
        public void getVerifyCode(String phone){
            attemptGetVerifyCode(phone);
        }*/

       /* @JavascriptInterface
        public void confirmModify(String tele, String password, String verifycode){
            retrievePassword(tele, password, verifycode);
        }*/

       /* @JavascriptInterface
        public void back(){
            RetrievePasswordActivity.this.finish();
            //dx  add
            RetrievePasswordActivity.this.overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }*/
    }

    private String verifyCode = null;
    private  void attemptGetVerifyCode(String phone){
        //尚需添加请求验证码的网络请求
        Map<String, String> map  = new HashMap<>();
        map.put("tele", phone);
//        String url = "http://192.168.1.13/ankioss/index.php/Home/App/SendCode/";
//        String url = "http://www.ankichina.net/Home/Application/appGetResetPwdCode/";
        ZXUtils.Post(Urls.APP_GET_RESET_PWD_CODE, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("str",result);
                Log.e(TAG, "attemptGetVerifyCode>>>>>>>onSuccess>>>"  + result);
                JSONObject jsonObject = null;
                String codeFromResult = null;
                try {
                    jsonObject = new JSONObject(result);
                    codeFromResult = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                verifyCode =  codeFromResult;
                Log.e(TAG, "attemptGetVerifyCode>>>>>>>verifyCode>>>"  + verifyCode);
                //webView.loadUrl("javascript:setVerifyCode('"+verifyCode+"')");
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "attemptGetVerifyCode>>>>>>>onError>>>" + ex.toString());
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });

    }

    private void retrievePassword(final String tele, String password, String verifycode){
        //尚需添加忘记密码的网络请求
        HashMap<String, String> map = new HashMap<>();
        map.put("tele", tele);
        map.put("code", verifycode);
        map.put("password", password);
//        String url = "http://192.168.1.13/ankioss/index.php/Home/App/updPass/";
//        String url = "http://www.ankichina.net/Home/Application/appResetPasswd/";
        ZXUtils.Post(Urls.APP_RESET_PASSWD, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("repasw",result);
                Log.e(TAG, "result>>>>>>>>>onSuccess>>>"+ result);
                int code = 0;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (code == 1000){
                    Toast.makeText(getApplicationContext(), "密码修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.putExtra("TeleFromRetrievePassword", tele);
                    setResult(RESULT_OK, i);
                    RetrievePasswordActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }else {
                    Toast.makeText(getApplicationContext(), "手机号错误，请重新输入", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "retrievePassword>>>>>>>onError>>>"  + ex.toString());
                Toast.makeText(getApplicationContext(), "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
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
     * Apply KitKat specific translucency.
     */
    private void applyKitKatAndLollipopTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            mTintManager.setStatusBarTintResource(R.color.login_color);//通知栏所需颜色
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

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeBtn.setBackgroundColor(Color.parseColor("#B6B6D8"));
            sendCodeBtn.setClickable(false);
            sendCodeBtn.setText("(" + millisUntilFinished / 1000 + ")可重新发送");
        }

        @Override
        public void onFinish() {
            sendCodeBtn.setText("重新获取验证码");
            sendCodeBtn.setClickable(true);
            sendCodeBtn.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }

    /**
     * 重写回退键方法
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

