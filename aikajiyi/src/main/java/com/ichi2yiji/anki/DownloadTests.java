package com.ichi2yiji.anki;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 共享题库
 */
public class DownloadTests extends BaseActivity {
    private String jsonData;
    private WebView webView;
    private int currentThemeTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        ThemeChangeUtil.changeTheme(this);
        currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_tests);
        ApplyTranslucency.applyKitKatTranslucency(this);

        //初始化
        webView = (WebView) findViewById(R.id.webView_downloadtests);
        webView.loadUrl("file:///android_asset/allWebView/AA071_downloaTests_1.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public JSONArray jsonArray;

            //重写父类的url加载方式
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // String jsonData = "[{\"title\":\"jiakao\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/2017021516\\/jiakao.mtest\",\"goods_id\":\"1297\",\"short_desc\":\"\\u9a7e\\u8003\",\"addtime\":\"2017-02-15 16:59:12\",\"logo\":\"https:\\/\\/ss1.bdstatic.com\\/70cFvXSh_Q1YnxGkpoWK1HF6hhy\\/it\\/u=3417969523,1166399386&fm=23&gp=0.jpg\",\"xia\":\"3\"},{\"title\":\"\\u533b\\u5b66\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e00\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241511\\/%E5%8C%BB%E5%AD%A6%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%B8%80.mtest\",\"goods_id\":\"1625\",\"short_desc\":\"\\u533b\\u5b661\",\"addtime\":\"2017-03-24 15:11:09\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee27746554.jpg\",\"xia\":\"12\"},{\"title\":\"\\u533b\\u5b66\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e8c\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241511\\/%E5%8C%BB%E5%AD%A6%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%BA%8C.mtest\",\"goods_id\":\"1626\",\"short_desc\":\"\\u533b\\u5b662\",\"addtime\":\"2017-03-24 15:11:15\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee26534d43.jpg\",\"xia\":\"0\"},{\"title\":\"\\u533b\\u5b66\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e09\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241511\\/%E5%8C%BB%E5%AD%A6%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%B8%89.mtest\",\"goods_id\":\"1627\",\"short_desc\":\"\\u533b\\u5b663\",\"addtime\":\"2017-03-24 15:11:21\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee252692de.jpg\",\"xia\":\"1\"},{\"title\":\"\\u9020\\u4ef7\\u5e08\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e00\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241511\\/%E9%80%A0%E4%BB%B7%E5%B8%88%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%B8%80.mtest\",\"goods_id\":\"1628\",\"short_desc\":\"\\u9020\\u4ef7\\u5e081\",\"addtime\":\"2017-03-24 15:11:34\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee4be507c4.jpg\",\"xia\":\"3\"},{\"title\":\"\\u9020\\u4ef7\\u5e08\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e8c\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241511\\/%E9%80%A0%E4%BB%B7%E5%B8%88%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%BA%8C.mtest\",\"goods_id\":\"1629\",\"short_desc\":\"\\u9020\\u4ef7\\u5e082\",\"addtime\":\"2017-03-24 15:11:40\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee4aac90dd.jpg\",\"xia\":\"1\"},{\"title\":\"\\u9020\\u4ef7\\u5e08\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e09\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241511\\/%E9%80%A0%E4%BB%B7%E5%B8%88%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%B8%89.mtest\",\"goods_id\":\"1630\",\"short_desc\":\"\\u9020\\u4ef7\\u5e083\",\"addtime\":\"2017-03-24 15:11:45\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee495abcd6.jpg\",\"xia\":\"0\"},{\"title\":\"\\u9a7e\\u7167\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e8c\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241515\\/%E9%A9%BE%E7%85%A7%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%BA%8C.mtest\",\"goods_id\":\"1631\",\"short_desc\":\"\\u9a7e\\u80032\",\"addtime\":\"2017-03-24 15:15:15\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee0d404ff3.jpg\",\"xia\":\"2\"},{\"title\":\"\\u9a7e\\u7167\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e09\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241515\\/%E9%A9%BE%E7%85%A7%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%B8%89.mtest\",\"goods_id\":\"1632\",\"short_desc\":\"\\u9a7e\\u80033\",\"addtime\":\"2017-03-24 15:15:22\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee0433ba23.jpg\",\"xia\":\"3\"},{\"title\":\"\\u9a7e\\u7167\\u8003\\u8bd5__\\u8bd5\\u9898\\u56db\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201703241515\\/%E9%A9%BE%E7%85%A7%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E5%9B%9B.mtest\",\"goods_id\":\"1633\",\"short_desc\":\"\\u9a7e\\u80034\",\"addtime\":\"2017-03-24 15:15:27\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee0245531b.jpg\",\"xia\":\"3\"},{\"title\":\"\\u521d\\u4e2d\\u82f1\\u8bed\\u8bd5\\u9898\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201704011426\\/%E5%88%9D%E4%B8%AD%E8%8B%B1%E8%AF%AD%E8%AF%95%E9%A2%98.mtest\",\"goods_id\":\"1747\",\"short_desc\":\"\\u82f1\\u8bed\\u8bd5\\u9898\",\"addtime\":\"2017-04-01 14:26:53\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58df4dcadf943.png\",\"xia\":\"4\"},{\"title\":\"\\u9020\\u4ef7\\u5e08\\u8003\\u8bd5__\\u8bd5\\u9898\\u56db\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201704011718\\/%E9%80%A0%E4%BB%B7%E5%B8%88%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E5%9B%9B.mtest\",\"goods_id\":\"1759\",\"short_desc\":\"\\u9020\\u4ef7\\u5e08\\u56db\",\"addtime\":\"2017-04-01 17:18:56\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee4794f73b.jpg\",\"xia\":\"0\"},{\"title\":\"\\u9a7e\\u7167\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e00\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201704011730\\/%E9%A9%BE%E7%85%A7%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%B8%80.mtest\",\"goods_id\":\"1761\",\"short_desc\":\"\\u9a7e\\u80031\",\"addtime\":\"2017-04-01 17:30:45\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eee0006dd3a.jpg\",\"xia\":\"1\"},{\"title\":\"\\u9a7e\\u7167\\u8003\\u8bd5__\\u8bd5\\u9898\\u4e94\",\"url\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Uploads\\/201704011730\\/%E9%A9%BE%E7%85%A7%E8%80%83%E8%AF%95__%E8%AF%95%E9%A2%98%E4%BA%94.mtest\",\"goods_id\":\"1762\",\"short_desc\":\"\\u9a7e\\u80035\",\"addtime\":\"2017-04-01 17:30:53\",\"logo\":\"http:\\/\\/anki.oss-cn-hangzhou.aliyuncs.com\\/Logos\\/58eedfef6a3d6.jpg\",\"xia\":\"0\"}]";
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initLayout('"+jsonData+"', '"+null+"')");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");
        requestData();
    }

    private void requestData(){
        Map<String, String> map = new HashMap<>();
        // 共享模考
        ZXUtils.Post(Urls.URL_APP_TESTS, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("DeckTestReal","requestData>>>>onSuccess" + result);
                jsonData = result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("DeckTestReal","requestData>>>>onError" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                webView.loadUrl("javascript:initLayout('"+jsonData+"', '"+null+"')");
            }
        });
    }

    private class MyObject {
        //供js调用  需要添加@JavascriptInterface注解
        @JavascriptInterface
        public void downloadSingleItem(String[] data) {
            Log.i("downloadSingleItem", "downloadSingleItem");
            downloadTodirTests(data);
        }

        @JavascriptInterface
        public void backToParent() {
            Log.i("downloadSingleItem", "downloadSingleItem");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DownloadTests.this.finish();
                    overridePendingTransition(0, R.anim.out_top_to_bottom_translate_anim);
                }
            });

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

    /**
     *  下载
     *  data[0] 下载地址
     *  data[1] id
     *  data[2] 描述
     *  data[3] 图片地址
     * @param data
     */
    private void downloadTodirTests(final String[] data){
        String path;
        path = data[0];
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

        final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirTests/" +filename;
        ZXUtils.DownLoadFile(path, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                ToastAlone.showShortToast(filename.replace(".mtest","")+" 下载成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("downloadTodirTests", "onError>>>" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("PathFromDownLoadBooks", filepath);
                        setResult(RESULT_OK, intent);
                        if (data.length >= 2 ) {
                            String goodsId = data[1];
                            addStatistics(goodsId);
                        }
                        DownloadTests.this.finish();
                        DownloadTests.this.overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
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

        ZXUtils.Post(Urls.URL_DOWN_TESTS, map, new Callback.CommonCallback<JSONObject>() {
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
