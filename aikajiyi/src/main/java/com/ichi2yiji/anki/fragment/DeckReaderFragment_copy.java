package com.ichi2yiji.anki.fragment;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankireader.CopyRawtodata;
import com.ankireader.model.Book;
import com.chaojiyiji.geometerplus.android.fbreader.FBReader;
import com.chaojiyiji.geometerplus.android.fbreader.libraryService.BookCollectionShadow;
import com.chaojiyiji.geometerplus.fbreader.Paths;
import com.chaojiyiji.geometerplus.fbreader.fbreader.FBReaderApp;
import com.chaojiyiji.geometerplus.zlibrary.core.filesystem.ZLFile;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.DownloadBooks;
import com.ichi2yiji.anki.dialogs.AlertDialog;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.anki.widgets.DeckReaderFragmentGridViewAdapter;
import com.ichi2yiji.anki.widgets.DeckReaderFragmentListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

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

import static android.app.Activity.RESULT_OK;


public class DeckReaderFragment_copy extends Fragment {
    private ListView listView;
    private GridView gridView;
    private RelativeLayout deckreder_listview_lyt;
    private TextView text_load_online_reader;
    private TextView text_share_book;
    private List<String[]> list;
    private DeckReaderFragmentListViewAdapter adapter;
    private DeckReaderFragmentGridViewAdapter adapter_grid;

    private FBReaderApp myFBReaderApp;
    File dirBooks;
    File downloadBooks;
    private Receiver myReceiver;
    private  static final int DownloadBooks_Result = 111;

    public DeckReaderFragment_copy() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DeckReaderFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadBookList();//自动扫描指定文件夹下txt文件并初始化WebView书籍列表页面
        return inflater.inflate(R.layout.fragment_deck_reader, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView)view.findViewById(R.id.deckreader_listview);
        deckreder_listview_lyt = (RelativeLayout)view.findViewById(R.id.deckreder_listview_lyt);
        text_load_online_reader = (TextView) view.findViewById(R.id.text_load_online_reader);
        text_share_book = (TextView) view.findViewById(R.id.text_share_book);

        adapter = new DeckReaderFragmentListViewAdapter(listView, getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bookPath = list.get(position)[1];
                Book b = new Book();
                b.setBookPath(bookPath);

                ZLFile file = ZLFile.createFileByPath(b.getBookPath());
                final com.chaojiyiji.geometerplus.fbreader.book.Book book = createBookForFile(file);
                if (book != null) {
                    FBReader.openBookActivity(getActivity(), book,
                            null);
                }else{
                    Log.i("DeckReaderFragment", "book == null");
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String bookname = list.get(position)[0];
                final String bookPath = list.get(position)[1];
                Log.e("onItemLongClick", bookPath);
                new AlertDialog(getActivity()).builder()
                        .setTitle("删除书籍")
                        .setMsg("你确定要删除"+ bookname +"吗？")
                        .setCancelable(false)
                        .setNegativeButton("否", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("是", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File file = new File(bookPath);
                                if(file.exists()){
                                    file.delete();
                                    list.clear();
                                    list.addAll(getTXTFiles());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }).show();
                return true;
            }
        });

