package com.ichi2yiji.anki;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

public class AikaDistributeActivity extends Activity {

    private WebView webView;
    private String class_id = null;
    private String goods_id = null;
    private String mem_id= null;
    private String distribute_json_data;
    // private String currentThemeTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aika_distribute);

        ApplyTranslucency.applyKitKatTranslucency(this);

        getDistributeJsonData();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//        final String distribute_json_data = pref.getString("DistributeJsonData","");
        mem_id = pref.getString("MEM_ID","");

        /**
         * WebView分发作业
         */
        webView=(WebView)findViewById(R.id.distribute_webview);
//        webView.loadUrl("file:///android_asset/distribute_attention/distribute0308.html");
//        webView.loadUrl("file:///android_asset/allWebView/distribute0309.html");
        webView.loadUrl("file:///android_asset/allWebView/distribute.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initFirstWindow('"+null+"','"+distribute_json_data+"')");
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
        @JavascriptInterface
        public void comfirmDistribute(String data){
            Log.e("comfirmDistribute",data);
            try{
                JSONObject jsonObject = new JSONObject(data);
                class_id = jsonObject.getString("class_id");
                goods_id = jsonObject.getString("goods_id");
            }catch (JSONException e){
                e.printStackTrace();
                Log.e("JSONException", e.toString());

            }

            String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/pushGroup/";
            Map<String, String> map = new HashMap<>();
            map.put("mem_id",mem_id);
            map.put("class_id",class_id);
            map.put("goods_id",goods_id);
            ZXUtils.Post(url, map, new Callback.CommonCallback<String>(){
                @Override
                public void onSuccess(String result) {
                    Log.e("comfirmDistribute", ">>>>>>>>>>onSuccess>>>" + result);
                    Toast.makeText(getApplicationContext(), "课件分发成功！",Toast.LENGTH_SHORT).show();
                    AikaDistributeActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("comfirmDistribute", ">>>>>>>>>>onError>>>" + ex);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        }
        @JavascriptInterface
        public void backToParent(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AikaDistributeActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });

        }

    }


    //从后台请求分发作业页面的数据
    public void getDistributeJsonData(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = pref.edit();
        String mem_id = pref.getString("MEM_ID","");
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/messageList/";
        Map map = new HashMap<String,String>();
        map.put("mem_id",mem_id);
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getDistributeJsonData", ">>>onSuccess>>>" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if(code == 1000){
                        editor.putString("DistributeJsonData", jsonObject.toString());
                        editor.commit();
                        distribute_json_data = jsonObject.toString();
                        webView.loadUrl("javascript:initFirstWindow('"+null+"','"+distribute_json_data+"')");
                    }

                }catch (JSONException e){
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
}
