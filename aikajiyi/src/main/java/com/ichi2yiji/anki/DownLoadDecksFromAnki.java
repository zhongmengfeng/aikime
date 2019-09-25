package com.ichi2yiji.anki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownLoadDecksFromAnki extends BaseActivity implements View.OnClickListener{
    private WebView webView;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_deckfromanki);
        Log.e("DownLoadDecksFromAnki",">>>>>>>>>>onCreate");

        ApplyTranslucency.applyKitKatTranslucency(this);

        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.showWithStatus("网页加载中...");

        //初始化
        webView = (WebView) findViewById(R.id.webView_deckfromanki);
//        webView.loadUrl("file:///android_asset/ankiShareDecks/index.html");
        webView.loadUrl("https://ankiweb.net/shared/decks/");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);//关键点

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        settings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
          getWindowManager().getDefaultDisplay().getMetrics(metrics);
          int mDensity = metrics.densityDpi;
          if (mDensity == 240) {
              settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
             } else if (mDensity == 160) {
              settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
             } else if(mDensity == 120) {
              settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
             }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
              settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
             }else if (mDensity == DisplayMetrics.DENSITY_TV){
              settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
             }else{
              settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
          }
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);



        webView.setWebViewClient(new WebViewClient() {
            //重写父类的url加载方式
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("DownLoadDecksFromAnki","WebView>>>>>>>>>>onPageFinished");
                svProgressHUD.dismiss();
//                changeHeader();
//                webView.loadUrl("javascript:initLayout('"+null+"', '"+null+"')");

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");

        webView.setDownloadListener(new MyWebViewDownLoadListener());//下载的监听


        ImageView icon_back_left = (ImageView)findViewById(R.id.icon_back_left);
        ImageView icon_back_right = (ImageView)findViewById(R.id.icon_back_right);
        ImageView icon_refresh = (ImageView)findViewById(R.id.icon_refresh);
        ImageView icon_home = (ImageView)findViewById(R.id.icon_home);
        ImageView back_fill_image = (ImageView)findViewById(R.id.back_fill_image);
        icon_back_left.setOnClickListener(this);
        icon_back_right.setOnClickListener(this);
        icon_refresh.setOnClickListener(this);
        icon_home.setOnClickListener(this);
        back_fill_image.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_back_left:
                webView.goBack();
                break;
            case R.id.icon_back_right:
                webView.goForward();
                break;
            case R.id.icon_refresh:
                webView.reload();
                break;
            case R.id.icon_home:
                webView.loadUrl("https://ankiweb.net/shared/decks/");
                break;
            case R.id.back_fill_image:
                DownLoadDecksFromAnki.this.finish();
                overridePendingTransition(0, R.anim.out_top_to_bottom_translate_anim);
                break;
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,long contentLength) {
            Log.e("MyWebViewDownLoad","url>>>>>>>>>>" +url);
            Log.e("MyWebViewDownLoad","userAgent>>>>>>>>>>" +userAgent);
            Log.e("MyWebViewDownLoad","contentDisposition>>>>>>>>>>" +contentDisposition);
            Log.e("MyWebViewDownLoad","mimetype>>>>>>>>>>" + mimetype);

            final SVProgressHUD svProgressHUD = new SVProgressHUD(DownLoadDecksFromAnki.this);


            String filename  = contentDisposition.substring(contentDisposition.lastIndexOf("=")+ 1);
            Log.e("MyWebViewDownLoad",">>>>>>>>>>filename>>>>"+ filename);
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" + filename;

            svProgressHUD.showWithStatus("开始下载");
            com.ankireader.util.ZXUtils.DownLoadFileWithProgress(url, path, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {
                }

                @Override
                public void onStarted() {
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {

                    Float i = Float.parseFloat(String.valueOf(current))/Float.parseFloat(String.valueOf(total));
                    int progress = (int)(i*100);

                    Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>i" +i);
                    Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>progress" +progress);
                    svProgressHUD.setText("下载 " + progress +"%");
                }

                @Override
                public void onSuccess(File result) {
                    Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onSuccess");
                    svProgressHUD.setText("下载完成");

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("MyWebViewDownLoad","DownLoadFileWithProgress>>>>>>>>>>onError" + ex);
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    svProgressHUD.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent();
                            i.putExtra("PathFromDownLoadDecksFromAnki", path);
                            setResult(RESULT_OK, i);
                            DownLoadDecksFromAnki.this.finish();
                            //dx  add
                            DownLoadDecksFromAnki.this.overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                        }
                    },1000);
                }

            });

        }

    }



    public void changeHeader(){

        String js = "var header = document.getElementById('header');";
        js += "logo = document.getElementById('logo'),";
        js += "table = document.getElementsByTagName('table')[0],";
        js += "iptParent = document.getElementById('q').parentNode;";
        js += "logo.style.display = 'none';";
        js += "table.style.display = 'none';";
        js += "header.style.height = 44 + 'px';";
        js += "iptParent.style.position = 'absolute';";
        js += "iptParent.style.left = 50 + '%';";
        js += "iptParent.style.marginLeft = - 117 + 'px';";
        js += "iptParent.style.top = 12 + 'px';";


        webView.loadUrl("javascript:" + js);


        //webView.loadUrl("javascript:changeColor('"+color+"','"+str+"')");
    }

    class  MyObject{
        //供js调用  需要添加@JavascriptInterface注解
        @JavascriptInterface
        public String getTime(){
            return new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date());
        }
    }

}
