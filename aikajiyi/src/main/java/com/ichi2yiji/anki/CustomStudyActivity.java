package com.ichi2yiji.anki;

import android.content.Intent;
import android.os.Bundle;
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
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class CustomStudyActivity extends BaseActivity {
    WebView webView;
    private int totalNewForCurrentDeck;
    private int totalRevForCurrentDeck;
    private long did;
    private JSONObject deckJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_study);
        ApplyTranslucency.applyKitKatTranslucency(this);
        deckJson = new JSONObject();
        Intent intent = getIntent();

        try{
            totalNewForCurrentDeck = intent.getIntExtra("totalNewForCurrentDeck",-1);
            totalRevForCurrentDeck = intent.getIntExtra("totalRevForCurrentDeck",-1);
            did = intent.getLongExtra("did",-1);
            deckJson.put("newMax",totalNewForCurrentDeck);
            deckJson.put("revMax",totalRevForCurrentDeck);
        }catch (JSONException e){
            e.printStackTrace();
        }

        webView = (WebView)findViewById(R.id.custom_study_webview);
        webView.loadUrl("file:///android_asset/allWebView/CustomStudy.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:initFirWindow('"+null+"','"+deckJson.toString()+"')");
                webView.loadUrl("javascript:changeAllColor('"+currentThemeTag+"')");
            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.addJavascriptInterface(new CustomStudyActivity.MyObject(), "xuming");
    }


    class MyObject{
        @JavascriptInterface
        public void comfirm(String data){
            Log.e("CustomStudyActivity",">>>>>>>comfirm>>>>>"  + data);
            int type;
            int number;
            try {
                JSONObject jsonObject = new JSONObject(data);
                type = Integer.parseInt(jsonObject.getString("type"));
                Collection col = CollectionHelper.getInstance().getCol(CustomStudyActivity.this);
                switch(type){
                    case 0:
//                        Toast.makeText(getApplicationContext(),"今天的新卡片上限数已保存", Toast.LENGTH_SHORT).show();
                        number = Integer.parseInt(jsonObject.getString("studyNew"));
                        JSONObject deck_1 = col.getDecks().get(did);
                        deck_1.put("extendNew", number);
                        col.getDecks().save(deck_1);
                        col.getSched().extendLimits(number, 0);
                        break;
                    case 1:
//                        Toast.makeText(getApplicationContext(),"今天的复习卡片上限数已保存", Toast.LENGTH_SHORT).show();
                        number = Integer.parseInt(jsonObject.getString("studyRev"));
                        JSONObject deck_2 = col.getDecks().get(did);
                        deck_2.put("extendRev", number);
                        col.getDecks().save(deck_2);
                        col.getSched().extendLimits(0, number);
                        break;
                    case 2:
//                        Toast.makeText(getApplicationContext(),"复习忘记的卡片数已保存", Toast.LENGTH_SHORT).show();
                        number = Integer.parseInt(jsonObject.getString("studyForget"));
                        JSONArray ar = new JSONArray();
                        try {
                            ar.put(0, 1);
                            createCustomStudySession(ar, new Object[]{String.format(Locale.US,
                                    "rated:%d:1", number), Consts.DYN_MAX_SIZE, Consts.DYN_RANDOM}, false);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 3:
//                        Toast.makeText(getApplicationContext(),"提前复习数已保存", Toast.LENGTH_SHORT).show();
                        number = Integer.parseInt(jsonObject.getString("studyAhead"));
                        createCustomStudySession(new JSONArray(), new Object[]{String.format(Locale.US,
                                "prop:due<=%d", number), Consts.DYN_MAX_SIZE, Consts.DYN_DUE}, true);
                        break;
                    case 4:
//                        Toast.makeText(getApplicationContext(),"预览新卡片数已保存", Toast.LENGTH_SHORT).show();
                        number = Integer.parseInt(jsonObject.getString("studyPreview"));
                        createCustomStudySession(new JSONArray(), new Object[]{"is:new added:" +
                                Integer.toString(number), Consts.DYN_MAX_SIZE, Consts.DYN_OLDEST}, false);
                        break;
                    case 5:
//                        Toast.makeText(getApplicationContext(),"按照卡片状态或标签学习数已保存", Toast.LENGTH_SHORT).show();
                        number = Integer.parseInt(jsonObject.getString("studyRandom"));
                        //尚未实现，待实现

                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CustomStudyActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });
        }

        @JavascriptInterface
        public void backToParent(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CustomStudyActivity.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });
        }
    }


    /**
     * Create a custom study session
     * @param delays delay options for scheduling algorithm
     * @param terms search terms
     * @param resched whether to reschedule the cards based on the answers given (or ignore them if false)
     */
    private void createCustomStudySession(JSONArray delays, Object[] terms, Boolean resched) {
        JSONObject dyn;
        final AnkiActivity activity = (AnkiActivity) getParent();
        Collection col = CollectionHelper.getInstance().getCol(CustomStudyActivity.this);
        try {
            String deckName = col.getDecks().get(did).getString("name");
            String customStudyDeck = "自定义学习的进程";
            JSONObject cur = col.getDecks().byName(customStudyDeck);
            if (cur != null) {
                if (cur.getInt("dyn") != 1) {
                    Toast.makeText(getApplicationContext(), "请先重命名现有的自定义学习牌组。", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // safe to empty
                    col.getSched().emptyDyn(cur.getLong("id"));
                    // reuse; don't delete as it may have children
                    dyn = cur;
                    col.getDecks().select(cur.getLong("id"));
                }
            } else {
                long customStudyDid = col.getDecks().newDyn(customStudyDeck);
                dyn = col.getDecks().get(customStudyDid);
            }
            // and then set various options
            if (delays.length() > 0) {
                dyn.put("delays", delays);
            } else {
                dyn.put("delays", JSONObject.NULL);
            }
            JSONArray ar = dyn.getJSONArray("terms");
            ar.getJSONArray(0).put(0, "deck:\"" + deckName + "\" " + terms[0]);
            ar.getJSONArray(0).put(1, terms[1]);
            ar.getJSONArray(0).put(2, terms[2]);
            dyn.put("resched", resched);
            // Rebuild the filtered deck
            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_REBUILD_CRAM, new DeckTask.TaskListener() {
                @Override
                public void onCancelled() {
                }

                @Override
                public void onPreExecute() {

                }

                @Override
                public void onPostExecute(DeckTask.TaskData result) {
                    ((CustomStudyListener) activity).onCreateCustomStudySession();
                }

                @Override
                public void onProgressUpdate(DeckTask.TaskData... values) {
                }
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public interface CustomStudyListener {
        void onCreateCustomStudySession();
        void onExtendStudyLimits();
    }
}
