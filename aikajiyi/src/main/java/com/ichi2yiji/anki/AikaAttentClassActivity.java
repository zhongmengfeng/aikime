package com.ichi2yiji.anki;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 关注班级
 */
public class AikaAttentClassActivity extends BaseActivity {
    private WebView webView;
    private String mem_id = null;
    private String class_id = null;
    private String in_class = null;
    String attentJSONData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aika_attent_class);
        ApplyTranslucency.applyKitKatTranslucency(this);

        mem_id = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        if (!TextUtils.isEmpty(mem_id)){
            getAttentionJsonData(mem_id);
        }else {
            Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
        }
        /**
         * WebView关注老师
         */
        webView=(WebView)findViewById(R.id.attent_class_webview);
//        webView.loadUrl("file:///android_asset/distribute_attention/attention.html");
//        webView.loadUrl("file:///android_asset/allWebView/attention0309.html");
        webView.loadUrl("file:///android_asset/allWebView/attention.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("attentJSONData01",attentJSONData);
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initFirstWindow('"+null+"','"+attentJSONData+"')");
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");
    }

    /**
     * JS调用原生的接口类
     */
    class MyObject{
//        @JavascriptInterface
//        public void comfirmAttention(String data) {
//            Log.e("comfirmAttention",data);
//            //向后台发送“已关注”信息
//            try {
//                JSONObject jsonObject = new JSONObject(data);
//                class_id = jsonObject.getString("class_id");
//                in_class = jsonObject.getString("in_class");
//                // Log.e("class_id",reqString+"");
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.e("JSONException", e.toString());
//
//            }
//            String attentedUrl = AnkiDroidApp.BASE_DOMAIN + "Home/App/guanzhuClass/";
//            String unattentedUrl = AnkiDroidApp.BASE_DOMAIN + "Home/App/removeGuanzhu/";
//            String url = null;
//            Map<String, String> map = new HashMap<>();
//            map.put("mem_id", mem_id);
//            map.put("class_id", class_id);
//            if (in_class.equals("1")) {
//                 url = attentedUrl;
//            } else if (in_class.equals("0")) {
//                url = unattentedUrl;
//            }
//            ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.e("comfirmAttention", ">>>>>>>>>>onSuccess>>>" + result);
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    Log.e("comfirmAttention", ">>>>>>>>>>onError>>>" + ex);
//
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//
//                }
//            });
//        }

        @JavascriptInterface
        public void backToParent(){
//            Log.e("AikaAttentClassActivity","backToParent");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AikaAttentClassActivity.this.finish();
                    AikaAttentClassActivity.this.overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });

        }

        /**
         * 关注班级
         * @param classId
         * @param index
         */
        @JavascriptInterface
        public void attentOneClass(String classId, final String index){
            class_id = classId;
            Log.e("class_id======",class_id+"");
            String attentedUrl = AnkiDroidApp.BASE_DOMAIN + "Home/App/guanzhuClass/";
            Log.e("attentedUrl=====",attentedUrl);
            Map<String, String> map = new HashMap<>();
            map.put("mem_id", mem_id);
            map.put("class_id", class_id);
            ZXUtils.Post(attentedUrl, map, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("attentOneClass", ">>>>>>>>>>onSuccess>>>" + result);
                    webView.loadUrl("javascript:attentionOk('"+true+"','"+index+"')");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("attentOneClass", ">>>>>>>>>>onError>>>" + ex);
                    webView.loadUrl("javascript:attentionOk('"+false+"','"+index+"')");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    attentJSONData = null;
                    getAttentionJsonData(mem_id);
                    Log.e("attentJSONData02",attentJSONData);
                }
            });
        }

        /**
         * 取消关注
         * @param classId
         * @param index
         */
        @JavascriptInterface
        public void offAttentOneClass(String classId, final String index){
            class_id = classId;
            Log.e("class_id$$$$$$",class_id);
            String unattentedUrl = AnkiDroidApp.BASE_DOMAIN + "Home/App/removeGuanzhu/";
            Log.e("unattentedUrl",unattentedUrl);
            Map<String, String> map = new HashMap<>();
            map.put("mem_id", mem_id);
            map.put("class_id", class_id);
            ZXUtils.Post(unattentedUrl, map, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("offAttentOneClass", ">>>>>>>>>>onSuccess>>>" + result);
                    webView.loadUrl("javascript:offAttentionOk('"+true+"','"+index+"')");
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("offAttentOneClass", ">>>>>>>>>>onError>>>" + ex);
                    webView.loadUrl("javascript:offAttentionOk('"+false+"','"+index+"')");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    attentJSONData = null;
                    getAttentionJsonData(mem_id);
                    Log.e("attentJSONData03",attentJSONData);
                }
            });
        }


        /**
         * 邀请码关注班级
         */
        @JavascriptInterface
        public void giveYouInvite(String inviteValue){
            if (TextUtils.isEmpty(inviteValue)) {
                ToastAlone.showShortToast("请输入邀请码");
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("mem_id", mem_id);
            map.put("yao", inviteValue);
            ZXUtils.Post(Urls.URL_APP_YAO_GUAN_ZHU, map, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        int code = result.getInt("code");
                        String data = result.getString("data");
                        if (code == 1000) {
                            webView.loadUrl("javascript:isInvite('"+ true +"', '"+data+"')");
                        }else{
                            ToastAlone.showShortToast(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.e("onError: ex = " + ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    getAttentionJsonData(mem_id);
                }
            });
        }



    }
    /**
     * 从后台请求关注老师页面的数据
     */
    public void getAttentionJsonData(String memId){
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/classList/";
        Map myMap = new HashMap<String,String>();
        myMap.put("mem_id", mem_id);
        ZXUtils.Post(url, myMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAttentionJsonData", ">>>onSuccess>>>" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if(code == 1000){
                        SPUtil.setPreferences(SPUtil.TYPE_DEFAULT_CONFIG,"AttentionJsonData", jsonObject.toString());
                        //canGoToAttentionPage = true;
                        attentJSONData = jsonObject.toString();
                    }else{
                        //标志符 让用户先登录
                        //canGoToAttentionPage = false;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
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
                Log.e("attentJSONData",attentJSONData);
                webView.loadUrl("javascript:initFirstWindow('"+null+"','"+attentJSONData+"')");
            }
        });
    }
    /*public String getAttentionJsonData(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String attention_json_data = pref.getString("AttentionJsonData","");
        return attention_json_data;
    }*/
}
