package com.ichi2yiji.anki;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaojiyiji.yiji.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by 金鹏 on 2016/12/16.
 */

public class DeckTest extends NavigationDrawerActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_test);

        applyKitKatTranslucency();


        //初始化
        webView = (WebView) findViewById(R.id.webView_test);
        webView.loadUrl("file:///android_asset/testMUI_888.01/AA07_deckTest_1.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            //重写父类的url加载方式
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                webView.loadUrl("javascript:initFirWindow('"+null+"', '"+null+"')");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");

        View mainView = findViewById(android.R.id.content);

        initNavigationDrawer(mainView);
        setTitle(getResources().getString(R.string.app_name));
    }




    class  MyObject {
        //供js调用  需要添加@JavascriptInterface注解
        @JavascriptInterface
        public void importDownloadBook() {
            Log.i("importDownloadBook", "importDownloadBook");
        }

        @JavascriptInterface
        public void showSliderMenu(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toOpenDrawer();
                }
            });
        }

        @JavascriptInterface
        public void testHelp(){
            Log.i("testHelp", "testHelp");
        }

        @JavascriptInterface
        public void sharedTests(){
            Log.i("sharedTests", "sharedTests");
            Intent intent = new Intent(DeckTest.this, DownloadTests.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        @JavascriptInterface
        public void beginTesting(String path){
            Log.i("beginTesting", "beginTesting");
            Intent intent = new Intent(DeckTest.this, DeckTestReal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        @JavascriptInterface
        public void deleteOneTest(String curBookName){
            Log.i("deleteOneTest", "deleteOneTest");
        }

        @JavascriptInterface
        public void switch_reader(){
            Log.i("switch_reader", "switch_reader");
            Intent intent = new Intent(DeckTest.this, DeckReader.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            DeckTest.this.finish();
            //dx  add
            overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }

        @JavascriptInterface
        public void switch_decks(){
            Log.i("switch_reader", "switch_reader");
            Intent intent = new Intent(DeckTest.this, DeckPicker.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            DeckTest.this.finish();
            //dx  add
            overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }

    }


    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            mTintManager.setStatusBarTintResource(R.color.material_top_blue);//通知栏所需颜色
        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}

