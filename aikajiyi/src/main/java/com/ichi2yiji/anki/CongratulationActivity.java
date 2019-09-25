package com.ichi2yiji.anki;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.fragment.CongratulationFragment;
import com.ichi2yiji.anki.stats.AnkiStatsTaskHandler;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Stats;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

public class CongratulationActivity extends AnkiActivity {
    private static final String TAG = "CongratulationActivity";
    private ImageView icon_back_to;
    private TextView deck_name;
    private TextView top_right_button_congratulation;
    private AnkiStatsTaskHandler mTaskHandler;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private CongratulationFragment fragment;
    FrameLayout frameLayout;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        frameLayout = (FrameLayout)findViewById(R.id.fragment_congratulation);
        fragment = new CongratulationFragment();
        ApplyTranslucency.applyKitKatTranslucency(this);
        icon_back_to = (ImageView)findViewById(R.id.icon_back_to);
        deck_name = (TextView)findViewById(R.id.deck_name);
        icon_back_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CongratulationActivity.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String mem_id = preferences.getString("MEM_ID","");
//        final String url = AnkiDroidApp.BASE_DOMAIN + "Home/Index/shareAnki/mem_id/" + mem_id;
        final String url = "https://www.baidu.com";
        top_right_button_congratulation = (TextView)findViewById(R.id.top_right_button_congratulation);
        top_right_button_congratulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //友盟分享链接
                if(mem_id.equals("")){
                    Toast.makeText(getApplicationContext(), "您还没有登录，请先登录吧！", Toast.LENGTH_SHORT).show();
                }else{

//                    UMImage thumb =  new UMImage(CongratulationActivity.this, R.drawable.aika);

                    //分享链接
                    UMWeb web = new UMWeb(url);
                    web.setTitle("艾卡记忆");//标题
                    web.setDescription("超好用的记忆软件，快来试试吧！");//描述
                    Log.e(TAG, "UMWeb:" + web + "  url:" + url);
//
                    ShareAction shareAction = new ShareAction(CongratulationActivity.this);
                    shareAction.withMedia(web);
                    shareAction.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.WEIXIN_CIRCLE);
                    shareAction.setCallback(umShareListener).open();


                }

            }
        });
        startLoadingCollection();
    }

    @Override
    protected void onCollectionLoaded(Collection col) {
        super.onCollectionLoaded(col);
        Log.e("CongratulationActivity","onCollectionLoaded>>>>>>>>>>>>>>>>>>>>>done!" );
        try {
            Long did = getCol().getDecks().selected();
            JSONObject deck = col.getDecks().get(did);
            Log.e("CongratulationActivity","onCollectionLoaded>>>>>>>deck>>"  + deck);
            int totalNewForCurrentDeck = col.getSched().totalNewForCurrentDeck();//牌组中新卡片的数量
            int totalRevForCurrentDeck = col.getSched().totalRevForCurrentDeck();//牌组中到期的卡牌数量
            Log.e("CongratulationActivity","onCollectionLoaded>>>>>>>totalNewForCurrentDeck>>"  + totalNewForCurrentDeck);
            Log.e("CongratulationActivity","onCollectionLoaded>>>>>>>totalRevForCurrentDeck>>"  + totalRevForCurrentDeck);

            Bundle bundle = new Bundle();
            bundle.putInt("totalNewForCurrentDeck",totalNewForCurrentDeck);
            bundle.putInt("totalRevForCurrentDeck",totalRevForCurrentDeck);
            bundle.putLong("did", did);

//            JSONObject deckObject = mCol.getDecks().current();
//            int extendNew = deckObject.getInt("extendNew");
//            int extendRev = deckObject.getInt("extendRev");
//            jsonObject = new JSONObject();
//            jsonObject.put("newMax",extendNew);
//            jsonObject.put("revMax",extendRev);
//            Bundle bundle = new Bundle();
//            bundle.putInt("extendNew",extendNew);
//            bundle.putInt("extendRev",extendRev);
            fragment.setArguments(bundle);
            transaction.replace(R.id.fragment_congratulation,fragment);
            transaction.commit();
            deck_name.setText(col.getDecks().current().getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mTaskHandler = new AnkiStatsTaskHandler(col);
        AsyncTask mCreateChartTask2 = mTaskHandler.createChart2(Stats.ChartType.REVIEW_COUNT, CongratulationActivity.this);
        AsyncTask mCreateStatisticsOverviewTask = mTaskHandler.createStatisticsOverview2(CongratulationActivity.this);

        //获取所有牌组复习计数数据的方法（REVIEW_COUNT）
        AsyncTask mCreateChartTask3 = mTaskHandler.createChart3(Stats.ChartType.REVIEW_COUNT, CongratulationActivity.this);

    }

    //友盟分享回调
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.e("plat","platform"+platform);

            Toast.makeText(getApplicationContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getApplicationContext(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                com.umeng.socialize.utils.Log.e("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getApplicationContext(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
