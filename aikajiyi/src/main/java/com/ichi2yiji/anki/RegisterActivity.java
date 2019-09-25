package com.ichi2yiji.anki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
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
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;

import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/11/25.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    public final static int REGIST_REQUEST = 1;
    private static final String TAG = "RegisterActivity";

    @Bind(R.id.register_tele_text)
    EditText teleText;
    @Bind(R.id.register_pass_text)
    EditText passText;
    @Bind(R.id.register_code_text)
    EditText codeText;
    @Bind(R.id.register_sendCode_btn)
    Button sendCode;
    @Bind(R.id.register_regist_btn)
    Button regist;
    /**
     * 返回
     **/
    @Bind(R.id.rl_register_back_img)
    RelativeLayout rl_register_back_img;
    @Bind(R.id.register_back_img)
    ImageView backImg;

    @Bind(R.id.register_pass_show)
    ImageView pwdShow;
    /**
     * 是否同意用户注册协议  true:同意 false:不同意
     **/
    @Bind(R.id.register_agree_img)
    Button agreeImg;
    /**
     * 跳转用户协议页面
     **/
    @Bind(R.id.register_protocol_text)
    TextView protocolText;


    private boolean flag = false;
    private String verifyCode = null;
    private TimeCount time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        setContentView(R.layout.register_webview);
        ButterKnife.bind(this);
        time = new TimeCount(60000, 1000);
        initView();
        setListener();
    }

    private void initView() {
        // teleText = (EditText)findViewById(R.id.register_tele_text);
        //passText = (EditText)findViewById(R.id.register_pass_text);
        passText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //codeText = (EditText)findViewById(R.id.register_code_text);
        //sendCode = (Button)findViewById(R.id.register_sendCode_btn);
        //regist = (Button)findViewById(R.id.register_regist_btn);
        //backImg = (ImageView)findViewById(R.id.register_back_img);
        //pwdShow = (ImageView)findViewById(R.id.register_pass_show);
        // agreeImg = (Button) findViewById(R.id.register_agree_img);
        // protocolText = (TextView)findViewById(R.id.register_protocol_text);
    }

    private void setListener() {
        agreeImg.setOnClickListener(this);
        rl_register_back_img.setOnClickListener(this);
        backImg.setOnClickListener(this);
        regist.setOnClickListener(this);
        sendCode.setOnClickListener(this);
        pwdShow.setOnClickListener(this);
        protocolText.setOnClickListener(this);
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void attemptGetVerifyCode(String phone) {
        if (!isPhone(phone)) {
            ToastAlone.showShortToast("手机号码格式不正确");
            return;
        }
        time.start();
        Map<String, String> map = new HashMap<>();
        map.put("tele", phone);
        ZXUtils.Post(Urls.URL_APP_GET_CODE, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "attemptGetVerifyCode>>>>>>>onSuccess>>>" + result);
                JSONObject jsonObject;
                String codeFromResult = null;
                try {
                    jsonObject = new JSONObject(result);
                    codeFromResult = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                verifyCode = codeFromResult;
                Log.e(TAG, "attemptGetVerifyCode>>>>>>>verifyCode>>>" + verifyCode);
                if (NumberUtils.toInt(verifyCode) == 0) {
                    ToastAlone.showShortToast(verifyCode);
                }
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

    /**
     * 注册账号
     *
     * @param tele
     * @param password
     * @param verifycode
     */
    private void attemptRegister(final String tele, final String password, String verifycode) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String clientid = pref.getString("CLIENTID", " ");
        Map<String, String> map = new HashMap<>();
        map.put("tele", tele);
        map.put("client_type", String.valueOf(1));
        map.put("password", password);
        map.put("tele_code", verifycode);
        map.put("client_id ", clientid);
        Log.e("tele",tele);
        Log.e("client_type",String.valueOf(1));
        Log.e("tele_code",verifycode);
        Log.e("client_id",clientid);

        ZXUtils.Post(Urls.URL_APP_REGIST, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "result>>>>>>>>>onSuccess>>>" + result);
                int code = 0;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 1000) {
                    // 激活账号
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("client_id", clientid);
//                    map.put("tele", tele);
//                    ZXUtils.Post(Urls.URL_APP_GET_CLIENT_ID, map, new Callback.CommonCallback<String>() {
//
//                        @Override
//                        public void onSuccess(String result) {
//                            Log.e("POST--clientid", "onSuccess" + result);
//                            ToastAlone.showShortToast("账号激活成功");
//                        }
//                        @Override
//                        public void onError(Throwable ex, boolean isOnCallback) {
//                            Log.e("POST--clientid", "onError" + ex);
//                            ToastAlone.showShortToast("账号激活失败");
//                        }
//
//                        @Override
//                        public void onCancelled(CancelledException cex) {
//
//                        }
//
//                        @Override
//                        public void onFinished() {
//
//                        }
//                    });
                    ToastAlone.showShortToast("注册成功,请登录");

                    //此处判断是否是从欢迎页面的注册按钮点击跳至此页面的，如果是，则此页面结束后需启动登录页面
                    Intent intent = getIntent();
                    if (intent.getBooleanExtra("IntentFromWelcomeActivity", false)) {
                        Intent i_1 = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i_1);
                        overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                    } else {
                        Intent i_2 = new Intent();
                        i_2.putExtra("TeleFromRegister", tele);
                        i_2.putExtra("passwordFromRegister",password);
                        setResult(RESULT_OK, i_2);
                    }
                    RegisterActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);

                } else {
                    ToastAlone.showShortToast("手机号错误，请重新输入");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError>>>>>>>>>onError>>>" + ex.toString());
                ToastAlone.showShortToast("网络异常，请稍后再试");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_agree_img:
                // 同意用户协议
                isAgreeProtoco();
                break;
            case R.id.rl_register_back_img:
                // 返回
                RegisterActivity.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                break;
            case R.id.register_regist_btn:
                // 注册
//                Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show();
//                Intent i_2 = new Intent();
//                i_2.putExtra("TeleFromRegister", "seis");
//                i_2.putExtra("passwordFromRegister","123456");
//                setResult(RESULT_OK, i_2);
//                RegisterActivity.this.finish();
//                overridePendingTransition(R.anim.out_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                register();
                break;
            case R.id.register_sendCode_btn:
                // 获取验证码
                attemptGetVerifyCode(teleText.getText().toString());
                break;
            case R.id.register_pass_show:
                // 显示与隐藏密码
                showOrHidePwd();
                break;

            case R.id.register_protocol_text:
                // 跳转用户协议
                Intent intent = new Intent(RegisterActivity.this, ProtocolDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
                break;
        }

    }

    /**
     * 显示与隐藏密码
     */
    private void showOrHidePwd() {
        int type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        if (passText.getInputType() == type) {
            passText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passText.setSelection(passText.getText().length());        //把光标设置到当前文本末尾
            pwdShow.setImageResource(R.drawable.invisible);
        } else {
            passText.setInputType(type);
            passText.setSelection(passText.getText().length());
            pwdShow.setImageResource(R.drawable.visible);
        }
    }

    private void register() {
        String tele = teleText.getText().toString();
        String password = passText.getText().toString();
        String code = codeText.getText().toString();
        if (tele.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isPhone(tele)) {
            Toast.makeText(RegisterActivity.this, "手机号输入不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }/*if(code.isEmpty()){
            Toast.makeText(RegisterActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else if(verifyCode!=null&&!verifyCode.equals(code)){
            Toast.makeText(RegisterActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (!flag) {
            ToastAlone.showShortToast("请勾选用户协议");
            return;
        }
        attemptRegister(tele, password, code);
    }

    /**
     * 是否同意用户协议
     */
    private void isAgreeProtoco() {
        if (flag) {
            // 不同意
            agreeImg.setBackgroundResource(R.drawable.disagree);
            flag = false;
        } else {
            // 同意
            agreeImg.setBackgroundResource(R.drawable.protocol);
            flag = true;
        }
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (sendCode != null) {
                sendCode.setBackgroundColor(Color.parseColor("#B6B6D8"));
                sendCode.setClickable(false);
                sendCode.setText("(" + millisUntilFinished / 1000 + ")重新发送");
            }
        }

        @Override
        public void onFinish() {
            if (sendCode != null) {
                sendCode.setText("重新获取验证码");
                sendCode.setClickable(true);
                sendCode.setBackgroundColor(Color.parseColor("#4EB84A"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

