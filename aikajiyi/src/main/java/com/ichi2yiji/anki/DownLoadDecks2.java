package com.ichi2yiji.anki;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.adapter.ShakegridAdapter;
import com.ichi2yiji.anki.adapter.ShakerecyclerAdapter;
import com.ichi2yiji.anki.base.BaseActivity;
import com.ichi2yiji.anki.bean.SearchBean;
import com.ichi2yiji.anki.bean.SharedDecksBean;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DownLoadDecks2 extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DownLoadDecks2";
    private ImageView img_shake_return;
    private GridView mGridview;
    //  private RecyclerView mRecyclerview;
    private InputStream inputStream;
    private ShakegridAdapter shakegridAdapter;
    private ImageView img_search;
    private EditText edit_search;
    private TextView text_search;
    private TextView text_gongxiang;
    private RelativeLayout shake_rl_all;
    private List<SharedDecksBean.DataBean.CatsBean> list1;
    private boolean flag = true;
    private ListView mListview;
    private List<SharedDecksBean.DataBean.CatsBean> cats;
    private SharedDecksBean bean;
    private InputMethodManager imm;
    private RelativeLayout rl_wrong;
    private RelativeLayout rl;
    private SVProgressHUD svProgressHUD;

    private List<SearchBean.DataBean> data;
    private WebView webView;
    private RelativeLayout rl_share_deck_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_deck_fragment);
        initView();
        requestData();
        searchByCatId("2");
    }

    //初始化
    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        rl_share_deck_back = (RelativeLayout) findViewById(R.id.rl_share_deck_back);
        img_shake_return = (ImageView) findViewById(R.id.img_shake_return);
       // mGridview = (GridView) findViewById(R.id.mGridview);
        img_search = (ImageView) findViewById(R.id.img_search);
        edit_search = (EditText) findViewById(R.id.edit_search);
        text_search = (TextView) findViewById(R.id.text_search);
        text_gongxiang = (TextView) findViewById(R.id.text_gongxiang);
        shake_rl_all = (RelativeLayout) findViewById(R.id.shake_rl_all);
        mListview = (ListView) findViewById(R.id.mListview);
        rl_wrong = (RelativeLayout) findViewById(R.id.shake_rl_wrong);
        rl = (RelativeLayout) findViewById(R.id.rl);
        img_shake_return.setOnClickListener(this);
        img_search.setOnClickListener(this);
        text_search.setOnClickListener(this);
        mGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        searchByCatId("2");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //webView.addJavascriptInterface(new MyObject(),"xuming");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        svProgressHUD = new SVProgressHUD(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (DownLoadDecks2.this.getCurrentFocus() != null) {
                if (DownLoadDecks2.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(DownLoadDecks2.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }
        return super.onTouchEvent(event);

    }

    //获取牌组列表数据
    private void requestData() {
        HashMap<String, String> map = new HashMap<>();
        String memId = SPUtil.getPreferences(SPUtil.TYPE_DEFAULT_CONFIG, "MEM_ID", "");
        map.put("mem_id", memId);
        Log.e("mem_id", memId);
        ZXUtils.Post(Urls.URL_APP_PICKER, map, new Callback.CommonCallback<String>() {
            private String cat_id;

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: " + result);
                Gson gson = new Gson();
                bean = gson.fromJson(result, SharedDecksBean.class);
                cats = bean.getData().getCats();
                shakegridAdapter = new ShakegridAdapter(DownLoadDecks2.this, cats);
                shakegridAdapter.setSecletion(0);
                //mGridview.setAdapter(shakegridAdapter);//展示科目 （cats）
                  if(cats.size()>10){//如果数据大于10
                    list1 = new ArrayList<>();
                    for(int i=0;i<10;i++){
                        list1.add(cats.get(i));
                    }
                    shake_rl_all.setVisibility(View.VISIBLE);
                    mGridview.setAdapter(new ShakegridAdapter(DownLoadDecks2.this,list1));
                    shake_rl_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(flag){
                                mGridview.setAdapter(shakegridAdapter);
                                flag=false;
                            } else {
                                mGridview.setAdapter(new ShakegridAdapter(DownLoadDecks2.this,list1));
                                flag=true;
                            }
                        }
                    });
                }else{
                    mGridview.setAdapter(shakegridAdapter);
                }
                mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        shakegridAdapter.setSecletion(position);
                        shakegridAdapter.notifyDataSetChanged();
                        cat_id = bean.getData().getCats().get(position).getCat_id();
                        Log.i(TAG, "onItemClick: " + cat_id);
                        searchByCatId(cat_id);
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TAG", "onError: fffffffffffffffffffff" + ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private class MyObject {
        //供js调用  需要添加@JavascriptInterface注解

        @JavascriptInterface
        public void downloadSingleItem(String[] data) {
            String goodsId = data[1];
            addStatistics(goodsId,data);
        }

    }

    //输入关键字搜索
    public void searchByCatName(String name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("goods_name", name);
        ZXUtils.Post(Urls.URL_SEARCH_PICKER, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: name" + result);
                Gson gson = new Gson();
                SearchBean searchBean = gson.fromJson(result, SearchBean.class);
                if (searchBean.getCode() == 1000) {
                    data = searchBean.getData();
                    Log.i(TAG, "onSuccess: dddddddddddddddddddd" + data.toString());
                    mListview.setAdapter(new ShakerecyclerAdapter(DownLoadDecks2.this, data));
                    mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         //   String[] array = data.toArray(new String[data.size()]);

                           // addStatistics(filepath,"地理");
                        }
                    });
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

    //点击牌组进行关键字搜索
    private void searchByCatId(String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cat_id", id);
        ZXUtils.Post(Urls.URL_KEYWORD_SEARCH_PICKER, map, new Callback.CommonCallback<String>() {

            private List<SearchBean.DataBean> data;

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: CatId" + result);
                Gson gson = new Gson();
                SearchBean searchBean = gson.fromJson(result, SearchBean.class);

                if (searchBean.getCode() == 1000) {
                    data = searchBean.getData();
                    Log.i(TAG, "onSuccess: datadddddddd" + data.toString());
                    //没有数据加载失败的布局
                    if(data.size()==0){
                        rl_wrong.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.GONE);
                    }else{
                        rl_wrong.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                    }
                    mListview.setAdapter(new ShakerecyclerAdapter(DownLoadDecks2.this, data));
                    mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                          String[] data1=new String[]{data.get(position).getUrl(),"地理"};
                            addStatistics(data.get(position).getGoods_id(),data1);
                        }
                    });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.rl_share_deck_back:
                finish();
                overridePendingTransition(R.animator.shakeframe_in, R.animator.shakeframe_out);
                break;
            //搜索按钮
            case R.id.img_search:
                img_search.setVisibility(View.GONE);
                text_gongxiang.setVisibility(View.GONE);
                edit_search.setVisibility(View.VISIBLE);
                text_search.setVisibility(View.VISIBLE);
                break;
            case R.id.text_search:
                String string = edit_search.getText().toString();
                searchByCatName(string);
                break;
        }
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

    //下载
    private void downloadTodirDecks(final String[] data){
        String path = data[0];
        String deckName = "";
        if(data.length >=2){
            deckName = data[1];
        }
        //@TODO---6.2
        // webView.loadUrl("javascript:showInDownload('"+ deckName +"')");
        showInputProgressCircle(null,"正在下载中");
//        progressDialog.show();
//        progressDialog.setDownloadTip("准备下载");
        String filename = path.substring(path.lastIndexOf("/")+1);
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
                        Intent intent=new Intent(DownLoadDecks2.this,AikaActivity.class);
                        startActivity(intent);
                        DownLoadDecks2.this.finish();
                        DownLoadDecks2.this.overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                        Log.e("downloadTodirDecks", "onFinished");
                    }
                },1000);
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

