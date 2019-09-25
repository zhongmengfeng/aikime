package com.ichi2yiji.anki;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.bean.PayResult;
import com.ichi2yiji.anki.bean.WeixinBean;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.CommonUtils;
import com.ichi2yiji.anki.util.PayUtils;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.SPConstants;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import static android.util.TypedValue.COMPLEX_UNIT_PT;

/**
 * Created by ekar01 on 2017/6/29.
 */

public class PayOnlineActivity extends AppCompatActivity {

    public static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "PayOnlineActivity";
    private RelativeLayout rl_activity_payonline_back;
    private TextView tv_activity_payonline_orderprice;
    private TextView tv_activity_payonline_ordername;
    private TextView tv_activity_payonline_ordercount;
    private RadioGroup rg_activity_payonline_paytype;
    private RadioButton rb_activity_payonline_wxpay;
    private RadioButton rb_activity_payonline_alipay;
    private Button bt_activity_payonline_confirmpay;
    private int payStyle = 1;
    private String orderInfo;
    private WeixinBean weixinBean;
    private PayUtils payUtils;
    private String classname;
    private String total_course;
    private String price;
    private String memId;
    private String class_id;
    private Map<String, String> map;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payonline);
        ApplyTranslucency.applyKitKatTranslucency(this);

        init();
        initView();
    }

    private void init() {
        payUtils = PayUtils.getInstance();
        float heightPx = TypedValue.applyDimension(COMPLEX_UNIT_PT, 250, getResources().getDisplayMetrics());
        Log.e(TAG, "onCreate>>>>heightPx: " + heightPx);

        memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, SPConstants.DEF_CONFIG_MEM_ID, "");

        Intent intent = getIntent();
        classname = intent.getStringExtra("classname");
        class_id = intent.getStringExtra("class_id");
        total_course = intent.getStringExtra("total_course");
        price = intent.getStringExtra("price");

        map = new HashMap<>();
        map.put("mem_id", memId);
        map.put("class_id", class_id);
//        map.put("price", "0.01");
        map.put("price", price);
        map.put("pay_type", payStyle + "");
        map.put("spbill_create_ip", CommonUtils.getIPAddress(PayOnlineActivity.this));
        getOrderInfo(map);
    }

    private void initView() {
        rl_activity_payonline_back = (RelativeLayout) findViewById(R.id.rl_activity_payonline_back);
        tv_activity_payonline_orderprice = (TextView) findViewById(R.id.tv_activity_payonline_orderprice);
        tv_activity_payonline_ordername = (TextView) findViewById(R.id.tv_activity_payonline_ordername);
        tv_activity_payonline_ordercount = (TextView) findViewById(R.id.tv_activity_payonline_ordercount);
        rg_activity_payonline_paytype = (RadioGroup) findViewById(R.id.rg_activity_payonline_paytype);
        rb_activity_payonline_wxpay = (RadioButton) findViewById(R.id.rb_activity_payonline_wxpay);
        rb_activity_payonline_alipay = (RadioButton) findViewById(R.id.rb_activity_payonline_alipay);
        bt_activity_payonline_confirmpay = (Button) findViewById(R.id.bt_activity_payonline_confirmpay);

        tv_activity_payonline_orderprice.setText(price);
        tv_activity_payonline_ordername.setText(classname);
        tv_activity_payonline_ordercount.setText(total_course);

        rl_activity_payonline_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
            }
        });

        rg_activity_payonline_paytype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.rb_activity_payonline_wxpay:
                        //切换到微信支付按钮
                        payStyle = 1;

                        map.put("pay_type", "1");
                        map.put("spbill_create_ip", CommonUtils.getIPAddress(PayOnlineActivity.this));
                        getOrderInfo(map);
                        break;
                    case R.id.rb_activity_payonline_alipay:
                        //切换到支付宝按钮
                        payStyle = 2;

                        map.put("pay_type", "2");
                        getOrderInfo(map);
                        break;
                }
            }
        });

        /**
         * @"mem_id": mem_id,
         * @"class_id": class_id,
         * @"price": price,
         * @"pay_type": 微信1,支付宝2
         * 微信多了个参数:spbill_create_ip
         * class_ids='2001018,2001001,2001005
         */
        bt_activity_payonline_confirmpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击确认支付
                if (1 == payStyle) {
                    //微信支付
                    if(weixinBean == null){
                        return;
                    }
                    payUtils.payByWeixin(PayOnlineActivity.this, weixinBean);
                } else if (2 == payStyle) {
                    //支付宝支付
                    if(orderInfo == null){
                        return;
                    }
                    payUtils.payByAlipay(PayOnlineActivity.this, mHandler, orderInfo);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    Log.e(TAG, "onError>>>>result: " + resultStatus);

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(resultInfo);
                            String alipay_trade_app_pay_response = jsonObject1.getString("alipay_trade_app_pay_response");
                            JSONObject jsonObject2 = new JSONObject(alipay_trade_app_pay_response);
                            String out_trade_no = jsonObject2.getString("out_trade_no");
                            String trade_no = jsonObject2.getString("trade_no");
                            String timestamp = jsonObject2.getString("timestamp");

                            Map<String, String>  xmap = new HashMap<>();
                            xmap.put("mem_id", memId);
                            xmap.put("out_trade_no", out_trade_no);
                            xmap.put("trade_no", trade_no);
                            xmap.put("timestamp", timestamp);
                            updatePayState(xmap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                        Toast.makeText(PayOnlineActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayOnlineActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        }
    };

    private void updatePayState(Map<String, String> xmap) {
        ZXUtils.Post(Urls.UPDATE_ORDERMSG, xmap, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e(TAG, "onSuccess>>>>result: " + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 1000) {
//                        Toast.makeText(PayOnlineActivity.this, "成功信息上传给服务器了", Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = jsonObject.getString("data");
                        Toast.makeText(PayOnlineActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError>>>>result: " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void getOrderInfo(Map<String, String> map) {

        ZXUtils.Post(Urls.PAY_FOR_CLASS, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 1000) {
                        if (1 == payStyle) {
                            Gson gson = new Gson();
                            String data = jsonObject.getString("data");
                            weixinBean = gson.fromJson(data, WeixinBean.class);

                        } else if (2 == payStyle) {
                            orderInfo = jsonObject.getString("data");
                        }
                    } else {
                        String msg = jsonObject.getString("data");
                        Toast.makeText(PayOnlineActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onSuccess>>>>result: " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "onError>>>>result: " + ex.getMessage());
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
