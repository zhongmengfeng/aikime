package com.ichi2yiji.anki.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/11/28.
 */

public class PushReceiver extends BroadcastReceiver {
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    public static String url;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                Log.e("PushReceiver", "onReceive() bundle=" + bundle.toString());
                byte[] payload = bundle.getByteArray("payload");
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                int pageid = 1;//默认为牌组的推送
                if (payload != null || payload.length !=0){
                    String payloadata = new String(payload);
                    try {
                        JSONObject jsonObject = new JSONObject(payloadata);
                        url = jsonObject.getString("file_url");//推送过来的地址
                        pageid = jsonObject.getInt("type_id");//确定是阅读、牌组，还是模考的推送
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("onReceive---url", ">>>>>>>>>>>>>"+ url);
                    Log.e("onReceive---pageid", ">>>>>>>>>>>>>"+ pageid);
                    Log.e("onReceive---taskid", ">>>>>>>>>>>>>"+ taskid);
                    Log.e("onReceive---messageid", ">>>>>>>>>>>>>"+ messageid);
                }

                switch(pageid){
                    case 0://阅读的在线导入推送
                        if (url != null) {
                            editor.putString("URLTOREADER", url);
                            editor.commit();

                            Intent i = new Intent();
                            i.setAction("com.ankireader.ReceivedUrlFromPush");
                            i.putExtra("URL", url);
                            context.sendBroadcast(i);
                        }
                        break;
                    case 1://牌组的在线导入推送
                        if (url != null) {
                            editor.putString("URLTODECK", url);
                            editor.commit();

                            Intent i = new Intent();
                            i.setAction("com.ankideck.ReceivedUrlFromPush");
                            i.putExtra("URL", url);
                            context.sendBroadcast(i);
                        }
                        break;
                    case 2://模考的在线导入推送
                        break;
                    default:
                        break;
                }
//                //发送广播提醒主线程更新UI
//                if (url != null) {
//                    Intent i = new Intent();
//                    i.setAction("com.ankireader.ReceivedUrlFromPush");
//                    i.putExtra("URL", url);
//                    context.sendBroadcast(i);
//                }



//                // 获取透传数据
//                // String appid = bundle.getString("appid");
//                byte[] payload = bundle.getByteArray("payload");
//
//                String taskid = bundle.getString("taskid");
//                String messageid = bundle.getString("messageid");
//
//                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
//

                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                Log.e("onReceive---clientid", ">>>>>>>>>>>>>"+ cid);

                //向后台提交client_id

                editor.putString("CLIENTID", cid);
                editor.commit();
//                String mem_id = pref.getString("MEM_ID","");
//                String url = "http://192.168.1.13/ankioss/index.php/Home/App/memberMsg/";  //需后台提供
//                HashMap<String, String> map = new HashMap<>();
//                map.put("client_id", cid);
//                map.put("mem_id", mem_id);
//                map.put("type", String.valueOf(1));
//
//                ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
//
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.e("onReceive--clientid", "post Success!" + result);
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        Log.e("onReceive--clientid", "post Error!" + ex);
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });

                break;
            case PushConsts.GET_SDKONLINESTATE:
                boolean online = bundle.getBoolean("onlineState");
                Log.d("GetuiSdkDemo", "online = " + online);
                break;

            case PushConsts.SET_TAG_RESULT:
                String sn = bundle.getString("sn");
                String code = bundle.getString("code");

                String text = "设置标签失败, 未知异常";
                switch (Integer.valueOf(code)) {
                    case PushConsts.SETTAG_SUCCESS:
                        text = "设置标签成功";
                        break;

                    case PushConsts.SETTAG_ERROR_COUNT:
                        text = "设置标签失败, tag数量过大, 最大不能超过200个";
                        break;

                    case PushConsts.SETTAG_ERROR_FREQUENCY:
                        text = "设置标签失败, 频率过快, 两次间隔应大于1s";
                        break;

                    case PushConsts.SETTAG_ERROR_REPEAT:
                        text = "设置标签失败, 标签重复";
                        break;

                    case PushConsts.SETTAG_ERROR_UNBIND:
                        text = "设置标签失败, 服务未初始化成功";
                        break;

                    case PushConsts.SETTAG_ERROR_EXCEPTION:
                        text = "设置标签失败, 未知异常";
                        break;

                    case PushConsts.SETTAG_ERROR_NULL:
                        text = "设置标签失败, tag 为空";
                        break;

                    case PushConsts.SETTAG_NOTONLINE:
                        text = "还未登陆成功";
                        break;

                    case PushConsts.SETTAG_IN_BLACKLIST:
                        text = "该应用已经在黑名单中,请联系售后支持!";
                        break;

                    case PushConsts.SETTAG_NUM_EXCEED:
                        text = "已存 tag 超过限制";
                        break;

                    default:
                        break;
                }

                Log.d("GetuiSdkDemo", "settag result sn = " + sn + ", code = " + code);
                Log.d("GetuiSdkDemo", "settag result sn = " + text);
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }


}
