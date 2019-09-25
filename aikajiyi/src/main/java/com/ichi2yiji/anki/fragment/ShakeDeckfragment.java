package com.ichi2yiji.anki.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.google.gson.Gson;
import com.ichi2yiji.anki.DownLoadDecks;
import com.ichi2yiji.anki.DownLoadDecks11;
import com.ichi2yiji.anki.adapter.ShakegridAdapter;
import com.ichi2yiji.anki.adapter.ShakerecyclerAdapter;
import com.ichi2yiji.anki.bean.SearchBean;
import com.ichi2yiji.anki.bean.SharedDecksBean;
import com.ichi2yiji.anki.dialogs.ProgressDialog;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.anki.view.FlowLayout;
import com.ichi2yiji.anki.view.FlowTextView;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ekar01 on 2017/5/23.
 */

public class ShakeDeckfragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ShakeDeckfragment";
    private ImageView img_shake_return;
    // private GridView mGridview;
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
    private RelativeLayout rl_wrong;
    private RelativeLayout rl;
    private SVProgressHUD svProgressHUD;
    private List<SearchBean.DataBean> searchDataBean;
    private String decode;
    private DownLoadDecks downLoadDecks;
    private ShakerecyclerAdapter shakerecyclerAdapter;
    private FlowLayout shake_flow;
    private List<TextView> tlist = new ArrayList<>();
    //private TextView textView;
    private FlowTextView textView;
    private String param;
    private SharedDecksBean.DataBean dataBean;
    private boolean isHaveMyselfClass;
    private ProgressDialog progressDialog;
    private File downloadResult;
    private boolean hasError;
    private RelativeLayout rl_share_deck_back;
    private Callback.Cancelable cancelable;

//    public static ShakeDeckfragment newInstance(String text) {
//        ShakeDeckfragment fragment = new ShakeDeckfragment();
//        Bundle args = new Bundle();
//        args.putString("param", text);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_deck_fragment, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();//从activity传过来的Bundle
//        result = bundle.getString("result");
//        isHaveMyselfClass = bundle.getBoolean("isHaveMyselfClass");
        requestData1();
        searchByCatId(dataBean.getCats().get(0).getCat_id());

