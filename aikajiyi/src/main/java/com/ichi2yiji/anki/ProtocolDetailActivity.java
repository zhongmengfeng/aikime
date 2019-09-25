package com.ichi2yiji.anki;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ApplyTranslucency;

public class ProtocolDetailActivity extends Activity {
    private WebView webView;
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol_detail);
        ApplyTranslucency.applyKitKatTranslucency(this);
        webView = (WebView) findViewById(R.id.protocol_detail_webview);
        backImage = (ImageView)findViewById(R.id.protocol_detail_back);
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl("file:///android_asset/AikaRegProtocol.html");
        webView.setWebViewClient(new WebViewClient());
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });
    }
}
