package com.ichi2yiji.anki.features.ready;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.AnkiActivity;
import com.ichi2yiji.anki.CollectionHelper;
import com.ichi2yiji.anki.features.ready.bean.InitFilterConditionBean;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.libanki.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 临考备战
 */
public class ExamPreparationActivity extends AnkiActivity {

    private int currentThemeTag;
    @Bind(R.id.wv_exam_preparation)
    WebView webView;
    private String[][] options;
    private String[] decks;
    private String currentDeckname;
    private String jsonData;
    private Collection mCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_preparation);
        ButterKnife.bind(this);
        ApplyTranslucency.applyKitKatTranslucency(this);
        revMsg();
        initData();
        initWebView();
    }

    private void revMsg() {
        Intent intent = getIntent();
        decks = intent.getStringArrayExtra("decks");
    }

    private void initData() {

        /*@"options": @[@[@"again:count>3",@"筛选出错误次数>3的卡片"],
        @[@"again:count>5",@"筛选出错误次数>5的卡片"],
        @[@"tag:marked",@"筛选出我标记过的卡片"],
        @[@"tag:必考",@"筛选出必考知识点"]],*/


        /*options = new String[][]{
                new String[]{"is:new","筛选出新卡片"},
                new String[]{"added:1","筛选出今天添加的卡片"},
                new String[]{"rated:1","筛选出今天学习的卡片"},
                new String[]{"prop:due<=2","筛选出后天到期的卡片"}};*/

        options = new String[][]{
                new String[]{"again:count>3","筛选出错误次数>3的卡片"},
                new String[]{"again:count>5","筛选出错误次数>5的卡片"},
                new String[]{"tag:marked","筛选出我标记过的卡片"},
                new String[]{"tag:必考","筛选出必考知识点"}};

        // decks = new String[]{"01四级英语","02六级英语","03地质学"};
        // currentDeckname = "00六级英语";


        mCol = CollectionHelper.getInstance().getCol(this);
        if (mCol == null) {
            finish();
            overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            return;
        }
        try {
            currentDeckname = mCol.getDecks().current().getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InitFilterConditionBean filterBean = new InitFilterConditionBean();
        filterBean.setOptions(options);
        filterBean.setDecks(decks);
        filterBean.setCurrentDeckname(currentDeckname);
        Gson gson = new Gson();
        String testJson = gson.toJson(filterBean);
        LogUtil.e("initData: testJson = " + testJson);



        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("currentDeckname",currentDeckname);

            JSONArray optionsArray = new JSONArray();

            // 添加选项
            JSONArray arr1 = new JSONArray();
            arr1.put(0,options[0][0]);
            arr1.put(1,options[0][1]);
            optionsArray.put(0,arr1);

            JSONArray arr2 = new JSONArray();
            arr2.put(0,options[1][0]);
            arr2.put(1,options[1][1]);
            optionsArray.put(1,arr2);

            JSONArray arr3 = new JSONArray();
            arr3.put(0,options[2][0]);
            arr3.put(1,options[2][1]);
            optionsArray.put(2,arr3);

            JSONArray arr4 = new JSONArray();
            arr4.put(0,options[3][0]);
            arr4.put(1,options[3][1]);
            optionsArray.put(3,arr4);

            jsonObject.put("options",optionsArray);

            // 添加牌组列表
            JSONArray deckArray = new JSONArray();
            for (int i = 0; i < decks.length; i++) {
                deckArray.put(i,decks[i]);
            }
            jsonObject.put("decks", deckArray);
            jsonData = jsonObject.toString();

            LogUtil.e("initData: jsonData = " + jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initWebView() {
        webView.loadUrl("file:///android_asset/allWebView/beforeTestLearn.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyObject(), "xuming");

        webView.setWebViewClient(new WebViewClient() {
            //重写父类的url加载方式
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // webView.loadUrl("javascript:initFirstWindow('"+null+"', '"+null+"', '"+currentThemeTag+"')");
                LogUtil.e("onPageFinished: jsonData = " + jsonData);
                Log.e("", "onPageFinished: jsonData = " + jsonData);
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initFirstWindow('"+jsonData+"', '"+null+"')");
                // webView.loadUrl("javascript:initFirstWindow('"+null+"', '"+null+"')");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });

    }



    class MyObject {

        @JavascriptInterface
        public void filterByCondition(String condition){
            LogUtil.e("filterByCondition: condition = " + condition);   // {search: "is:new", deck: "四级英语"}
            JSONObject mDeck = null;
            JSONObject jsonCondition = null;
            String deckName = "def";
            String search = "";
            try {
                jsonCondition = new JSONObject(condition);
                search = jsonCondition.getString("search");
                deckName = jsonCondition.getString("deck") + "(筛选)";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            filterDeck(deckName, search);
        }

        @JavascriptInterface
        public void back(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ExamPreparationActivity.this.finish();
                    overridePendingTransition(0, R.anim.out_top_to_bottom_translate_anim);
                }
            });
        }
    }

    /**
     * 筛选牌组
     * @param deckName
     * @param search
     */
    private void filterDeck(String deckName, String search) {
        JSONObject mDeck;
        long did = mCol.getDecks().newDyn(deckName);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("did")) {
            mDeck = mCol.getDecks().get(did);
        } else {
            mDeck = mCol.getDecks().current();
        }
        try {
            JSONArray term = new JSONArray();
            term.put(search);   // search
            term.put(100);      // limit
            term.put(1);        // order

            JSONArray terms = new JSONArray();
            terms.put(0, term);

            JSONArray delays = new JSONArray();
            delays.put(1);
            delays.put(10);

            mDeck.put("terms",terms);
            mDeck.put("delays",delays);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCol.getDecks().save(mDeck);
        mCol.getSched().rebuildDyn(mCol.getDecks().selected());
        mCol.save();

        Intent intent=new Intent();
        intent.putExtra("newName",deckName);
        ExamPreparationActivity.this.setResult(301,intent);
        ExamPreparationActivity.this.finish();
        overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
