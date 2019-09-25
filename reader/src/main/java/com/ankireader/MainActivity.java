package com.ankireader;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import com.ankireader.Adapter.ListViewAdapter;
import com.ankireader.model.Book;
import com.ankireader.util.ViewShot;
import com.ankireader.util.ZXUtils;
import com.google.gson.Gson;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import com.chaojiyiji.geometerplus.android.fbreader.FBReader;
import com.chaojiyiji.geometerplus.android.fbreader.libraryService.BookCollectionShadow;
import com.chaojiyiji.geometerplus.fbreader.Paths;
import com.chaojiyiji.geometerplus.fbreader.fbreader.FBReaderApp;
import com.chaojiyiji.geometerplus.zlibrary.core.filesystem.ZLFile;
import com.chaojiyiji.geometerplus.zlibrary.core.util.RationalNumber;
import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;
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

public class MainActivity extends NavigationDrawerActivity {

    private FBReaderApp myFBReaderApp;
    private List<Book> bookList = new ArrayList<Book>();
    private float bookProgress = 0;
    ListViewAdapter adapter;
    ListView listView;
    RationalNumber progress = null;
    private WebView webView;
    File dirBooks;
    File downloadBooks;
    private int code;
    private String data;
    private static final int REQUEST_PERMISSION = 0;
    private Receiver myReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_webview);
        applyKitKatAndLollipopTranslucency();//设置沉浸式状态栏
        View mainView = findViewById(android.R.id.content);//设置侧滑菜单
        initNavigationDrawer(mainView);//设置侧滑菜单

        /**
         * 判断权限，初始化个推SDK
         */
        // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
        Log.d("GetuiSdkDemo", "initializing sdk...");
        PackageManager pkgManager = getPackageManager();
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {

            initWebView();
        }


//        Button btn = (Button) findViewById(R.id.button);

        /**
         * 以下为从raw读写文件的代码
         */
        /*
        CopyRawtodata.readFromRaw(MainActivity.this, R.raw.ian, "ian.txt");
        CopyRawtodata.readFromRaw(MainActivity.this, R.raw.text, "text.txt");
        */


        myFBReaderApp = (FBReaderApp) FBReaderApp.Instance();
        if (myFBReaderApp == null) {
            myFBReaderApp = new FBReaderApp(Paths.systemInfo(this),
                    new BookCollectionShadow());
        }
        getCollection().bindToService(this, null);
        Log.e("MainActivity", "onCreate");
        Log.e("MainActivity", "context = getApplication() 已执行");

        /**
         * 以下为首页加载一个Button的代码
         */
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("MainActivity", "onClick1");
//
//                Book b = new Book();
//                b.setBookPath("/data/data/com.chaojiyiji.geometerplus.zlibrary.ui.android/files/ian.txt");
//
//                ZLFile file = ZLFile.createFileByPath(b.getBookPath());
//                final com.chaojiyiji.geometerplus.fbreader.book.Book book = createBookForFile(file);
//                if (book != null) {
//                    FBReader.openBookActivity(MainActivity.this, book,
//                            null);
//                }else{
//                    Log.i("MainActivity", "book == null");
//                }
//            }
//        });

        /**
         * 以下为首页加载ListView的代码
         */
        /*
        Book b1 = new Book();
        b1.setBookPath("/data/data/com.chaojiyiji.geometerplus.zlibrary.ui.android/files/ian.txt");
        b1.setBookName("ian.txt");
        b1.setBookProgress("0%");
        bookList.add(b1);
        Book b2 = new Book();
        b2.setBookPath("/data/data/com.chaojiyiji.geometerplus.zlibrary.ui.android/files/text.txt");
        b2.setBookName("text.txt");
        bookList.add(b2);
        b2.setBookProgress("0%");

        adapter = new ListViewAdapter(MainActivity.this, R.layout.main_listview_item, bookList);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book b = bookList.get(position);
                Log.e("MainActivity", "Book b " + b.getBookProgress());
                ZLFile file = ZLFile.createFileByPath(b.getBookPath());
                final com.chaojiyiji.geometerplus.fbreader.book.Book book = createBookForFile(file);
                book.getId();
                progress = book.getProgress();
                bookProgress = progress.toFloat();
                b.setBookProgress(String.valueOf((int)(bookProgress*100)) + "%");
                Log.e("MainActivity", "bookProgress " + String.valueOf(bookProgress));
                if (book != null) {
                    FBReader.openBookActivity(MainActivity.this, book,
                            null);
                }else{
                    Log.i("MainActivity", "book == null");
                }
            }
        });
        */

        /**
         * 以下为首页面加载WebView的代码
         */
