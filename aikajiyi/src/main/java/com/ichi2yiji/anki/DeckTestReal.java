package com.ichi2yiji.anki;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.CopyRawToDataForInitDeck;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.common.Constants;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Models;
import com.ichi2yiji.libanki.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 模考 - item
 * Created by 金鹏 on 2016/12/16.
 */

public class DeckTestReal extends AnkiActivity {

    private WebView webView;
    private JSONArray jsonData;
    private Note mEditorNote;
    private ArrayList<String> fields;
    private JSONArray dataList;

    private String testData;
    private String testFullName;

    private DeckTask.TaskListener mRenderQAHandler = new DeckTask.TaskListener() {
        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
        }


        @Override
        public void onPreExecute() {
            //showProgressBar();
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (result != null) {
                //Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                //hideProgressBar();
            } else {
            }
        }


        @Override
        public void onCancelled() {
            //hideProgressBar();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        ThemeChangeUtil.changeTheme(this);
        final int currentThemeTag = ThemeChangeUtil.getCurrentThemeTag(this);
        Log.e("DeckTestReal", "currentThemeTag>>>>>>>>" + currentThemeTag);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_testreal);
        startLoadingCollection();
        Log.e("DeckTestReal", "onCreate");
        ApplyTranslucency.applyKitKatTranslucency(this);

       // requestData();//请求驾考数据

        Intent intent = getIntent();
        testData = intent.getStringExtra("testData");
        testFullName = intent.getStringExtra("testFullName");
        Log.e("DeckTestReal","testData>>>>>>>>>>>>" + testData);

        try {
            dataList = new JSONArray(testData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //初始化
        webView = (WebView) findViewById(R.id.webView_decktestreal);
//        webView.loadUrl("file:///android_asset/testMUI_888.01/AA06_realTesting_1.html");
        webView.loadUrl("file:///android_asset/allWebView/AA06_realTesting_1.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("DeckTestReal","onPageFinished>>>>>>>>>>>>>" );
//                webView.loadUrl("javascript:initFirWindow('"+null+"', '"+jsonData+"')");
                webView.loadUrl("javascript:initFirWindow('"+null+"', '"+ testData +"', '"+currentThemeTag+"')");
//                webView.loadUrl("javascript:initFirWindow('"+null+"', '"+dataList+"', '"+currentThemeTag+"')");

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
        webView.addJavascriptInterface(new MyObject(), "xuming");


    }

    @Override
    protected void onCollectionLoaded(Collection col) {
        super.onCollectionLoaded(col);

    }

    private void requestData(){
        Map<String, String> map = new HashMap<>();
//        String url = "https://ankichina.net/Home/Index/get_jiakao_question.html";
//        String url = "https://ankichina.net/Home/App/get_jiakao_question/";
        String url = AnkiDroidApp.BASE_DOMAIN + "Home/App/get_jiakao_question/";
        ZXUtils.Post(url, map, new Callback.CommonCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray result) {
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
                webView.loadUrl("javascript:initFirWindow('"+null+"', '"+jsonData+"')");
            }
        });
    }

    public void saveNote(long modleId, String deckName, ArrayList<String> fields) {

        Collection col = getCol();

        Models models = col.getModels();
        JSONObject map_model = models.get(modleId);
        mEditorNote = new Note(col,map_model);
        try {
            long mCurrentDid = col.getDecks().id(deckName,true);
            mEditorNote.model().put("did",String.valueOf(mCurrentDid));
            col.getModels().setChanged();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0;i<fields.size();i++){
            updateField(i,fields.get(i));
        }
        Log.e("AddNoteInAnyWindow", "mEditorNote>>>>>>>model>>>>>>" + mEditorNote.model());
        CopyRawToDataForInitDeck.save(mEditorNote.model().toString(), Environment.getExternalStorageDirectory().getAbsolutePath(), "map_model");
        Log.e("AddNoteInAnyWindow", "mEditorNote>>>>>>>getFields>>>>>" + Arrays.toString(mEditorNote.getFields()));
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ADD_FACT, mRenderQAHandler,
                new DeckTask.TaskData(mEditorNote));
    }



    private boolean updateField(int position,String field) {
        String newValue = field;
        if (!mEditorNote.values()[position].equals(newValue)) {
            mEditorNote.values()[position] = newValue;
            return true;
        }
        return false;
    }



    class MyObject {
        @JavascriptInterface
        public void backToParent() {
            Log.e("backToParent", "backToParent");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DeckTestReal.this.finish();
                    overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                }
            });

        }

        @JavascriptInterface
        public void autoMakeCard(String data){
            // 1489568927295
            // ios:1489568927295 模考模板id

            Log.e("DeckTestReal", "autoMakeCard>>>>>>>" + data);
            JSONObject fields = null;
            final ArrayList<String> fieldList = new ArrayList<>();
            int index = Integer.parseInt(data);
            try {
                fields = dataList.getJSONObject(index);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (testFullName.contains("__")) {
                testFullName = testFullName.replace("__", "::");
            }
            String json = fields.toString();

            System.out.println(json);
            json = json.replaceAll("\"","''");
            json = String.format("<div><div>%s</div></div>",json);
            System.out.println(json);

            fieldList.add(json);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AddNoteInAnyWindow.addNote(Constants.MODEL_CHOOSE,testFullName,fieldList);
                }
            });
            // AddNoteInAnyWindow.addNote(Long.parseLong("1489568927295"),testFullName,fieldList);
            // AddNoteInAnyWindow addNoteInAnyWindow = new AddNoteInAnyWindow(DeckTestReal.this);
            // addNoteInAnyWindow.saveNote(Long.parseLong("1424386168963"),testFullName,fields);


            /*fieldList.add("正面");
            fieldList.add("背面");
            AddNoteInAnyWindow.addNote(Long.parseLong("1491530869934"),testFullName,fieldList);*/

            Log.e("DeckTestReal", "autoMakeCard>>>>>>>fields>>>" + fields);



        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("DeckTestReal", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("DeckTestReal", "onDestroy");
    }

}