        gridView = (GridView)view.findViewById(R.id.deckreader_gridview);
        adapter_grid = new DeckReaderFragmentGridViewAdapter(getActivity(), list);
        gridView.setAdapter(adapter_grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bookPath = list.get(position)[1];
                Book b = new Book();
                b.setBookPath(bookPath);

                ZLFile file = ZLFile.createFileByPath(b.getBookPath());
                final com.chaojiyiji.geometerplus.fbreader.book.Book book = createBookForFile(file);
                if (book != null) {
                    FBReader.openBookActivity(getActivity(), book,
                            null);
                }else{
                    Log.i("DeckReaderFragment", "book == null");
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String bookname = list.get(position)[0];
                final String bookPath = list.get(position)[1];
                Log.e("onItemLongClick", bookPath);
                new AlertDialog(getActivity()).builder()
                        .setTitle("删除书籍")
                        .setMsg("你确定要删除"+ bookname +"吗？")
                        .setCancelable(false)
                        .setNegativeButton("否", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("是", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File file = new File(bookPath);
                                if(file.exists()){
                                    file.delete();
                                    list.clear();
                                    list.addAll(getTXTFiles());
                                    adapter_grid.notifyDataSetChanged();
                                }
                            }
                        }).show();
                return true;
            }
        });
        text_load_online_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(getActivity()).builder()
                        .setTitle("没有待导入文章")
                        .setMsg("请进入官方网站上传文章")
                        .setCancelable(false)
                        .setNegativeButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });
        text_share_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DownloadBooks.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, DownloadBooks_Result);
                getActivity().overridePendingTransition(R.anim.in_bottom_to_top_translate_anim, 0);
            }
        });
        //初始化在线导入的红点的显示
        boolean showmark = hasFilesInDownloadBooks();
        if (showmark){
        }else {
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = pref.edit();
        String testurl = pref.getString("URLTOREADER", "");
        if(pref.getString("URLTOREADER", "") != null && pref.getString("URLTOREADER", "") != ""){
            DownloadFromBackstage(pref.getString("URLTOREADER", ""));
            editor.remove("URLTOREADER");
            editor.commit();
        }
        myFBReaderApp = (FBReaderApp) FBReaderApp.Instance();
        if (myFBReaderApp == null) {
            myFBReaderApp = new FBReaderApp(Paths.systemInfo(getActivity()),
                    new BookCollectionShadow());
        }
        getCollection().bindToService(getActivity(), null);
        myReceiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("com.ankireader.ReceivedUrlFromPush");
        getActivity().registerReceiver(myReceiver, intentFilter);
        getShareBooks();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DownloadBooks_Result){
            if(resultCode == RESULT_OK){
                Log.e("DeckReaderFragment", "onActivityResult>>>>>>done!");
                list.clear();
                list.addAll(getTXTFiles());
                adapter.notifyDataSetChanged();
                adapter_grid.notifyDataSetChanged();
            }
        }
    }

    public  void showListViewOrGridView(String viewName){
        if(viewName.equals("ListView")){
            gridView.setVisibility(View.INVISIBLE);
            deckreder_listview_lyt.setVisibility(View.VISIBLE);
            Log.e("showListViewOrGridView", ">>>>>>>>>>>>ListView");
        }else if (viewName.equals("GridView")){
            deckreder_listview_lyt.setVisibility(View.INVISIBLE);
            gridView.setVisibility(View.VISIBLE);
            Log.e("showListViewOrGridView", ">>>>>>>>>>>>GridView");
        }
    }

    /**
     * 在此广播接收器中进行book的下载，并更新UI
     */
    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("DeckReader-onReceive", ">>>>>>>>>>onReceived!");
            Toast.makeText(getActivity(), "有新的书籍可以在线导入了！", Toast.LENGTH_SHORT).show();
            String url = intent.getStringExtra("URL");
            DownloadFromBackstage(url);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = pref.edit();
            if(pref.getString("URLTOREADER", "") != null && pref.getString("URLTOREADER", "") != ""){
                editor.remove("URLTOREADER");
                editor.commit();
            }
        }
    }


    /**
     * 以下为自动扫描指定文件夹下txt文件的代码
     */
    public List<String[]> getTXTFiles(){
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

        return list;
    }


    /**
     * 原生调用JS方法
     */
    private void loadBookList(){
        final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        //在dirBooks文件夹下预放几本书籍
        CopyRawtodata.readFromRaw(getActivity(), com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.aesopsfables, ROOT_PATH + "/Chaojiyiji/dirBooks", "Aesop's Fables.txt");
        CopyRawtodata.readFromRaw(getActivity(), com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.gonewiththewind, ROOT_PATH + "/Chaojiyiji/dirBooks", "GONE WITH THE WIND.txt");
        CopyRawtodata.readFromRaw(getActivity(), com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.ian, ROOT_PATH + "/Chaojiyiji/dirBooks", "ian.txt");
        CopyRawtodata.readFromRaw(getActivity(), com.chaojiyiji.geometerplus.zlibrary.ui.android.R.raw.mmook, ROOT_PATH + "/Chaojiyiji/dirBooks", "Xcode.txt");
        list = getTXTFiles();
//        webView.loadUrl("javascript:initFirWindow('"+null+"', '"+bookJson+"')");

    }

    /**
     * 从后台下载文件到downloadBooks目录下，下载成功后更新UI
     * @param url 推送透传过来的下载地址
     */
    private void  DownloadFromBackstage(String url){
        if(url.lastIndexOf("/") != -1) {
            String filename = url.substring(url.lastIndexOf("/"));//获取文件名
            String filepath = downloadBooks.getAbsolutePath() + "/" + filename;//下载文件的存储路径,最后一个“/”后的为文件名
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
//                        webView.loadUrl("javascript:showmark()");
                    } else {
//                        webView.loadUrl("javascript:hidemark()");
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
    public void onStart() {
        Log.e("DeckReaderFragment", "onStart");
        super.onStart();
//        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        Log.e("DeckReaderFragment", "onResume");
        super.onResume();

        //以下为接收上一次阅读的书籍阅读进度的代码
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Float progress = pref.getFloat("BookProgress", 0);
        String bookPath = pref.getString("BookPath", "");
        String bookProgress = String.valueOf((int)(progress*100)) + "%";
        Log.e("bookPath", ">>>>>>>>>>>>>>>in DeckReader  is " +bookPath);
        Log.e("bookProgress", ">>>>>>>>>>>>in DeckReader  is " +bookProgress);
    }

    @Override
    public void onStop() {
        Log.e("DeckReaderFragment", "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.e("DeckReaderFragment", "onDestroy");
        super.onDestroy();
        getCollection().unbind();
        getActivity().unregisterReceiver(myReceiver);

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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case DownloadBooks_Result:
//                if (resultCode == RESULT_OK){
//                    loadBookList();
//                }
//                break;
//            default:
//        }
//    }

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
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    boolean showmark = hasFilesInDownloadBooks();
//                    if (showmark){
//                        webView.loadUrl("javascript:showmark()");
//                    }else {
//                        webView.loadUrl("javascript:hidemark()");
//                    }
//                    loadBookList();
//                }
//            });
        }

        @JavascriptInterface
        public void showSliderMenu(){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    toOpenDrawer();
//                }
//            });
        }

        @JavascriptInterface
        public void readerHelp() {
            Log.i("readerHelp", "readerHelp");
        }

        @JavascriptInterface
        public void sharedBooks() {
            Log.i("sharedBooks", "sharedBooks");
            Intent intent = new Intent(getActivity(), DownloadBooks.class);
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
                FBReader.openBookActivity(getActivity(), book,
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
//            Intent intent = new Intent(DeckReader.this, DeckPicker.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
//            DeckReader.this.finish();
        }

        @JavascriptInterface
        public void switch_test() {
            Log.i("switch_test", "switch_test");
            Log.i("switch_reader", "switch_reader");
//            Intent intent = new Intent(DeckReader.this, DeckTest.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
//            DeckReader.this.finish();
        }
    }


    private void getShareBooks(){
        String url = "https://www.ankichina.net/Home/App/appTexts";
        Map<String, String> map = new HashMap<>();
        ZXUtils.Post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getShareBooks","onSuccess>>>>>>>"+ result);

                try {

                    JSONArray jsonArray = new JSONArray(result);
                    JSONArray out = new JSONArray();
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String url = jsonObject.getString("url");
                        JSONArray in = new JSONArray();
                        in.put(title);
                        in.put(url);
                        out.put(in);
                    }

                    Log.e("getShareBooks","onSuccess>>>>>data>>>>>>>"+ out.toString());
                    SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("SHAREBOOKDATA",out.toString());
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
