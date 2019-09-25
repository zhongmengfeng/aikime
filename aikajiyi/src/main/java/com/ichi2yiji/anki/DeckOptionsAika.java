package com.ichi2yiji.anki;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.exception.ConfirmModSchemaException;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.libanki.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DeckOptionsAika extends BaseActivity {
    private WebView webView;
    private JSONObject mDeck;
    private Collection mCol;
    private JSONObject mOptions;
    private JSONObject jsonObject_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_options_aika);

        ApplyTranslucency.applyKitKatTranslucency(this);

        mCol = CollectionHelper.getInstance().getCol(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("did")) {
            mDeck = mCol.getDecks().get(extras.getLong("did"));
            Log.e("DeckOptionsAika", "mDeck>>>>>>>>>>" + mDeck);
        } else {
            mDeck = mCol.getDecks().current();
        }


        JSONObject jsonObject_in = new JSONObject();
        jsonObject_out = new JSONObject();
        try {
            mOptions = mCol.getDecks().confForDid(mDeck.getLong("id"));

            ArrayList<JSONObject> allConf = mCol.getDecks().allConf();//所有牌组的conf信息
            Log.e("DeckOptionsAika", "allConf>>>>>>>>>>>>>" + allConf);
            for(int i = 0; i < allConf.size(); i++){
                //将所有牌组的conf信息放入
                JSONObject conf = allConf.get(i);
                jsonObject_in.put(conf.getString("id"), conf);
            }
            jsonObject_out.put("allOption", jsonObject_in);
            //放置selectedId
            jsonObject_out.put("selectedId", mDeck.getString("conf"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String data = jsonObject_out.toString();
        Log.e("DeckOptionsAika", "data>>>>>>>>>>" + data);

//        CopyRawToDataForInitDeck.save(data, Environment.getExternalStorageDirectory().getAbsolutePath(),"Json数据.txt");//输出查看数据

        /**
         * WebView偏好设置
         */
        webView=(WebView)findViewById(R.id.deck_option_webview);
        webView.loadUrl("file:///android_asset/allWebView/DeckOptions.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // '"+currentThemeTag+"'
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
                webView.loadUrl("javascript:initFirWindow('"+null+"','"+data+"')");

            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");
    }



    /**
     * JS调用原生的接口类，偏好设置
     */
    class MyObject {
        @JavascriptInterface
        public void backToParent() {
            DeckOptionsAika.this.finish();
            overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }

        @JavascriptInterface
        public void comfirm(String data) {
            //确定
            Log.e("DeckOptionsAika", "comfirm>>>>>>>>" + data);
            try {
                JSONObject json_from_html = new JSONObject(data);
                JSONObject allData = json_from_html.getJSONObject("allOption");//全部类型的数据

                String selectedId = json_from_html.getString("selectedId");//选中的牌组类型
                JSONObject selectedData = allData.getJSONObject(selectedId);//被选中的类型数据
                String selected_name = selectedData.getString("name");//被选中类型的名字


                //判断当前选中的是否为新添加的，如果是，则在数据库中新建，如果只是修改已存在的配置，则只需保存
                if(!jsonObject_out.getJSONObject("allOption").has(selectedId)){
                    long id = mCol.getDecks().confId(selected_name, selectedData.toString());
                    mDeck.put("conf", id);
                    mCol.getDecks().save(mDeck);
                }else{
                    mDeck.put("conf", selectedId);
                    mCol.getDecks().save(mDeck);
                    mCol.getDecks().saveconf(Long.parseLong(selectedId), selectedData);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            finish();
            overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);

        }


        @JavascriptInterface
        public void remove(String id) {
            Log.e("DeckOptionsAika", "remove>>>>>>>>done!>>>>id>>>>" + id);
            try {
                mCol.getDecks().remConf(Long.parseLong(id));
                mDeck.put("conf", 1);
            } catch (ConfirmModSchemaException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void applyToChild() {
            Log.e("DeckOptionsAika", "applyToChild>>>>>>>>done!");
            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_CONF_SET_SUBDECKS, mConfChangeHandler,
                    new DeckTask.TaskData(new Object[] { mDeck, mOptions }));
        }
    }


    private DeckTask.TaskListener mConfChangeHandler = new DeckTask.TaskListener() {
        @Override
        public void onPreExecute() {

        }

        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
        }

        @Override
        public void onPostExecute(DeckTask.TaskData result) {

        }

        @Override
        public void onCancelled() {
            // TODO Auto-generated method stub

        }
    };
}
