package com.ichi2yiji.anki.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.CollectionHelper;
import com.ichi2yiji.anki.CustomStudyActivity;
import com.ichi2yiji.anki.dialogs.CommentDialog;
import com.ichi2yiji.anki.dialogs.EvaluateDialog;
import com.ichi2yiji.anki.dialogs.ShareDialog;
import com.ichi2yiji.anki.stats.AnkiStatsTaskHandler;
import com.ichi2yiji.anki.stats.ChartView;
import com.ichi2yiji.anki.util.ViewShot;
import com.ichi2yiji.libanki.Stats;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/17.
 */

public class CongratulationFragment_copy extends Fragment {


    private WebView webView;

    private TextView tv_time;
    private TextView tv_card_num;
    private TextView learn_num;
    private TextView review_num;
    private TextView again_num;
    private TextView correct_rate;
    private TextView familiar_num;
    private Button self_define_learning;
    private Button button_share;
    private RelativeLayout view_shot_group;

    private String data_to_html;
    private int[] todayStats;
    private JSONObject deckJson;
    private int totalNewForCurrentDeck;
    private int totalRevForCurrentDeck;
    private long did;
    private  SVProgressHUD svProgressHUD;
    private ChartView chartView;

    public CongratulationFragment_copy() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        svProgressHUD = new SVProgressHUD(getActivity());
        svProgressHUD.showWithStatus("数据统计中...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //为了保证异步任务数据的获取已经执行完成，延迟3.5s从偏好设置取出数据
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String data_from_REVIEW_COUNT = pref.getString("REVIEW_COUNT","");
                String data_from_TODAYSTATS = pref.getString("TODAYSTATS","");
                Log.e("CongratulationFragment","data_from_REVIEW_COUNT>>>>>>>>>>>>" + data_from_REVIEW_COUNT);
                Log.e("CongratulationFragment","data_from_TODAYSTATS>>>>>>>>>>>>" + data_from_TODAYSTATS);

                //处理TODAYSTATS数据
                String[] todayStats_string = data_from_TODAYSTATS.replace("[","").replace("]","").replace(" ","").split(",");
                Log.e("CongratulationFragment","todayStats_string>>>>>>>>>>>>" + Arrays.toString(todayStats_string));
                todayStats = new int[9];
                for(int i = 0; i < todayStats_string.length; i++){
                    todayStats[i] = Integer.parseInt(todayStats_string[i]);
                }
                Log.e("CongratulationFragment","todayStats>>>>>>>>>>>>" + Arrays.toString(todayStats));

                //处理REVIEW_COUNT数据
                JSONObject jo = new JSONObject();
                try {
                    JSONArray jsonArray_1 = new JSONArray(data_from_REVIEW_COUNT);
                    jo.put("chart1",jsonArray_1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("CongratulationFragment","jo>>>>>>>>>>>>" + jo);

                data_to_html = jo.toString();


                //异步更新UI
                Message message = Message.obtain();
                message.what = 1;
                message.obj = todayStats;
                handler.sendMessage(message);
            }
        },3500);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("CongratulationFragment","handleMessage>>>>>>>>>>>>" + Arrays.toString(((int[])(msg.obj))));
            if (msg.what ==1){
                //更新恭喜页面TextView的数据
                tv_time.setText(String.valueOf((int) Math.round(((int[])(msg.obj))[1]/60.0)));
                tv_card_num.setText(String.valueOf(((int[])(msg.obj))[0]));
                learn_num.setText(String.valueOf(((int[])(msg.obj))[3]));
                review_num.setText(String.valueOf(((int[])(msg.obj))[4]));
                again_num.setText(String.valueOf(((int[])(msg.obj))[2]));
                correct_rate.setText(String.valueOf(((1 - ((int[])(msg.obj))[2] / (float) (((int[])(msg.obj))[0])) * 100.0)).substring(0,2) + "%");
                if (((int[])(msg.obj))[7] != 0){
                    familiar_num.setText(String.valueOf((((int[])(msg.obj))[8] / (float)(((int[])(msg.obj))[7]) * 100.0)).substring(0,2) + "%");
                }else{
                    familiar_num.setText("今日未学习");
                }
                //更新WebView页面的数据
//                webView.loadUrl("javascript:initAllChart('"+data_to_html+"')");
                svProgressHUD.dismiss();
            }
            return false;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_congratulation, container, false);
        Bundle bundle = getArguments();
        if(bundle !=null){
            totalNewForCurrentDeck = bundle.getInt("totalNewForCurrentDeck");
            totalRevForCurrentDeck = bundle.getInt("totalRevForCurrentDeck");
            did = bundle.getLong("did");
        }
        return inflater.inflate(R.layout.fragment_congratulation_new, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        webView = (WebView)view.findViewById(R.id.webview_congratulation);
////        webView.loadUrl("file:///android_asset/Echarts5/statistics.html");
//        webView.loadUrl("file:///android_asset/allWebView/echartsFirst.html");
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                webView.loadUrl("javascript:initAllChart('"+data_to_html+"')");
//            }
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        webView.setWebChromeClient(new WebChromeClient(){});
//        webView.addJavascriptInterface(new MyObject(), "xuming");

        chartView = (ChartView)view.findViewById(R.id.chart_view);

        tv_time = (TextView)view.findViewById(R.id.tv_time);
        tv_card_num = (TextView)view.findViewById(R.id.tv_card_num);
        learn_num = (TextView)view.findViewById(R.id.learn_num);
        review_num = (TextView)view.findViewById(R.id.review_num);
        again_num = (TextView)view.findViewById(R.id.again_num);
        correct_rate = (TextView)view.findViewById(R.id.correct_rate);
        familiar_num = (TextView)view.findViewById(R.id.familiar_num);
        self_define_learning = (Button)view.findViewById(R.id.self_define_learning);
        button_share = (Button)view.findViewById(R.id.button_share);
        view_shot_group = (RelativeLayout)view.findViewById(R.id.view_shot_group);

        self_define_learning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至自定义学习页面
                Intent intent = new Intent(getActivity(), CustomStudyActivity.class);
                intent.putExtra("totalNewForCurrentDeck",totalNewForCurrentDeck);
                intent.putExtra("totalRevForCurrentDeck",totalRevForCurrentDeck);
                intent.putExtra("did", did);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.in_old_activity_translate_anim);
            }
        });
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeViewShotAndShare();
            }
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int count = pref.getInt("LearningOverDeckNumber", 0);
        count++;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("LearningOverDeckNumber", count);
        editor.commit();
        if (count == 5){
            //每学完5个牌组，弹出一次评价对话框
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initEvaluateDialog();
                }
            }, 2000);
            count = 0;
            SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor mEditor = mPref.edit();
            mEditor.putInt("LearningOverDeckNumber", count);
            mEditor.commit();
        }

        //获取当前牌组复习计数数据并绘制原生ChartView（REVIEW_COUNT）
        AnkiStatsTaskHandler mTaskHandler = new AnkiStatsTaskHandler(CollectionHelper.getInstance().getCol(getActivity()));
        AsyncTask mCreateChartTask= mTaskHandler.createChartOriginal(Stats.ChartType.REVIEW_COUNT, chartView);

    }

    private void takeViewShotAndShare() {
        //截图分享
        Bitmap bitmap = ViewShot.getShot(view_shot_group);//获取View截图
//        Bitmap bitmap = ViewShot.captureScreen(getActivity());
//        Bitmap bitmap = ViewShot.captureWebView(webView);//获取WebView截图

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=sdf.format(date);//获取当前事件日期
        String format_time = time.replace(" ","-").replace(":","-");//替换时间字符串中的空格和冒号
        ViewShot.saveBitmap(Environment.getExternalStorageDirectory() + "/Chaojiyiji/picture","congratulation_" + format_time, bitmap);//保存截图

        //友盟分享图片
        new ShareAction(getActivity()).withMedia(new UMImage(getActivity(),bitmap))
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.DOUBAN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setCallback(umShareListener).open();
    }

    private void initEvaluateDialog() {
        final EvaluateDialog evaluateDialog = new EvaluateDialog(getActivity(), R.style.congratulationDialog);
        evaluateDialog.show();
        evaluateDialog.setFeelGoodClickListener(new EvaluateDialog.FeelGoodClickListener() {
            @Override
            public void onViewClick() {
                //跳到分享的Dialog
                evaluateDialog.dismiss();
                final ShareDialog shareDialog = new ShareDialog(getActivity(), R.style.congratulationDialog);
                shareDialog.show();
                shareDialog.setNoThinksClickListener(new ShareDialog.NoThinksClickListener() {
                    @Override
                    public void onViewClick() {
                        shareDialog.dismiss();
                    }
                });
                shareDialog.setGoToShareClickListener(new ShareDialog.GoToShareClickListener() {
                    @Override
                    public void onViewClick() {
                        //截图并调用分享
                        shareDialog.dismiss();
                        takeViewShotAndShare();
                    }
                });
            }
        });
        evaluateDialog.setFeelNotGoodClickListener(new EvaluateDialog.FeelNotGoodClickListener() {
            @Override
            public void onViewClick() {
                //跳到吐槽的Dialog
                evaluateDialog.dismiss();
                CommentDialog commentDialog = new CommentDialog(getActivity(), R.style.congratulationDialog);
                commentDialog.show();
            }
        });
        evaluateDialog.setCommentLaterClickListener(new EvaluateDialog.CommentLaterClickListener() {
            @Override
            public void onViewClick() {
                //当前Dialog消失
                evaluateDialog.dismiss();
            }
        });
    }

    class MyObject {
        @JavascriptInterface
        public void backToParent(){

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    //友盟分享回调
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.e("plat","platform"+platform);

            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                com.umeng.socialize.utils.Log.e("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
