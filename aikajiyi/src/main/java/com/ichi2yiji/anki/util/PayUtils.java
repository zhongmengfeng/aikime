package com.ichi2yiji.anki.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.ichi2yiji.anki.PayOnlineActivity;
import com.ichi2yiji.anki.bean.WeixinBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * Created by ekar01 on 2017/6/29.
 */

public class PayUtils {

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017061407488808";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    private static PayUtils payUtils;
    private PayReq req;

    private PayUtils(){

    }

    public static PayUtils getInstance(){
        if(payUtils == null){
            payUtils = new PayUtils();
        }
        return payUtils;
    }

    public void payByWeixin(PayOnlineActivity act, WeixinBean weixin) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(act, null);
        genPayReq(weixin);
        msgApi.registerApp(weixin.getAppid());
        msgApi.sendReq(req);
    }

    /**
     * 支付宝支付
     * @param activity
     * @param handler
     * @param orderinfo
     */
    public void payByAlipay(final Activity activity, final Handler handler, final String orderinfo) {

//        //支付宝授权
//        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
//            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialoginterface, int i) {
//                            //
//                            activity.finish();
//                        }
//                    })
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .show();
//            return;
//        }
//        /**
//         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//         *
//         * orderInfo的获取必须来自服务端；
//         */
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;



        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderinfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = PayOnlineActivity.SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     *要向微信支付端发送的参数
     */
    private void genPayReq(WeixinBean weixin) {
        req = new PayReq();
        req.appId = weixin.getAppid();
        req.partnerId = weixin.getPartnerid();
        req.prepayId = weixin.getPrepayid();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = weixin.getNoncestr();
        req.timeStamp = weixin.getTimestamp();

        req.sign = weixin.getSign();

    }

}
