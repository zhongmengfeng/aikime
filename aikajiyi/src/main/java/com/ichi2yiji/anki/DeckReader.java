package com.ichi2yiji.anki;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ankireader.CopyRawtodata;
import com.ankireader.model.Book;
import com.ankireader.util.ViewShot;
import com.chaojiyiji.geometerplus.android.fbreader.FBReader;
import com.chaojiyiji.geometerplus.android.fbreader.libraryService.BookCollectionShadow;
import com.chaojiyiji.geometerplus.fbreader.Paths;
import com.chaojiyiji.geometerplus.fbreader.fbreader.FBReaderApp;
import com.chaojiyiji.geometerplus.zlibrary.core.filesystem.ZLFile;
import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.util.ZXUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 金鹏 on 2016/12/16.
 */

public class DeckReader extends NavigationDrawerActivity {
    private WebView webView;
    private FBReaderApp myFBReaderApp;
    File dirBooks;
    File downloadBooks;
    private Receiver myReceiver;
    private  static final int DownloadBooks_Result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SQLException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deck_reader);
        Log.e("DeckReader", "onCreate");

        applyKitKatTranslucency();
        View mainView = findViewById(android.R.id.content);//设置侧滑菜单
        initNavigationDrawer(mainView);//设置侧滑菜单

       // PushManager.getIntance(),initialize(this.getApplicationContext());
        initWebView();

        myFBReaderApp = (FBReaderApp) FBReaderApp.Instance();
        if (myFBReaderApp == null) {
            myFBReaderApp = new FBReaderApp(Paths.systemInfo(this),
                    new BookCollectionShadow());
        }
        getCollection().bindToService(this, null);

        myReceiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("com.ankireader.ReceivedUrlFromPush");
        registerReceiver(myReceiver, intentFilter);

        getShareBooks();


    }

    /**
     * 初始化WebView
     */
    private void initWebView(){
        webView=(WebView)findViewById(R.id.webView_reader);
        webView.loadUrl("file:///android_asset/testMUI_888.01/AA08_deckReader_1.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadBookList();//自动扫描指定文件夹下txt文件并初始化WebView书籍列表页面

                //初始化在线导入的红点的显示
                boolean showmark = hasFilesInDownloadBooks();
                if (showmark){
                    webView.loadUrl("javascript:showmark()");
                }else {
                    webView.loadUrl("javascript:hidemark()");
                }

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                String testurl = pref.getString("URLTOREADER", "");
                Log.e("testurl", ">>>>>>>>>>>>" + testurl);
                if(pref.getString("URLTOREADER", "") != null && pref.getString("URLTOREADER", "") != ""){
                    DownloadFromBackstage(pref.getString("URLTOREADER", ""));
                    editor.remove("URLTOREADER");

                    editor.commit();
                }
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
     * 在此广播接收器中进行book的下载，并更新UI
     */
    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("DeckReader-onReceive", ">>>>>>>>>>onReceived!");
            Toast.makeText(getApplicationContext(), "有新的书籍可以在线导入了！", Toast.LENGTH_SHORT).show();
            String url = intent.getStringExtra("URL");
            DownloadFromBackstage(url);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = pref.edit();
            if(pref.getString("URLTOREADER", "") != null && pref.getString("URLTOREADER", "") != ""){
                editor.remove("URLTOREADER");
                editor.commit();
            }
        }
    }

    /**
     * 获取WebView的截图
     */
    private void runViewShot(View view){
        Bitmap bitmap = ViewShot.getShot(view);
        ViewShot.saveBitmap(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture", "MyPic", bitmap);
        Toast.makeText(this, "截图已保存！",Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取整个屏幕的截图
     */
    private void runScreenShot(Activity context){
        Bitmap bitmap = ViewShot.captureScreen(context);
        ViewShot.saveBitmap(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/picture", "MyPic", bitmap);
        Toast.makeText(this, "截图已保存！",Toast.LENGTH_SHORT).show();
    }


    /**
     * 以下为自动扫描指定文件夹下txt文件的代码
     */
    public String getTXTFiles(){
        final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e("ROOT_PATH>>>>", ROOT_PATH);
        dirBooks = new File(ROOT_PATH + "/Chaojiyiji/dirBooks");
        downloadBooks = new File(ROOT_PATH + "/Chaojiyiji/downloadBooks");
        if (!dirBooks.exists()) {
            boolean b = dirBooks.mkdirs();
//            Log.e("dirBooks", String.valueOf(b));
        }
        if (!downloadBooks.exists()) {
            boolean b =downloadBooks.mkdirs();
//            Log.e("downloadBooks", String.valueOf(b));
        }

        //在dirBooks文件夹下预放几本书籍
        CopyRawtodata.readFromRaw(DeckReader.this, com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.aesopsfables, ROOT_PATH + "/Chaojiyiji/dirBooks", "Aesop's Fables.txt");
        CopyRawtodata.readFromRaw(DeckReader.this, com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.gonewiththewind, ROOT_PATH + "/Chaojiyiji/dirBooks", "GONE WITH THE WIND.txt");
        CopyRawtodata.readFromRaw(DeckReader.this, com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.ian, ROOT_PATH + "/Chaojiyiji/dirBooks", "ian.txt");
        CopyRawtodata.readFromRaw(DeckReader.this, com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.mmook, ROOT_PATH + "/Chaojiyiji/dirBooks", "mmook.txt");


        File[] files = dirBooks.listFiles();
//        Log.e("files", files.toString());
        List<String[]> list = new ArrayList<>();
        if (files != null){
            for(int i = 0; i < files.length; i++){
                String [] book = new String[2];
                if (files[i].getName().toLowerCase().endsWith(".txt")){
                    book[0] = files[i].getName();
                    book[1] = files[i].getAbsolutePath();
                }
                list.add(book);
            }
            Log.e("DeckReader--list", list.toString());
        }

        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.e("DeckReader--json", json);
        return json;

    }

    /**
     * 原生调用JS方法
     */
    private void loadBookList(){
        String bookJson = getTXTFiles();
        webView.loadUrl("javascript:initFirWindow('"+null+"', '"+bookJson+"')");
    }

    /**
     * 从后台下载文件到downloadBooks目录下，下载成功后更新UI
     * @param url 推送透传过来的下载地址
     */
    private void  DownloadFromBackstage(String url){
        if(url.lastIndexOf("/") != -1) {
            String filename = url.substring(url.lastIndexOf("/"));//获取文件名
            String filepath = downloadBooks.getAbsolutePath() + "/" + filename;//下载文件的存储路径,最后一个“/”后的为文件名
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String mem_id = pref.getString("MEM_ID","");
            Map<String, String> map = new HashMap<>();
            map.put("good_id", "160");//需要后台提供数据
            map.put("mem_id", mem_id);
            com.ankireader.util.ZXUtils.DownLoadFile2(url, map, filepath, new Callback.CommonCallback<File>() {

                @Override
                public void onSuccess(File result) {
                    Log.e("文件下载的回调onSuccess", "下载成功 ");
                    String filename = result.getName();
                    Log.e("文件下载的回调onSuccess", filename);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("文件下载的回调onError", ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.e("文件下载的回调onCancelled", cex.toString());

                }

                @Override
                public void onFinished() {
                    Log.e("文件下载的回调onFinished", "finish");
                    boolean showmark = hasFilesInDownloadBooks();
                    if (showmark) {
                        webView.loadUrl("javascript:showmark()");
                    } else {
                        webView.loadUrl("javascript:hidemark()");
                    }
                }
            });
        }else {
            Log.e("DownloadFromBackstage", "从后台获取的url地址有错误");
        }
    }


    /**
     * 判断downloadBooks文件夹下是否存在文件
     */
    private boolean hasFilesInDownloadBooks(){
        File[] files = downloadBooks.listFiles();
        if(files.length == 0 || files == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        Log.e("DeckReader", "onStart");
        super.onStart();
//        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.e("DeckReader", "onResume");
        super.onResume();

        //以下为接收上一次阅读的书籍阅读进度的代码
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Float progress = pref.getFloat("BookProgress", 0);
        String bookPath = pref.getString("BookPath", "");
        String bookProgress = String.valueOf((int)(progress*100)) + "%";
        Log.e("bookPath", ">>>>>>>>>>>>>>>in DeckReader  is " +bookPath);
        Log.e("bookProgress", ">>>>>>>>>>>>in DeckReader  is " +bookProgress);
    }

    @Override
    protected void onStop() {
        Log.e("DeckReader", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("DeckReader", "onDestroy");
        super.onDestroy();
        getCollection().unbind();
        unregisterReceiver(myReceiver);

    }

    private com.chaojiyiji.geometerplus.fbreader.book.Book createBookForFile(ZLFile file) {
        if (file == null) {
            return null;
        }
        com.chaojiyiji.geometerplus.fbreader.book.Book book = getCollection()
                .getBookByFile(file.getPath());
        if (book != null) {
            return book;
        }
        if (file.isArchive()) {
            for (ZLFile child : file.children()) {
                book = getCollection().getBookByFile(child.getPath());
                if (book != null) {
                    return book;
                }
            }
        }
        return null;
    }

    private BookCollectionShadow getCollection() {
        return (BookCollectionShadow) myFBReaderApp.Collection;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case DownloadBooks_Result:
                if (resultCode == RESULT_OK){
//                    Intent intent = getIntent();
//                    String path = intent.getStringExtra("PathFromDownLoadBooks");
//                    String filename = path.substring(path.lastIndexOf("/")+1);
                    loadBookList();
                }
                break;
            default:
        }
    }

    class  MyObject {

        @JavascriptInterface
        public void importDownloadBook() {
            Log.i("importDownloadBook", "importDownloadBook");
            //将downloadBooks目录下的文件拷贝至dirBooks
            File[] files = downloadBooks.listFiles();
            if (files != null){
                for(int i = 0; i < files.length ; i++){
                    String filename = files[i].getName();
                    File source  = new File(downloadBooks.getAbsolutePath(), filename);
                    File destination  = new File(dirBooks.getAbsolutePath(), filename);
                    if(source.exists()){
                        try {
                            FileChannel src = new FileInputStream(source).getChannel();
                            FileChannel dst = new FileOutputStream(destination).getChannel();
                            dst.transferFrom(src, 0 , src.size());
                            src.close();
                            dst.close();
                            files[i].delete();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //更新UI，使HTML页面重新刷新一遍
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean showmark = hasFilesInDownloadBooks();
                    if (showmark){
                        webView.loadUrl("javascript:showmark()");
                    }else {
                        webView.loadUrl("javascript:hidemark()");
                    }
                    loadBookList();
                }
            });
        }

        @JavascriptInterface
        public void showSliderMenu(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toOpenDrawer();
                }
            });
        }

        @JavascriptInterface
        public void readerHelp() {
            Log.i("readerHelp", "readerHelp");
        }

        @JavascriptInterface
        public void sharedBooks() {
            Log.i("sharedBooks", "sharedBooks");
            Intent intent = new Intent(DeckReader.this, DownloadBooks.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, DownloadBooks_Result);
        }

        @JavascriptInterface
        public void beginReadingBook(String path) {
            Log.i("beginReadingBook", "beginReadingBook");
            Book b = new Book();
            b.setBookPath(path);

            ZLFile file = ZLFile.createFileByPath(b.getBookPath());
            final com.chaojiyiji.geometerplus.fbreader.book.Book book = createBookForFile(file);
            if (book != null) {
                FBReader.openBookActivity(DeckReader.this, book,
                        null);
            }else{
                Log.i("DeckReader", "book == null");
            }
        }

        @JavascriptInterface
        public void deleteOneBook(String curBookName) {
            Log.i("deleteOneBook", "deleteOneBook");
        }

        @JavascriptInterface
        public void switch_decks() {
            Log.i("switch_decks", "switch_decks");
            Intent intent = new Intent(DeckReader.this, DeckPicker.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            DeckReader.this.finish();
            //dx  add
            overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
        }

        @JavascriptInterface
        public void switch_test() {
            Log.i("switch_test", "switch_test");
            Log.i("switch_reader", "switch_reader");
            Intent intent = new Intent(DeckReader.this, DeckTest.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            DeckReader.this.finish();
            //dx  add
            overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
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

    private void getShareBooks(){
//        String url = "https://www.ankichina.net/Home/Index/Apptexts.html";
//        String url = AnkiDroidApp.BASE_DOMAIN + "Home/Index/Apptexts.html";
        String url = "https://shopcz.ankichina.net/Home/Index/Apptexts.html";
        Map<String, String> map = new HashMap<>();
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getShareBooks","onSuccess>>>>>>>"+ result);
                try {
                    JSONObject jo = new JSONObject(result);
                    JSONArray ja = jo.getJSONArray("data");
                    String basepath = jo.getString("basepath");
                    List<String> list = new ArrayList<String>();
                    JSONArray jsonArrayout = new JSONArray();
                    for(int i = 0; i<ja.length();i++){
                        String deck = ja.getJSONObject(i).getString("deck");
                        String deckname = ja.getJSONObject(i).getString("deckname");
//                        String[] in = new String[2];
//                        in[0] = deckname.replace(".txt","");
//                        in[1] = basepath + deck;
//                        Log.e("getShareBooks","onSuccess>>>>>in>>>>>>>"+ Arrays.toString(in));
//                        list.add(Arrays.toString(in));

                        JSONArray jsonArrayin = new JSONArray();
                        jsonArrayin.put(deckname.replace(".txt",""));
                        jsonArrayin.put(basepath + deck);
                        jsonArrayout.put(jsonArrayin);
                    }
                    Log.e("getShareBooks","onSuccess>>>>>list>>>>>>>"+ list);
                    Log.e("getShareBooks","onSuccess>>>>>jsonArrayout>>>>>>>"+ jsonArrayout);
                    SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("SHAREBOOKDATA",jsonArrayout.toString());
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("getShareBooks","onError>>>>>>>"+ ex);
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
