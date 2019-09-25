package com.ichi2yiji.anki.fragment;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.DownLoadDecks11;
import com.ichi2yiji.anki.adapter.ClassByMeDecksAdapter;
import com.ichi2yiji.anki.adapter.ProfessionDecksAdapter;
import com.ichi2yiji.anki.bean.SharedDecksBean;
import com.ichi2yiji.anki.dialogs.ProgressDialog;
import com.ichi2yiji.anki.util.ZXUtils;
import com.ichi2yiji.anki.view.BaseListView;
import com.ichi2yiji.anki.view.FlowLayout;
import com.ichi2yiji.anki.view.FlowTextView;
import com.ichi2yiji.common.ToastAlone;
import com.ichi2yiji.common.Urls;
import com.ichi2yiji.utils.SPUtil;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.io.File;
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

public class AddDeckfragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddDeckfragment";
    private TextView tv_more;
    private ImageView img_add_return;
    private List<String> list = new ArrayList<>();
    private BaseListView mListview;

    private boolean isHaveClassCreateByMe;

    private List<String> catid_list = new ArrayList<>();
    private RelativeLayout rl_wrong;
    private SVProgressHUD svProgressHUD;
    private RelativeLayout add_rl;
    private String decode;
    private SharedDecksBean.DataBean data;

    private String classname1;
    private String classname;


    private FlowLayout mFlowlayout;
    private List<TextView> textlist = new ArrayList<>();//科目集合
    private List<SharedDecksBean.DataBean.ClassCreatedByMeBean> classCreatedByMe;
    private List<SharedDecksBean.DataBean.ProfessionalsBean> professionals;
    private List<SharedDecksBean.DataBean.ProfessionalsBean.DecksBean> professionalDecks;
    private List<SharedDecksBean.DataBean.ClassCreatedByMeBean.DecksBean> classCreatedByMeDecks;
    private ProgressDialog progressDialog;
    private RelativeLayout rl_add_deck_back;
    private boolean isFirstEnter = true;
    private Callback.Cancelable cancelable;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_deck_fregment, null);

        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();//从activity传过来的Bundle
//        result = bundle.getString("result");
        Log.i(TAG, "onSuccess----: " + data);
        requestData1();
        initFistTag();
