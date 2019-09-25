package com.ichi2yiji.anki;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
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

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 共享文章
 */
public class DownloadBooks extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_books);

        ApplyTranslucency.applyKitKatTranslucency(this);

        SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String json = pref.getString("SHAREBOOKDATA","");
        final String data = json.replaceAll("@@@[0-9]", "");
        //初始化
        webView = (WebView) findViewById(R.id.webView_downloadbooks);
        webView.loadUrl("file:///android_asset/allWebView/AA081_downloaBooks_1.html");
//        webView.loadUrl("file:///android_asset/testMUI_888.01/AA081_downloaBooks_1.html");
//        webView.loadUrl("file:///android_asset/18_casApp-1/new_file.html");
//        webView.loadUrl("file:///android_asset/tech/index.html");
        WebSettings settings = webView.getSettings();
//        settings.setUseWideViewPort(true); // 关键点
//        settings.setAllowFileAccess(true); // 允许访问文件
//        settings.setSupportZoom(true); // 支持缩放
//        settings.setLoadWithOverviewMode(true);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容

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
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initLayout('"+data+"', '"+null+"')");

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onShowCustomView(View view, CustomViewCallback callback) {
//                super.onShowCustomView(view, callback);
//            }
        });
        webView.addJavascriptInterface(new MyObject(), "xuming");

    }

    class MyObject {
        //供js调用  需要添加@JavascriptInterface注解

        @JavascriptInterface
        public void downloadSingleItem(String[] data) {
            downloadTodirBooks(data);
        }

        @JavascriptInterface
        public void backToParent() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DownloadBooks.this.finish();
                    overridePendingTransition(0, R.anim.out_top_to_bottom_translate_anim);
                }
            });


        }

    }

    private void downloadTodirBooks(final String[] data){
        String path = data[0];
        final String filename = path.substring(path.lastIndexOf("/")+1);
        String filename_encode = null;
        try {//对地址中的汉字转码
            filename_encode = URLEncoder.encode(filename, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (filename_encode == null) {
            ToastAlone.showShortToast("下载链接错误");
            return;
        }
        path = path.replace(filename, filename_encode);

        final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirBooks/" +filename;
        ZXUtils.DownLoadFile(path, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                ToastAlone.showShortToast(filename.replace(".txt","")+" 下载成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("downloadTodirBooks", "onError>>>" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent();
//                        i.putExtra("PathFromDownLoadBooks", filepath);
                        setResult(RESULT_OK, i);
                        if (data.length >= 2 ) {
                            String goodsId = data[1];
                            addStatistics(goodsId);
                        }
                        DownloadBooks.this.finish();
                        DownloadBooks.this.overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                    }
                },1000);
            }
        });
    }

    /**
     * 添加下载统计
     */
    private void addStatistics(String goodsId) {
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        HashMap<String, String> map = new HashMap<>();
        map.put("goods_id",goodsId);
        map.put("mem_id",memId);

        ZXUtils.Post(Urls.URL_DOWN_READER, map, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                LogUtil.e("onSuccess: result = " + result);
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

            }
        });
    }

}