//        webView=(WebView)findViewById(R.id.webView);
//        webView.loadUrl("file:///android_asset/htmlpage/testMUI_6.1_ios/z9_deckReader_1_and.html");
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                loadBookList();//自动扫描指定文件夹下txt文件
//            }
//
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        webView.setWebChromeClient(new WebChromeClient(){
//
//        });
//        webView.addJavascriptInterface(new MyObject(), "xuming");

        /**
         * 以下为首页面加载替换掉的WebView的代码(testMUI_6.3_ios)
         */
//        webView=(WebView)findViewById(R.id.webView);
//        webView.loadUrl("file:///android_asset/htmlpage/testMUI_6.3_ios/z9_deckReader_1_and.html");
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                loadBookList();//自动扫描指定文件夹下txt文件并初始化WebView书籍列表页面
//
////                //控制在线导入边上的红点显示与否
////                boolean hasPush = false;  //判断后台是否有推送消息
//////                boolean hasPush = true;
////                if (hasPush){
////                    DownloadFromBackstage();
////                }else {
////                    boolean showmark = hasFilesInDownloadBooks();
////                    if (showmark){
////                        webView.loadUrl("javascript:showmark()");
////                    }else {
////                        webView.loadUrl("javascript:hidemark()");
////                    }
////                }
//
//            }
//
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        webView.setWebChromeClient(new WebChromeClient(){
//
//        });
//        webView.addJavascriptInterface(new MyObject(), "xuming");

//        initWebView();

        //注册广播，监听PushReceiver中发送过来的广播，进行UI更新
        myReceiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("com.ankireader.ReceivedUrlFromPush");
        registerReceiver(myReceiver, intentFilter);

    }

    /**
     * 初始化WebView
     */
    private void initWebView(){
        webView=(WebView)findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/htmlpage/testMUI_6.3_ios/z9_deckReader_1_and.html");
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

//                //控制在线导入边上的红点显示与否
//                boolean hasPush = false;  //判断后台是否有推送消息
////                boolean hasPush = true;
//                if (hasPush){
//                    DownloadFromBackstage();
//                }else {
//                    boolean showmark = hasFilesInDownloadBooks();
//                    if (showmark){
//                        webView.loadUrl("javascript:showmark()");
//                    }else {
//                        webView.loadUrl("javascript:hidemark()");
//                    }
//                }

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
    class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MainActivity-onReceive", ">>>>>>>>>>onReceived!");
            Toast.makeText(getApplicationContext(), "MainActivity-onReceive", Toast.LENGTH_SHORT).show();
            String url = intent.getStringExtra("URL");
            DownloadFromBackstage(url);
        }
    }



    /**
     * 请求权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);

    }
    /**
     * 请求权限的回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

                initWebView();

            } else {
                Toast.makeText(this, "对不起，艾卡阅读的使用需要得到您对以上权限的许可，请您重启应用并选择允许，谢谢！", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    /**
     * JS调用原生的接口类，加载至阅读页面，文件从downloadBooks目录拷贝至dirBooks
     */
    class  MyObject{
        @JavascriptInterface
        public void read(String path){
            Book b = new Book();
            b.setBookPath(path);

            ZLFile file = ZLFile.createFileByPath(b.getBookPath());
            final com.chaojiyiji.geometerplus.fbreader.book.Book book = createBookForFile(file);
            if (book != null) {
                FBReader.openBookActivity(MainActivity.this, book,
                        null);
            }else{
                Log.i("MainActivity", "book == null");
            }
        }

        @JavascriptInterface
        public void moveFiles(){
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
        public void openDrawer(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toOpenDrawer();
                }
            });

        }

        @JavascriptInterface
        public void viewShot(){
//            runViewShot(webView);
            runScreenShot(MainActivity.this);
        }
    }

    /**
     * 获取WebView的截图
     */
    private void runViewShot(View view){
        Bitmap bitmap = ViewShot.getShot(view);
        ViewShot.saveBitmap(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AnkiReader/picture", "MyPic", bitmap);
        Toast.makeText(this, "截图已保存！",Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取整个屏幕的截图
     */
    private void runScreenShot(Activity context){
        Bitmap bitmap = ViewShot.captureScreen(context);
        ViewShot.saveBitmap(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AnkiReader/picture", "MyPic", bitmap);
        Toast.makeText(this, "截图已保存！",Toast.LENGTH_SHORT).show();
    }


    /**
     * 以下为自动扫描指定文件夹下txt文件的代码
     */
    public String getTXTFiles(){
        final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e("ROOT_PATH>>>>", ROOT_PATH);
        dirBooks = new File(ROOT_PATH + "/AnkiReader/dirBooks");
        downloadBooks = new File(ROOT_PATH + "/AnkiReader/downloadBooks");
        if (!dirBooks.exists()) {
            boolean b = dirBooks.mkdirs();
//            Log.e("dirBooks", String.valueOf(b));
        }
        if (!downloadBooks.exists()) {
            boolean b =downloadBooks.mkdirs();
//            Log.e("downloadBooks", String.valueOf(b));
        }

        //在dirBooks文件夹下预放几本书籍
        CopyRawtodata.readFromRaw(MainActivity.this, R.raw.aesopsfables, ROOT_PATH + "/AnkiReader/dirBooks", "Aesop's Fables.txt");
        CopyRawtodata.readFromRaw(MainActivity.this, R.raw.gonewiththewind, ROOT_PATH + "/AnkiReader/dirBooks", "GONE WITH THE WIND.txt");
        CopyRawtodata.readFromRaw(MainActivity.this, R.raw.ian, ROOT_PATH + "/AnkiReader/dirBooks", "ian.txt");
        CopyRawtodata.readFromRaw(MainActivity.this, R.raw.mmook, ROOT_PATH + "/AnkiReader/dirBooks", "mmook.txt");


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
            Log.e("list", list.toString());
        }

        Gson gson = new Gson();
        String json = gson.toJson(list);
        Log.e("json", json);
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
     * 后台请求下载文件,下载至downloadBooks文件夹下
     */
    private void  DownloadFromBackstage(){
        //先post请求获得下载所需的url地址
        String url = "http://192.168.1.13/ankichina/index.php/Home/App/xiazai/";
        HashMap<String, String> map = new HashMap<>();
//        map.put("goods_id", "");
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("code");
                    data = jsonObject.getString("data");
                    Log.e("请求到的地址", ">>>>>>>>>> " + data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //确保url地址获取到之后，再get请求下载文件
                String filename = data.substring(data.lastIndexOf("/"));//获取文件名
//                String filepath = "/storage/emulated/0/Android/data/com.chaojiyiji.geometerplus.zlibrary.ui.android/downloadBooks/" +filename;//下载文件的存储路径,最后一个“/”后的为文件名
                String filepath = downloadBooks.getAbsolutePath()+ "/" +filename;//下载文件的存储路径,最后一个“/”后的为文件名
                ZXUtils.DownLoadFile(data, filepath, new Callback.CommonCallback<File>() {

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
                        if (showmark){
                            webView.loadUrl("javascript:showmark()");
                        }else {
                            webView.loadUrl("javascript:hidemark()");
                        }
                    }
                });

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("请求下载地址失败的日志", ">>>>>>>>>> " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 从后台下载文件到downloadBooks目录下，下载成功后更新UI
     * @param url 推送透传过来的下载地址
     */
    private void  DownloadFromBackstage(String url){
        if(url.lastIndexOf("/") != -1) {
            String filename = url.substring(url.lastIndexOf("/"));//获取文件名
            String filepath = downloadBooks.getAbsolutePath() + "/" + filename;//下载文件的存储路径,最后一个“/”后的为文件名
            ZXUtils.DownLoadFile(url, filepath, new Callback.CommonCallback<File>() {

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



    /**
     * Apply KitKat specific translucency.
     */
    private void applyKitKatAndLollipopTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            mTintManager.setStatusBarTintResource(R.color.colorAnki);//通知栏所需颜色
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initState();
        }

    }

    @TargetApi(19)  //Android4.4
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)  //Android5.0
    private void initState() {
        Window window = getWindow();
        //window.setStatusBarColor(Color.TRANSPARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onStart() {
        Log.e("MainActivity", "onStart");
        super.onStart();
//        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.e("MainActivity", "onResume");
        super.onResume();

        //以下为接收上一次阅读的书籍阅读进度的代码
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Float progress = pref.getFloat("BookProgress", 0);
        String bookPath = pref.getString("BookPath", "");
        String bookProgress = String.valueOf((int)(progress*100)) + "%";
        Log.e("bookPath", ">>>>>>>>>>>>>>>in MainActivity  is " +bookPath);
        Log.e("bookProgress", ">>>>>>>>>>>>in MainActivity  is " +bookProgress);
    }

    @Override
    protected void onStop() {
        Log.e("MainActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("MainActivity", "onDestroy");
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
}
