package com.ichi2yiji.anki;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.bean.SettingsBean;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.common.SettingUtil;
import com.ichi2yiji.utils.SPUtil;

import org.apache.commons.lang.math.NumberUtils;

public class SettingActivity extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 更改当前主题
        ThemeChangeUtil.changeTheme(this);
        // 获取当前主题
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ApplyTranslucency.applyKitKatTranslucency(this);
        SettingsBean settings1 = SettingUtil.getSettings();
        Log.e("setting",settings1.getVersion()+"");
        int answerButtonType = settings1.getAnswerButtonType();
        Log.e("setting",answerButtonType+"answerButtonType");

        /**
         * WebView偏好设置
         */
        webView=(WebView)findViewById(R.id.setting_webview);
        webView.loadUrl("file:///android_asset/allWebView/setting.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 初始化设置
                String settingsJson = SettingUtil.getSettingsJson();
                webView.loadUrl("javascript:initFirWindow('"+null+"','"+settingsJson+"')");
                // 设置h5页面主题色
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
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

    @Override
    public void onBackPressed() {
        webView.loadUrl("javascript:goBack()");
    }

    /**
     * JS调用原生的接口类，偏好设置
     */
    class MyObject {
        @JavascriptInterface
        public void backToParent(String returnData) {
            //判断返回的字符串数据和偏好设置内保存的是否一致，如若不一致，则将其覆盖
            String data = SPUtil.getPreferences(SPUtil.TYPE_SETTINGS_DATA,"Settings", "");
            if (!TextUtils.equals(returnData, data)) {
                SPUtil.setPreferences(SPUtil.TYPE_SETTINGS_DATA,"Settings", returnData);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SettingActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });
        }

        @JavascriptInterface
        public void changTheme(String index){
            SettingsBean settings = SettingUtil.getSettings();
            settings.setThemeIndex(NumberUtils.toInt(index));
            SettingUtil.upDateSettings(settings);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ThemeChangeUtil.changeTheme(SettingActivity.this);
                    ApplyTranslucency.applyKitKatTranslucency(SettingActivity.this);
                }});
            }
    }
}