//        ((DownLoadDecks11) getActivity()).setMyKeyDownListener(new DownLoadDecks11.MyKeyDownListener() {
//            @Override
//            public boolean onKeyDown(int keyCode, KeyEvent event) {
//                    Log.e(TAG, "取消了码？:  " + cancelable.isCancelled());
//                if(null != cancelable){
//                    cancelable.cancel();
//                    Log.e(TAG, "取消了码？:  " + cancelable.isCancelled());
//                }
//                return true;
//            }
//        });
    }

    public void set(SharedDecksBean.DataBean dataBean, boolean isHaveMyselfClass){
        this.dataBean = dataBean;
        this.isHaveMyselfClass = isHaveMyselfClass;
    }

    //初始化
    private void initView(View view) {

        rl_share_deck_back = (RelativeLayout) view.findViewById(R.id.rl_share_deck_back);
        img_shake_return = (ImageView) view.findViewById(R.id.img_shake_return);
        img_search = (ImageView) view.findViewById(R.id.img_search);
        edit_search = (EditText) view.findViewById(R.id.edit_search);
        text_search = (TextView) view.findViewById(R.id.text_search);
        text_gongxiang = (TextView) view.findViewById(R.id.text_gongxiang);
        shake_rl_all = (RelativeLayout) view.findViewById(R.id.shake_rl_all);
        mListview = (ListView) view.findViewById(R.id.mListview);
        rl_wrong = (RelativeLayout) view.findViewById(R.id.shake_rl_wrong);
        rl = (RelativeLayout) view.findViewById(R.id.rl);
        shake_flow = (FlowLayout) view.findViewById(R.id.shake_flow);
        downLoadDecks = new DownLoadDecks();
//        svProgressHUD = new SVProgressHUD(getActivity());
        progressDialog = new ProgressDialog(getActivity()).builder();
        if (getArguments() != null) {
            param = getArguments().getString("param");
        }

        mListview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //searchByCatId("2");
        //showInputProgressCircle(null, "加载中");
        rl_share_deck_back.setOnClickListener(this);
        img_shake_return.setOnClickListener(this);
        img_search.setOnClickListener(this);
        text_search.setOnClickListener(this);
        //mGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

//        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //跳转到支付方式页面
//                Intent intent = new Intent(getActivity(), PayOnlineActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    //获取牌组列表数据
    private void requestData1() {
        Log.e(TAG, "onActivityCreated>>>>>>>>" + dataBean.toString());
        cats = dataBean.getCats();
                /*shakegridAdapter = new ShakegridAdapter(getActivity(), cats);
                shakegridAdapter.setSecletion(0);*/
        //流式布局
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 15;
        lp.leftMargin = 15;
        for (int i = 0; i < cats.size(); i++) {
            textView = new FlowTextView(getActivity());
            textView.setText(cats.get(i).getCat_name());
            textView.setBackgroundResource(R.drawable.shakegrid);
            textView.setTextColor(Color.WHITE);
            tlist.add(textView);
            for (int j = 0; j < tlist.size(); j++) {
                if (j == 0) {
                    tlist.get(j).setBackgroundResource(R.drawable.shakegrid_select);
                    tlist.get(j).setTextColor(Color.BLACK);
                } else {
                    tlist.get(j).setBackgroundResource(R.drawable.shakegrid);
                    tlist.get(j).setTextColor(Color.WHITE);
                }
            }
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < tlist.size(); i++) {
                        if (i == finalI) {
                            tlist.get(i).setBackgroundResource(R.drawable.shakegrid_select);
                            tlist.get(i).setTextColor(Color.BLACK);
                        } else {
                            tlist.get(i).setBackgroundResource(R.drawable.shakegrid);
                            tlist.get(i).setTextColor(Color.WHITE);
                            //textView.setTextColor(Color.WHITE);
                        }
                    }
                    String cat_id = dataBean.getCats().get(finalI).getCat_id();
                    Log.i(TAG, "onItemClick: " + cat_id);
                    searchByCatId(cat_id);
                }
            });
            shake_flow.addView(textView, lp);

        }
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
                disappearProgress();
                Log.i(TAG, "onSuccess: " + result);
                dataBean.getCats();
                /*shakegridAdapter = new ShakegridAdapter(getActivity(), cats);
                shakegridAdapter.setSecletion(0);*/
                //流式布局
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.bottomMargin = 5;
                lp.rightMargin = 5;
                lp.topMargin = 15;
                lp.leftMargin = 15;
                for (int i = 0; i < cats.size(); i++) {
                    textView = new FlowTextView(getActivity());
                    textView.setText(cats.get(i).getCat_name());
                    textView.setBackgroundResource(R.drawable.shakegrid);
                    textView.setTextColor(Color.WHITE);
                    tlist.add(textView);
                    tlist.get(0).setBackgroundResource(R.drawable.shakegrid_select);
                    tlist.get(0).setTextColor(Color.BLACK);
                    final int finalI = i;
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < tlist.size(); i++) {
                                if (i == finalI) {
                                    tlist.get(i).setBackgroundResource(R.drawable.shakegrid_select);
                                    tlist.get(i).setTextColor(Color.BLACK);
                                } else {
                                    tlist.get(i).setBackgroundResource(R.drawable.shakegrid);
                                    tlist.get(i).setTextColor(Color.WHITE);
                                    //textView.setTextColor(Color.WHITE);
                                }
                            }
                            cat_id = dataBean.getCats().get(finalI).getCat_id();
                            Log.i(TAG, "onItemClick: " + cat_id);
                            searchByCatId(cat_id);
                        }
                    });
                    shake_flow.addView(textView, lp);

                }
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
                    final List<SearchBean.DataBean> searchDataBean = searchBean.getData();
                    Log.i(TAG, "onSuccess: dddddddddddddddddddd" + searchDataBean.toString());
                    ShakerecyclerAdapter shakerecyclerAdapter = new ShakerecyclerAdapter(getActivity(), searchDataBean);
                    mListview.setAdapter(shakerecyclerAdapter);
                    shakerecyclerAdapter.setItem_shakerecycler(new ShakerecyclerAdapter.ItemDownClicklistener() {
                        @Override
                        public void itemdownclicklistener(View view, int position) {
                            try {
                                decode = URLDecoder.decode(searchDataBean.get(position).getUrl(), "UTF-8");
                                Log.i("decode", "onClick: " + decode);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            String[] data1 = new String[]{decode, searchDataBean.get(position).getTitle()};
                            addStatistics(searchDataBean.get(position).getGoods_id(), data1);
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
        Log.e(TAG, "onActivityCreated>>>>>>>>" + dataBean.toString());
        showInputProgressCircle(null, "加载中");
        HashMap<String, String> map = new HashMap<>();
        map.put("cat_id", id);
        ZXUtils.Post(Urls.URL_KEYWORD_SEARCH_PICKER, map, new Callback.CommonCallback<String>() {
            private List<SearchBean.DataBean> data;

            @Override
            public void onSuccess(String result) {
                disappearProgress();
                Log.i(TAG, "onSuccess: CatId" + result);
                Gson gson = new Gson();
                SearchBean searchBean = gson.fromJson(result, SearchBean.class);

                if (searchBean.getCode() == 1000) {
                    data = searchBean.getData();
                    Log.i(TAG, "onSuccess: datadddddddd" + data.toString());
                    //没有数据加载失败的布局
                    if (data.size() == 0) {
                        rl_wrong.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.GONE);
                    } else {
                        rl_wrong.setVisibility(View.GONE);
                        rl.setVisibility(View.VISIBLE);
                    }
                    shakerecyclerAdapter = new ShakerecyclerAdapter(getActivity(), data);
                    mListview.setAdapter(shakerecyclerAdapter);
                    //点击下载按钮
                    shakerecyclerAdapter.setItem_shakerecycler(new ShakerecyclerAdapter.ItemDownClicklistener() {
                        @Override
                        public void itemdownclicklistener(View view, int position) {
                            try {
                                decode = URLDecoder.decode(data.get(position).getUrl(), "UTF-8");
                                Log.i("decode", "onClick: " + decode);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            String[] data1 = new String[]{decode, data.get(position).getTitle()};
                            addStatistics(data.get(position).getGoods_id(), data1);
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
                if (isHaveMyselfClass){
                    ((DownLoadDecks11)getActivity()).showFragment("addDeckfragment");
                } else {
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.animator.add_in, R.animator.add_out);
                }
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
        map.put("goods_id", goodsId);
        map.put("mem_id", memId);
        ZXUtils.Post(Urls.URL_DOWN_PICKER, map, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                LogUtil.e("onSuccess: result = " + result);
                try {
                    int code = result.getInt("code");
                    if (code == 1000) {
                        downloadTodirDecks(data);
                    } else {
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
    private void downloadTodirDecks(final String[] data) {
        String path = data[0];
        String deckName = "";
        if (data.length >= 2) {
            deckName = data[1];
        }
        // webView.loadUrl("javascript:showInDownload('"+ deckName +"')");
//        showInputProgressCircle(null, "正在下载中");
        progressDialog.show();
        progressDialog.setDownloadTip("准备下载");
        String filename = path.substring(path.lastIndexOf("/") + 1);
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

        final String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chaojiyiji/dirDecks/" + filename.replace("yiji", "apkg");
        /*ZXUtils.DownLoadFile(path_encode, filepath, new Callback.CommonCallback<File>() {
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
                        getActivity().setResult(RESULT_OK, i);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);
                        Log.e("downloadTodirDecks", "onFinished");
                    }
                }, 1000);
            }
        });*/


        cancelable = ZXUtils.DownLoadFile(path_encode, filepath, new Callback.ProgressCallback<File>() {
            //成功获取数据
            @Override
            public void onSuccess(File result) {
                ShakeDeckfragment.this.downloadResult = result;
                Log.e(TAG, "result:  " + result);///storage/emulated/0/Chaojiyiji/dirDecks/四级单词__01.apkg
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!hasError && downloadResult != null) {
                    //成功获取数据
                    progressDialog.showSuccessWithStatus();
                    progressDialog.setDownloadTip("恭喜，下载成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            Intent i = new Intent();
//                            i.putExtra("PathFromDownLoadDecks", filepath);
//                            getActivity().setResult(RESULT_OK, i);
//                            getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);

                            Intent intent = new Intent();
                            intent.setAction("message_broadcast");
                            intent.putExtra("message", filepath);
                            intent.putExtra("isFromDownloadDecks", true);
                            getActivity().sendBroadcast(intent);

//                            EventBus.getDefault().post(new MessageEvent(filepath));
                            getActivity().finish();
                            Log.e("downloadTodirDecks", "onFinished");
                        }
                    }, 1000);
                } else {
                    Log.e(TAG, "data:  " + data);
                    progressDialog.showErrorWithStatus();
                    progressDialog.setDownloadTip("下载出现问题了");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    },100);
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Float i = Float.parseFloat(String.valueOf(current)) / Float.parseFloat(String.valueOf(total));
                DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
                String progress = decimalFormat.format(i * 100);

                progressDialog.setDownloadTip("下载中：" + progress + "%");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
