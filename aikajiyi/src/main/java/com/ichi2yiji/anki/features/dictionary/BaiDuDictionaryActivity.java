package com.ichi2yiji.anki.features.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.common.Urls;

public class BaiDuDictionaryActivity extends Activity implements View.OnClickListener {

    private WebView wvDictionary;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_du_dictionary);

        initView();
        setListener();
        initWebView();
        revMsg();
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
    }

    private void initView() {
        wvDictionary = (WebView)findViewById(R.id.wv_dictionary);
        ivBack = (ImageView)findViewById(R.id.iv_back);
    }

    private void initWebView() {
        WebSettings settings = wvDictionary.getSettings();
        // settings.setUseWideViewPort(true); // 关键点
        // settings.setAllowFileAccess(true); // 允许访问文件
        // settings.setSupportZoom(true); // 支持缩放
        // settings.setLoadWithOverviewMode(true);
        // settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容

        settings.setJavaScriptEnabled(true);
        wvDictionary.setWebViewClient(new WebViewClient() {
            //重写父类的url加载方式
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 去除网页顶部蓝条
                String joCode = "document.getElementById('search-bar').remove();document.getElementsByClassName('c-container card')[0].remove();document.getElementsByClassName('c-result c-row-tile')[0].remove();";
                wvDictionary.loadUrl("javascript:" + joCode);
            }
        });
        wvDictionary.setWebChromeClient(new WebChromeClient() {

        });
    }

    private void revMsg() {
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        // 过滤特殊字符
        String reg = "[^\u4e00-\u9fa5a-zA-Z0-9]";
        key = key.replaceAll(reg, "");
        String url = String.format(Urls.URL_BAIDU_DIACTIONARY, key);
        wvDictionary.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
