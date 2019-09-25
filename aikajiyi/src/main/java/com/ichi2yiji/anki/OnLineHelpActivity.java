package com.ichi2yiji.anki;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;

public class OnLineHelpActivity extends Activity {

    private WebView webView;
    private int currentThemeTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_help);
        ApplyTranslucency.applyKitKatTranslucency(this);
        initUI();
        setWebView();
    }

    public void initUI(){
        webView=(WebView) findViewById(R.id.linehelp_wv_help);
    }
    public void setWebView(){
        webView.loadUrl("file:///android_asset/allWebView/onlineHelp.html");
//        webView.loadUrl("file:///android_asset/OnLineHelp/onlineHelp.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initThisWindow('"+null+"')");
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.addJavascriptInterface(new OnLineHelpActivity.MyObject(), "xuming");
    }

    class MyObject{
        @JavascriptInterface
        public void backToPresent(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });

        }
    }
}
