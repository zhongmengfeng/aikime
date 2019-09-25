package com.ichi2yiji.anki;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.bean.SearchDecksBean;
import com.ichi2yiji.anki.bean.SharedDecksBean;
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
import java.util.List;
import java.util.Map;

/**
 * 艾卡共享
 */
public class DownLoadDecks_webview extends BaseActivity {

    private WebView webView;
    private String jsonData;
    private int currentThemeTag;
    // private JSONObject decksJson;
    private SharedDecksBean decksBean;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        ThemeChangeUtil.changeTheme(this);
        currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_deck);
        ApplyTranslucency.applyKitKatTranslucency(this);

        svProgressHUD = new SVProgressHUD(DownLoadDecks_webview.this);
        initWebView();

        requestData();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView_downloaddeck);
        webView.loadUrl("file:///android_asset/allWebView/AA091_downloadDeck_1.html");
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
                // SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                // String jsonData = pref.getString("DECKINFOFROMOSS", "");
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initLayout('"+jsonData+"', '"+null+"', '"+currentThemeTag+"')");
                LogUtil.e("onPageFinished: javascript:changeAllColor('"+currentThemeTag+"')");
                LogUtil.e("onPageFinished: javascript:initLayout('"+jsonData+"', '"+null+"', '"+currentThemeTag+"')");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");
    }


    private class MyObject {
        //供js调用  需要添加@JavascriptInterface注解

        @JavascriptInterface
        public void downloadSingleItem(String[] data) {
            String goodsId = data[1];
            addStatistics(goodsId,data);
        }

        @JavascriptInterface
        public void searchById(String catId){
            searchByCatId(catId);
        }

        @JavascriptInterface
        public void searchByName(String name){
            searchByCatName(name);
        }

        @JavascriptInterface
        public void backToParent() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DownLoadDecks_webview.this.finish();
                    overridePendingTransition(0, R.anim.out_top_to_bottom_translate_anim);
                }
            });
        }
    }

    private void searchByCatName(String name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("goods_name",name);
        ZXUtils.Post(Urls.URL_SEARCH_PICKER, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                SearchDecksBean searchDecksBean = gson.fromJson(result, SearchDecksBean.class);
                if(searchDecksBean.getCode() == 1000){
                    int colorNum = decksBean.getData().getColorNum();
                    List<SharedDecksBean.DataBean.CatsBean> cats = decksBean.getData().getCats();
                    searchDecksBean.setColorNum(colorNum);
                    searchDecksBean.setCats(cats);
                    searchDecksBean.setDecks(searchDecksBean.getData());

                    String json = gson.toJson(searchDecksBean);
                    webView.loadUrl("javascript:initLayout('"+json+"', '"+null+"', '"+currentThemeTag+"')");
                    LogUtil.e("onSuccess: javascript:initLayout('"+json+"', '"+null+"', '"+currentThemeTag+"')");
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

    private void requestData(){
        Map<String, String> map = new HashMap<>();
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        map.put("mem_id", memId);
        Log.e("mem_id",memId);
        ZXUtils.Post(Urls.URL_APP_PICKER, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                decksBean = gson.fromJson(result, SharedDecksBean.class);
                int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(null);
                decksBean.getData().setColorNum(currentThemeTag);
                jsonData = gson.toJson(decksBean.getData());
                webView.loadUrl("javascript:initLayout('"+ jsonData +"', '"+null+"')");
                LogUtil.e("onSuccess: javascript:initLayout('"+ jsonData +"', '"+null+"')");
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

            }
        });
    }


    private void searchByCatId(String id){
        Map<String, String> map = new HashMap<>();
        map.put("cat_id",id);
        ZXUtils.Post(Urls.URL_KEYWORD_SEARCH_PICKER, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("onSuccess: result = " + result.toString());
                Gson gson = new Gson();
                SearchDecksBean searchDecksBean = gson.fromJson(result, SearchDecksBean.class);
                if(searchDecksBean.getCode() == 1000){
                    int colorNum = decksBean.getData().getColorNum();
                    List<SharedDecksBean.DataBean.CatsBean> cats = decksBean.getData().getCats();
                    searchDecksBean.setColorNum(colorNum);
                    searchDecksBean.setCats(cats);
                    searchDecksBean.setDecks(searchDecksBean.getData());

                    String json = gson.toJson(searchDecksBean);
                    webView.loadUrl("javascript:initLayout('"+json+"', '"+null+"', '"+currentThemeTag+"')");
                    LogUtil.e("onSuccess: javascript:initLayout('"+json+"', '"+null+"', '"+currentThemeTag+"')");
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
            }
        });
    }

    private void downloadTodirDecks(final String[] data){
        String path = data[0];
        String deckName = "";
        if(data.length >=2){
            deckName = data[1];
        }
        // webView.loadUrl("javascript:showInDownload('"+ deckName +"')");
        showInputProgressCircle(null,"正在下载中");
        String filename = path.substring(path.lastIndexOf("/")+1);
        Log.i("filename", "downloadTodirDecks: "+filename);
        //处理GBK编码汉字为UTF-8格式
        String path_encode = path;
        try {
            String filename_encode_1 = URLEncoder.encode(filename, "UTF-8");
            String filename_encode_2 = filename_encode_1.replace("+", "%20");//把变量中的加号（+）全部替换为“%20”
            String filename_encode_3 = filename_encode_2.replace("yiji", "apkg");
            path_encode = path.replace(filename, filename_encode_2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" +filename.replace("yiji", "apkg");
        ZXUtils.DownLoadFile(path_encode, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.e("downloadTodirDecks", "onSuccess");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("downloadTodirDecks", "onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                // webView.loadUrl("javascript:disappearOKBox()");
                disappearProgress();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent();
                        i.putExtra("PathFromDownLoadDecks", filepath);
                        setResult(RESULT_OK, i);
                        finish();
                        overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                        Log.e("downloadTodirDecks", "onFinished");
                    }
                },1000);
            }
        });
    }

    /**
     * 添加下载统计
     */
    private void addStatistics(String goodsId, final String[] data) {
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        HashMap<String, String> map = new HashMap<>();
        map.put("goods_id",goodsId);
        map.put("mem_id",memId);
        ZXUtils.Post(Urls.URL_DOWN_PICKER, map, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                LogUtil.e("onSuccess: result = " + result);
                Log.i("nan", "onSuccess: "+result);
                try {
                    int code = result.getInt("code");
                    if(code == 1000){
                        downloadTodirDecks(data);
                    }else{
                        String msg = result.getString("data");
                        ToastAlone.showShortToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastAlone.showShortToast("下载失败");
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

            }
        });
    }

    public void showInputProgressCircle(String title, String action) {
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (svProgressHUD != null) {
            svProgressHUD.showWithStatus(title + " " + action);
        }
    }

    /**
     * 关闭进度条
     */
    public void disappearProgress() {
        if (svProgressHUD != null && svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }


}
