package com.ichi2yiji.anki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.bean.TranslateBean;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.CopyRawToDataForInitDeck;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.async.DeckTask;
import com.ichi2yiji.common.Constants;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Models;
import com.ichi2yiji.libanki.Note;
import com.ichi2yiji.utils.StorageUtil;


import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;

/**
 * 制卡界面
 */
public class DictionaryPage extends AnkiActivity implements View.OnClickListener {

    private WebView webView;
    private String sentence;
    // 句子翻译
    private String sentenceZh;
    private Note note;
    private String bookname;
    /** 数据初始化完成 true:已完成    false:未完成 **/
    private boolean noteHasInit;
    private int pressTimes;
    private ArrayList<String> fields;
    private int type;
    private String word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        startLoadingCollection();
        setContentView(R.layout.activity_dictionary_page);
        ApplyTranslucency.applyKitKatTranslucency(this);

        revMsg();
        initView();
        initWebView();
    }

    private void revMsg() {
        Intent intent = getIntent();
        word = intent.getStringExtra("TEXT")
                .replace(",", "")
                .replace(".", "")
                .replace("...", "")
                .replace("?", "")
                .replace("'", "")
                .replace("。","")
                .replace(":","");
        sentence = intent.getStringExtra("sentence");
        bookname = intent.getStringExtra("BOOK_NAME");
        type = intent.getIntExtra("type", 0);

        translateSentence();
    }

    private void initView() {
        TextView title = (TextView)findViewById(R.id.word);
        TextView back = (TextView)findViewById(R.id.text_back);
        TextView auto_make_card = (TextView)findViewById(R.id.auto_make_card);

        title.setText(word);
        back.setOnClickListener(this);
        auto_make_card.setOnClickListener(this);
    }

    /**
     * 英文制卡
     */
    private void translateSentence() {
        HashMap<String, String> map = new HashMap<>();
        map.put("query",sentence);
        map.put("from","en");
        map.put("to","zh");
        ZXUtils.Post(Urls.URL_TRANSLATE, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                TranslateBean translateBean = gson.fromJson(result, TranslateBean.class);
                sentenceZh = translateBean.getRet().getTrans_result().get(0).getDst();
                LogUtil.e("onSuccess: sentenceZh = " + sentenceZh);
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

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView_dictionary_page);
        webView.loadUrl("http://cn.bing.com/dict/search?q=" + word);
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
                changeHead();
                view.loadUrl("javascript:window.xuming.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
        });
        webView.addJavascriptInterface(new MyObject(), "xuming");
    }


    @Override
    protected void onCollectionLoaded(final Collection col) {
        super.onCollectionLoaded(col);
        //初始化Note
        final Models models = col.getModels();
        JSONObject map_model = null;
        if (type == 1) {
            // 英文制卡
            map_model = models.get(Constants.MODEL_SIMPLE);
        }else{
            map_model = models.get(Constants.MODEL_BLANK);
        }
        if (map_model == null) {
            Toast.makeText(this, "model is null", Toast.LENGTH_SHORT).show();
            return;
        }
        CopyRawToDataForInitDeck.save(map_model.toString(),Environment.getExternalStorageDirectory()+"/","map_model.txt");
        note = new Note(col, map_model);
    }

    private Map<String, String> getNeedElementMap(){
        Log.e("getNeedElementMap", "getNeedElementMap>>>>>>>>>>>>>>>>>done!");
        Map<String, String> map = new HashMap<>();
        //  //*[@id='headword']/h1/strong;
        map.put("word","//div[@id='headword']/h1/strong");
        // //html/body/div[1]/div/div/div[1]/div[1]/div[1]/div[2]/div/div[1]
        map.put("yinbiao_us","//html/body/div[1]/div/div/div[1]/div[1]/div[1]/div[2]/div/div[1]");
        //  //html/body/div[1]/div/div/div[1]/div[1]/ul/li[1]/span[1]
        map.put("cixing1","//html/body/div[1]/div/div/div[1]/div[1]/ul/li[1]/span[1]");
        // //html/body/div[1]/div/div/div[1]/div[1]/ul/li[1]/span[2]/span
        map.put("shiyi1","//html/body/div[1]/div/div/div[1]/div[1]/ul/li[1]/span[2]/span");
        // /html/body/div[1]/div/div/div[1]/div[1]/ul/li[2]/span[1]
        map.put("cixing2","//html/body/div[1]/div/div/div[1]/div[1]/ul/li[2]/span[1]");
        // /html/body/div[1]/div/div/div[1]/div[1]/ul/li[2]/span[2]/span
        map.put("shiyi2","//html/body/div[1]/div/div/div[1]/div[1]/ul/li[2]/span[2]/span");
        // /html/body/div[1]/div/div/div[1]/div[1]/div[1]/div[2]/div/div[2]/a
        map.put("fayin_us","//html/body/div[1]/div/div/div[1]/div[1]/div[1]/div[2]/div/div[2]/a");
        map.put("imageDivs","//html/body/div[1]/div/div/div[1]/div[1]/div[2]/div");
        return map;
    }

    private void changeHead(){
        String js1 = "document.body.removeChild(document.getElementById(\"b_header\"));";
        webView.loadUrl("javascript:" + js1);
        String js2 = "var pags = document.getElementsByClassName(\"bi_pag\");var footers = document.getElementsByClassName(\"b_footer\");var pagP = pags[0].parentNode;pagP.removeChild(pags[0]);var footerP = footers[0].parentNode;footerP.removeChild(footers[0]);";
        webView.loadUrl("javascript:" + js2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_back:
                // 返回
                DictionaryPage.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                break;
            case R.id.auto_make_card:
                // 自动制卡
                autoMakeCard();
                break;
        }

    }

    /**
     * 自动制卡
     */
    private void autoMakeCard() {
        if (noteHasInit && (pressTimes==0 ||pressTimes==1)){

            if(type == 1){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AddNoteInAnyWindow.addNote(Constants.MODEL_SIMPLE,bookname,fields);
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AddNoteInAnyWindow.addNote(Long.parseLong("1424386168963"),bookname,fields);
                    }
                });
            }
            noteHasInit = false;
            pressTimes++;
        }else if (!noteHasInit && pressTimes ==0){
            Toast.makeText(getApplicationContext(), "您的手速太快啦，再点一下吧",Toast.LENGTH_SHORT).show();
            pressTimes++;
        }else {
            Toast.makeText(getApplicationContext(), "您已制作过这个卡片了",Toast.LENGTH_SHORT).show();
        }
    }

    class MyObject {
        @JavascriptInterface
        public void showSource(String html) {
            Log.e("HTML", "html>>>>>>>>>" + html);
            //获取网页HTML代码
//            save(html, Environment.getExternalStorageDirectory().getAbsolutePath(),"bing.html");
            Map<String, String> map = getNeedElementMap();
            // 单词
            String word = getNeedElementText(html,map.get("word"));
            // 音标
            String yinbiao_us = getNeedElementText(html,map.get("yinbiao_us"));
            yinbiao_us = processYinbiao(yinbiao_us);
            // 词性1
            String cixing1 = getNeedElementText(html,map.get("cixing1"));
            // 释义1
            String shiyi1 = getNeedElementText(html,map.get("shiyi1"));
            // 词性2
            String cixing2 =  getNeedElementText(html,map.get("cixing2"));
            // 释义2
            String shiyi2 = getNeedElementText(html,map.get("shiyi2"));
            // 发音
            String fayin_us = getFaYin(html,map.get("fayin_us"));
            // 图片
            String imageDivs = getNeedElementText(html,map.get("imageDivs"));
            Log.e("showSource", "word>>>>>>>>>>>>>>>>>" + word);
            Log.e("showSource", "yinbiao_us>>>>>>>>>>>>>>>>>" + yinbiao_us);
            Log.e("showSource", "cixing1>>>>>>>>>>>>>>>>>" + cixing1);
            Log.e("showSource", "shiyi1>>>>>>>>>>>>>>>>>" + shiyi1);
            Log.e("showSource", "cixing2>>>>>>>>>>>>>>>>>" + cixing2);
            Log.e("showSource", "shiyi2>>>>>>>>>>>>>>>>>" + shiyi2);
            Log.e("showSource", "fayin_us>>>>>>>>>>>>>>>>>" + fayin_us);
            Log.e("showSource", "imageDivs>>>>>>>>>>>>>>>>>" + imageDivs);

            if (type == 1){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("word", word);
                    jsonObject.put("st", sentence);
                    jsonObject.put("sttr", sentenceZh);
                    jsonObject.put("mean_cn", cixing1 + shiyi1);
                    jsonObject.put("audio", fayin_us);
                    jsonObject.put("accent", yinbiao_us);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fields = new ArrayList<>();
                fields.add(word);
                fields.add(jsonObject.toString());
                String format = String.format("[sound:%s.mp3]", word);
                fields.add(format);
                bookname = bookname.replaceAll("@@@[0-9]", "");
                downMp3(fayin_us,word);
                noteHasInit = true;
                return;
            }else{
                //制卡数据传递
                //后期添加"例句翻译，拓展，词组短语"
                note.setField(0,word);
                note.setField(1,yinbiao_us);
                note.setField(2,cixing1);
                note.setField(3,shiyi1);
                note.setField(4,cixing2);
                note.setField(5,shiyi2);
                note.setField(6,fayin_us);
                note.setField(7,sentence);
                note.setField(8,"");
                note.setField(9,"");
                Log.e("showSource", "note>>>>>>>>>>>>>>>>>" + note);
                noteHasInit = true;

                fields = new ArrayList<>();
                fields.add(word);
                fields.add(yinbiao_us);
                fields.add(cixing1);
                fields.add(shiyi1);
                fields.add(cixing2);
                fields.add(shiyi2);
                fields.add(fayin_us);
                fields.add(sentence);
                fields.add("");
                fields.add("");
            }
        }
    }

    /**
     * 处理音标字符串
     * @param yinbiao_us
     * @return
     */
    private String processYinbiao(String yinbiao_us) {
        String re1 = ".*?";	// Non-greedy match on filler
        String re2 = "(\\[.*?\\])";	// Square Braces 1
        String sbraces1 = yinbiao_us;

        Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(yinbiao_us);
        if (m.find()){
            sbraces1 = m.group(1);
        }
        return sbraces1.toString();
    }

    private void downMp3(String path, String word) {
        final String fileName = word + ".mp3";
        // final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirTests/" +filename;
        String filepath = StorageUtil.getAppCustomCacheDirectory(Constants.APP_CACHE_DIR_COLLECTION).getAbsolutePath() + File.separator + fileName;
        ZXUtils.DownLoadFile(path, filepath, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.e("downloadTodirTests", "onSuccess");
                // Toast.makeText(DictionaryPage.this, fileName.replace(".mp3","")+" 下载成功", Toast.LENGTH_SHORT).show();
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
                // Toast.makeText(DictionaryPage.this, "finished", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * JsoupXpath的代码
     */
    private String getNeedElementText(String html, String xpath){
        Log.e("getNeedElementText", "getNeedElementText>>>>>>>>>>>>>>>>>start!");
        JXDocument jxDocument = new JXDocument(html);
        List<Object> rs = null;
        String data = null;
        try {
            rs = jxDocument.sel(xpath);
            for (Object o:rs){
                if (o instanceof Element){
                    data = ((Element) o).text();
//                    Log.e("showSource", "data>>>>>>>>>>>>>>>>>" + data);
                }

            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
        Log.e("getNeedElementText", "getNeedElementText>>>>>>>>>>>>>>>>>over!");
        return data;
    }

    private String getFaYin(String html, String xpath){
        Element element = getNeedElement(html, xpath);
        String s = element.toString();
        String regEx = ".*Click\\(this,'(.*)','";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(s);
        boolean find = mat.find();
        if(find){
            return mat.group(1);
        }else{
            LogUtil.e("getFaYin: 未找到发音链接");
        }
        return "";
    }

    private Element getNeedElement(String html, String xpath){
        JXDocument jxDocument = new JXDocument(html);
        List<Object> rs = null;
        String data = null;
        try {
            rs = jxDocument.sel(xpath);
            for (Object o:rs){
                if (o instanceof Element){
                    return ((Element) o);
                }
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DeckTask.TaskListener mRenderQAHandler = new DeckTask.TaskListener() {
        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
        }


        @Override
        public void onPreExecute() {
            showProgressBar();
        }


        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            if (result != null) {
                Toast.makeText(getApplicationContext(), "制卡成功", Toast.LENGTH_SHORT).show();
                hideProgressBar();
            } else {
            }
        }


        @Override
        public void onCancelled() {
            hideProgressBar();
        }
    };

    private static void save(String data, String outputPath, String outputName){
        FileOutputStream out = null;
        BufferedWriter write = null;
        File file = new File(outputPath + "/" + outputName);

        try {
            out = new FileOutputStream(file);
            write = new BufferedWriter(new OutputStreamWriter(out));
            write.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(write != null){
                    write.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