//        ((DownLoadDecks11) getActivity()).setMyKeyDownListener(new DownLoadDecks11.MyKeyDownListener() {
//            @Override
//            public boolean onKeyDown(int keyCode, KeyEvent event) {
//                if(null != cancelable){
//                    cancelable.cancel();
//                }
//                return false;
//            }
//        });
    }

    public void set(SharedDecksBean.DataBean data){
        this.data = data;
    }

    private void initFistTag() {
        if (isHaveClassCreateByMe) {
            setClass1(0);
        } else {
            setPro1(0);
        }
    }


    private void requestData1() {
        //1.获取要展现的数据;
//        Log.i(TAG, "requestData1: " + data.toString());
        if(data != null){
            classCreatedByMe = data.getClassCreatedByMe();
            if (classCreatedByMe.size() > 0) {
                isHaveClassCreateByMe = true;
            }
            Log.e(TAG, "requestData1: " + classCreatedByMe.toString());

            professionals = data.getProfessionals();
            Log.e(TAG, "requestData1: " + professionals.toString());
        }

        //2.准备好流失布局;设置listview的边距等参数;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 15;
        lp.leftMargin = 20;
        //3.将要展现的数据都绑定到标签上;

        //3.1 将classCreateByMe的课程一个个添加;
        for (int i = 0; i < classCreatedByMe.size(); i++) {
            FlowTextView textView = new FlowTextView(getActivity());
//            TextView textView = new TextView(getActivity());
            SharedDecksBean.DataBean.ClassCreatedByMeBean bean = classCreatedByMe.get(i);

            //为textView设置属性;
            textView.setCollectionType(FlowTextView.DeckGroupType_classCreateByme);
            textView.setClassId(Integer.parseInt(bean.getClass_id()));
            textView.setClassOrCourseName(bean.getClassname());
            textView.setDecksClassCreateByme(bean.getDecks());
            textView.setText(bean.getClassname());
            textView.setTagIndex(textlist.size());


            //////////////////////////
//            textlist.get(i).setBackgroundResource(R.drawable.add_pro);
//            textlist.get(i).setTextColor(Color.parseColor("#ffce54"));
//            for (int j = 0; j < classCreatedByMe.size(); j++) {
//                textlist.get(j).setBackgroundResource(R.drawable.addgrid);
            ///////////////////////////

            //为textView设置样式;自己的课程与关注的课程颜色不一样;
            textView.setTextColor(Color.parseColor("#a0d468"));
            textView.setBackgroundResource(R.drawable.add_pro);
            textView.setItsFocusBackgroundColor(R.drawable.add_pro);
            textView.setItsBackgroundColor(R.drawable.add_pro);
            textView.setItsTextColor(Color.parseColor("#a0d468"));//绿色
            //textView.setItsFocusTextColor(Color.parseColor("#ffce54"));

            //为textView设置点击事件;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.便利所有的标签,保持原有
                    for (int i = 0; i < textlist.size(); i++) {
                        FlowTextView TextView = (FlowTextView) textlist.get(i);
                        TextView.setBackgroundResource(TextView.getItsBackgroundColor());
                        TextView.setItsFocusBackgroundColor(TextView.getItsFocusBackgroundColor());
                        TextView.setTextColor(TextView.getItsTextColor());
                    }
                    //2.被点中的变样子;
                    FlowTextView flowTextView = (FlowTextView) v;
                    int touchIndex = flowTextView.getTagIndex();
                    setClass1(touchIndex);
                }
            });


            //将textView添加到集合和流失布局中;
            textlist.add(textView);
            mFlowlayout.addView(textView, lp);
        }


        //3.2 将professional的课程一个个添加;
        for (int i = 0; i < professionals.size(); i++) {
            FlowTextView textView = new FlowTextView(getActivity());
            SharedDecksBean.DataBean.ProfessionalsBean bean = professionals.get(i);

            //为textView设置属性;
            textView.setCollectionType(FlowTextView.DeckGroupType_professional);
            textView.setClassId(Integer.parseInt(bean.getClass_id()));
            textView.setClassOrCourseName(bean.getClassname());
            textView.setDecksProfessional(bean.getDecks());
            /**
             * @TODO---6.29
             * by:xz
             */
            textView.setIsPay(bean.getIs_pay());
            textView.setFree_course_number(bean.getFree_course_number());
            textView.setClassname(bean.getClassname());
            textView.setClass_price(bean.getClass_price());
            textView.setTotal_course(bean.getTotal_course());

            textView.setText(bean.getClassname());
            textView.setTagIndex(textlist.size());

            //为textView设置样式;自己的课程与关注的课程颜色不一样;
            textView.setTextColor(Color.parseColor("#ffce54"));
            textView.setBackgroundResource(R.drawable.addgrid);
            textView.setItsBackgroundColor(R.drawable.addgrid);
            textView.setItsFocusBackgroundColor(R.drawable.addgrid);
            textView.setItsTextColor(Color.parseColor("#ffce54"));//黄色

            //为textView设置点击事件;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.便利所有的标签,保持原有
                    for (int i = 0; i < textlist.size(); i++) {
                        FlowTextView TextView = (FlowTextView) textlist.get(i);
                        TextView.setBackgroundResource(TextView.getItsBackgroundColor());
                        TextView.setItsFocusBackgroundColor(TextView.getItsFocusBackgroundColor());
                        TextView.setTextColor(TextView.getItsTextColor());
                    }
                    //2.被点中的变样子;
                    FlowTextView flowTextView = (FlowTextView) v;
                    int touchIndex = flowTextView.getTagIndex();
                    isFirstEnter = false;
                    setPro1(touchIndex);
                }
            });


            //将textView添加到集合和流失布局中;
            textlist.add(textView);
            mFlowlayout.addView(textView, lp);
        }
    }


    private void showItsDecks(int touchIndex) {
        int collectionType = ((FlowTextView) textlist.get(touchIndex)).getCollectionType(); //获取他是什么类型的标签;
        if (collectionType == 1) {
            setClass1(touchIndex);
        } else if (collectionType == 2) {
            setPro1(touchIndex);
        }
    }


    //加载classCreatedByMe数据
    private void setClass1(int i) {
        //1.获取制定的那个标签,并给标签改变颜色;
        FlowTextView textView = (FlowTextView) textlist.get(i);
        if (isFirstEnter) {
//            textView.setBackgroundResource(R.drawable.addgrid);
//            textView.setTextColor(Color.parseColor("#ffce54"));
            textView.setBackgroundResource(R.drawable.addpro_select);
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setBackgroundResource(R.drawable.addpro_select);
            textView.setTextColor(Color.WHITE);
        }
        //2.从标签中获得decks,看有没有内容;
        final List<SharedDecksBean.DataBean.ClassCreatedByMeBean.DecksBean> myDecks = textView.getDecksClassCreateByme();
        if (myDecks.size() == 0) {
            rl_wrong.setVisibility(View.VISIBLE);
            add_rl.setVisibility(View.GONE);
        } else {
            rl_wrong.setVisibility(View.GONE);
            add_rl.setVisibility(View.VISIBLE);
        }
        //3.从标签中获得decks,并置放在Adapter中;并为Adapter放置监听事件;
        ClassByMeDecksAdapter addClassAdapter = new ClassByMeDecksAdapter(getActivity(), myDecks);
        addClassAdapter.setItem_shakerecycler(new ClassByMeDecksAdapter.ItemDownClicklistener() {
            @Override
            public void itemdownclicklistener(View view, int position) {
                try {
                    decode = URLDecoder.decode(myDecks.get(position).getUrl(), "UTF-8");
                    Log.i("decode", "onClick: " + decode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String[] data1 = new String[]{decode, myDecks.get(position).getTitle()};
                addStatistics(myDecks.get(position).getGoods_id(), data1);
            }
        });
        mListview.setAdapter(addClassAdapter);

    }

    //加载professional数据
    private void setPro1(int i) {
        //1.获取制定的那个标签,并给标签改变颜色;
        FlowTextView textView = (FlowTextView) textlist.get(i);
        textView.setBackgroundResource(R.drawable.addgrid_select);
        textView.setTextColor(Color.WHITE);
        //2.从标签中获得decks,看有没有内容;
        final List<SharedDecksBean.DataBean.ProfessionalsBean.DecksBean> myDecks = textView.getDecksProfessional();
        /**
         * @TODO---6.29
         * by:xz
         */
        String isPay = textView.getIsPay();
        String free_course_number = textView.getFree_course_number();
        String classname = textView.getClassname();
        String class_price = textView.getClass_price();
        String total_course = textView.getTotal_course();

        if (myDecks.size() == 0) {
            rl_wrong.setVisibility(View.VISIBLE);
            add_rl.setVisibility(View.GONE);
        } else {
            rl_wrong.setVisibility(View.GONE);
            add_rl.setVisibility(View.VISIBLE);
        }
        //3.从标签中获得decks,并置放在Adapter中;并为Adapter放置监听事件;
        ProfessionDecksAdapter addClassAdapter = new ProfessionDecksAdapter(getActivity(), myDecks, isPay, free_course_number, classname, class_price, total_course);
        addClassAdapter.setItem_shakerecycler(new ProfessionDecksAdapter.ItemDownClicklistener() {
            @Override
            public void itemdownclicklistener(View view, int position) {
                try {
                    decode = URLDecoder.decode(myDecks.get(position).getUrl(), "UTF-8");
                    Log.i("decode", "onClick: " + decode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String[] data1 = new String[]{decode, myDecks.get(position).getTitle()};
                addStatistics(myDecks.get(position).getGoods_id(), data1);
            }
        });
        mListview.setAdapter(addClassAdapter);

//        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //跳转到支付方式页面
//                if("1".equals(isPay)){
//                    return;
//                }
//                Intent intent = new Intent(getActivity(), PayOnlineActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    private void initView(View view) {

        tv_more = (TextView) view.findViewById(R.id.tv_more);
        img_add_return = (ImageView) view.findViewById(R.id.img_add_return);
        rl_add_deck_back = (RelativeLayout) view.findViewById(R.id.rl_add_deck_back);
        mListview = (BaseListView) view.findViewById(R.id.addRecyclerview);
        rl_wrong = (RelativeLayout) view.findViewById(R.id.add_rl_wrong);
        add_rl = (RelativeLayout) view.findViewById(R.id.add_rl);
        mFlowlayout = (FlowLayout) view.findViewById(R.id.mFlowlayout);
//        svProgressHUD = new SVProgressHUD(getActivity());
        progressDialog = new ProgressDialog(getActivity()).builder();

        tv_more.setOnClickListener(this);
        rl_add_deck_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
                // ShakeDeckfragment shakeDeckfragment=ShakeDeckfragment.newInstance("111");
                //FragmentTransaction ft = getFragmentManager().beginTransaction();
                //ft.setCustomAnimations(R.animator.frame_in, R.animator.frame_out);
                ((DownLoadDecks11) getActivity()).showFragment("shakeDeckfragment");
                //ft.addToBackStack(null).replace(R.id.fragment, new ShakeDeckfragment()).commit();
                break;
            case R.id.rl_add_deck_back:
                //返回
                //startActivity(new Intent(getActivity(), DownLoadDecks.class));
                getActivity().finish();
                getActivity().overridePendingTransition(R.animator.add_in, R.animator.add_out);
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
                    String data1 = result.getString("data");
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
        //@TODO--6.1
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

            @Override
            public void onSuccess(File result) {
                //成功获取数据
                progressDialog.showSuccessWithStatus();
                progressDialog.setDownloadTip("恭喜，下载成功");
                Log.e(TAG, "result:  " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //获取数据失败
                progressDialog.showErrorWithStatus();
                progressDialog.setDownloadTip("下载出现问题了");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                },100);

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e(TAG,"取消了码？" + cancelable.isCancelled());
            }

            @Override
            public void onFinished() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Intent i = new Intent();
//                        i.putExtra("PathFromDownLoadDecks", filepath);
//                        getActivity().setResult(RESULT_OK, i);
//                        getActivity().finish();
//                        getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim, R.anim.out_old_activity_translate_anim);

                        Intent intent = new Intent();
                        intent.setAction("message_broadcast");
                        intent.putExtra("message", filepath);
                        intent.putExtra("isFromDownloadDecks", true);
                        getActivity().sendBroadcast(intent);

//                        EventBus.getDefault().post(new MessageEvent(filepath));
                        getActivity().finish();
                        Log.e("downloadTodirDecks", "onFinished");
                    }
                }, 1000);
                Log.e(TAG, "result:  " + data);
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
//            svProgressHUD.showWithStatus(title + " " + action, SVProgressHUD.SVProgressHUDMaskType.BlackCancel);
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
