package com.ichi2yiji.anki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.libanki.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选牌组
 */
public class FilteredDeckActivity extends BaseActivity {

    private WebView webView;
    private JSONObject mDeck;
    private Collection mCol;
    private boolean mAllowCommit = true;
    private boolean mPrefChanged = false;
    //牌组id
    private long deckDid;
    //传入的牌组名称
    private String filterDeckName;
    String search=null;
    int limit=0;
    int order=0;
    List<Integer> delays=new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen01);


        Intent intent = getIntent();
        deckDid = intent.getLongExtra("did",0);
        filterDeckName = intent.getStringExtra("name");

        mCol = CollectionHelper.getInstance().getCol(this);
        if (mCol == null) {
            finish();
            overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            return;
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("did")) {
            mDeck = mCol.getDecks().get(deckDid);
        } else {
            mDeck = mCol.getDecks().current();
        }
        ApplyTranslucency.applyKitKatTranslucency(this);
        initWebView(currentThemeTag);
    }

    private void initWebView(final int currentThemeTag) {
        webView=(WebView)findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/allWebView/filterDeck.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            //重写父类的url加载方式
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initFirWindow('"+null+"','"+null+"')");
                webView.loadUrl("javascript:changeDeck('"+filterDeckName+"')");
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        //给webView添加javascript的调用接口
        webView.addJavascriptInterface(new MyObject(), "xuming");
    }

    //***********************************************************//
    //***********************************************************//
    class  MyObject{
        //供js调用  需要添加@JavascriptInterface注解
        @JavascriptInterface
        public void resetPreference(String preferenceCur){
            ///////////////dx   start
            try{
                JSONObject jsonObject=new JSONObject(preferenceCur);
                search=jsonObject.getString("search");
                limit=jsonObject.getInt("limit");
                order=jsonObject.getInt("order");
                JSONArray array=jsonObject.getJSONArray("delays");
                filterCommitwith(search,limit,order,array);
                mCol.getSched().rebuildDyn(mCol.getDecks().selected());
                mCol.save();
            }catch (JSONException e){

            }
            /////////////////dx  end
            Intent intent=new Intent();
            intent.putExtra("newName",filterDeckName);
            FilteredDeckActivity.this.setResult(301,intent);
            FilteredDeckActivity.this.finish();
            //dx  add
            overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            return ;
        }
        //dx  add
        @JavascriptInterface
        public void backToPresent(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FilteredDeckActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });

        }

    }

    /**
     * 将页面获得的数据封装，传递给mDeck，用于筛选牌组
     */
    public void filterCommitwith(String search,int limit,int order,JSONArray delay ){
        try{

            JSONArray term=new JSONArray();
            term.put(search);
            term.put(limit);
            term.put(order);
            JSONArray terms= new JSONArray();
            //terms.put(term);
            terms.put(0,term);
            mDeck.put("terms",terms);
            //terms.put(0,term);
            JSONArray delays=mDeck.getJSONArray("delays");
            mDeck.put("delays",delay);
        }catch (JSONException e){

        }
        mCol.getDecks().save(mDeck);
    }

    //***********************************************************//
    //***********************************************************//

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("newName","onBackPressed");
        FilteredDeckActivity.this.setResult(301,intent);
        FilteredDeckActivity.this.finish();
        super.onBackPressed();
    }
}
