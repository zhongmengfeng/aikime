/****************************************************************************************
 * Copyright (c) 2014 Michael Goldbach <michael@m-goldbach.net>                         *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/
package com.ichi2yiji.anki;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anim.ActivityTransitionAnimation;
import com.ichi2yiji.anki.dialogs.ActionSheetDialog;
import com.ichi2yiji.anki.stats.AnkiStatsTaskHandler;
import com.ichi2yiji.anki.stats.ChartView;
import com.ichi2yiji.anki.toprightandbottomrightmenu.TopRightMenu;
import com.ichi2yiji.anki.util.ApplyTranslucency;
import com.ichi2yiji.anki.util.ThemeChangeUtil;
import com.ichi2yiji.anki.widgets.DeckDropDownAdapter;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Stats;
import com.ichi2yiji.ui.SlidingTabLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;


public class StatisticsAika extends NavigationDrawerActivity implements
        DeckDropDownAdapter.SubtitleListener{

    public static final int TODAYS_STATS_TAB_POSITION = 0;
    public static final int FORECAST_TAB_POSITION = 1;
    public static final int REVIEW_COUNT_TAB_POSITION = 2;
    public static final int REVIEW_TIME_TAB_POSITION = 3;
    public static final int INTERVALS_TAB_POSITION = 4;
    public static final int HOURLY_BREAKDOWN_TAB_POSITION = 5;
    public static final int WEEKLY_BREAKDOWN_TAB_POSITION = 6;
    public static final int ANSWER_BUTTONS_TAB_POSITION = 7;
    public static final int CARDS_TYPES_TAB_POSITION = 8;

    private Menu mMenu;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private AnkiStatsTaskHandler mTaskHandler = null;
    private View mMainLayout;
    private ArrayList<JSONObject> mDropDownDecks;
    private DeckDropDownAdapter mDropDownAdapter;
    private Spinner mActionBarSpinner;
    private boolean mIsWholeCollection = false;
    private static boolean sIsSubtitle;

    private WebView webView;
    private ImageView top_right_button;
    private LinearLayout title_name;
    private ImageView icon_backto;
    private RelativeLayout icon_backto_lyt;
    private TextView deck_name;

    /////////////dx  add 屏幕密度
    private float md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChangeUtil.changeTheme(this);
        Timber.d("onCreate()");
        sIsSubtitle = true;
        super.onCreate(savedInstanceState);
        ////////////////dx  add
        md=getMD();
//        setContentView(R.layout.activity_anki_stats);
//        mMainLayout = findViewById(android.R.id.content);
//        initNavigationDrawer(mMainLayout);
//        startLoadingCollection();

        ApplyTranslucency.applyKitKatTranslucency(this);

//        applyKitKatTranslucency();

        setContentView(R.layout.activity_statistics_aika);
//        mMainLayout = findViewById(android.R.id.content);
//        initNavigationDrawer(mMainLayout);
        startLoadingCollection();



        webView=(WebView)findViewById(R.id.webview_statistic_aika);
        webView.loadUrl("file:///android_asset/Echarts5/statistics022403.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //从SharedPreference中取得异步任务获得的源数据并初始化WebView
                updateWebView();

            }
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){});
        webView.addJavascriptInterface(new MyObject(), "xuming");

        //实例化一个右上角按钮的TopRightMenu
        final TopRightMenu mTopRightMenu = new TopRightMenu(StatisticsAika.this);

        //添加菜单项
        List<com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem("1个月    "));
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem("1年       "));
        menuItems.add(new com.ichi2yiji.anki.toprightandbottomrightmenu.MenuItem("所有时间"));
        mTopRightMenu
                .setHeight((int)(135*md))     //默认高度480
                .setWidth((int)(125*md))      //默认宽度wrap_content
                .showIcon(false)     //显示菜单图标，默认为true
                .showRadioButton(true)  //显示RadioButton，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
//                        Toast.makeText(StatisticsAika.this, "点击菜单:" + position, Toast.LENGTH_SHORT).show();
                        switch(position){
                            case 0 ://1个月
                                if (mTaskHandler.getStatType() != Stats.TYPE_MONTH) {
                                    mTaskHandler.setStatType(Stats.TYPE_MONTH);

                                    getData();//获取所选牌组的数据,
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateWebView();//为了保证异步任务数据的获取已经执行完成，延迟0.5s更新WebView
                                        }
                                    },500);
                                }
                                break;
                            case 1 ://1年
                                if (mTaskHandler.getStatType() != Stats.TYPE_YEAR) {
                                    mTaskHandler.setStatType(Stats.TYPE_YEAR);
                                    getData();//获取所选牌组的数据,
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateWebView();//为了保证异步任务数据的获取已经执行完成，延迟0.5s更新WebView
                                        }
                                    },500);
                                }
                                break;
                            case 2 ://所有时间
                                if (mTaskHandler.getStatType() != Stats.TYPE_LIFE) {
                                    mTaskHandler.setStatType(Stats.TYPE_LIFE);
                                    getData();//获取所选牌组的数据,
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateWebView();//为了保证异步任务数据的获取已经执行完成，延迟0.5s更新WebView
                                        }
                                    },500);
                                }
                                break;

                        }


                    }
                });

        //右上角按钮的点击事件
        top_right_button = (ImageView)findViewById(R.id.top_right_button);
        top_right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTopRightMenu.showAsDropDown(top_right_button, (int)(-95*md), (int)(13*md));
            }
        });

        //标题按钮
        title_name = (LinearLayout)findViewById(R.id.title_name);
        deck_name = (TextView)findViewById(R.id.deck_name);
        deck_name.setText("所有牌组");//设置默认标题为“所有牌组”
        title_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialog actionSheetDialog = new ActionSheetDialog(StatisticsAika.this)
                        .builder()
                        .setViewPostionAndWidth(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0.95)
                        .setTitle("请选择牌组")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .showCancelItem();
                for( int i = 0; i < allDeckNames.size(); i++){
                    //弹出菜单list item的添加
                    final String deckname = allDeckNames.get(i);
                    final int index = i;
                    actionSheetDialog.addSheetItem(deckname, ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    //item的点击事件
//                                    deck_name.setText(deckname);//将标题改为此牌组名称
                                    selectDropDownItem(index);
                                }
                    });
                }
                actionSheetDialog.show();


            }
        });

        //返回按钮
        icon_backto = (ImageView)findViewById(R.id.icon_backto);
        icon_backto_lyt = (RelativeLayout) findViewById(R.id.icon_backto_lyt);
        icon_backto_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsAika.this.finish();
                overridePendingTransition(R.anim.out_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
            }
        });
    }

    class MyObject {
        @JavascriptInterface
        public void backToParent(){

        }
    }

    private ArrayList<String> allDeckNames;
    @Override
    protected void onCollectionLoaded(Collection col) {

        mDropDownDecks = getCol().getDecks().allSorted();
        Log.e("Statistics","mDropDownDecks>>>>>>>>>>>>" + mDropDownDecks);
        allDeckNames = new ArrayList<>();
        for(int i = 0; i < mDropDownDecks.size() + 1; i++){
            if(i == 0){
                //设置第一个元素名称为“所有牌组”
                allDeckNames.add("所有牌组");
            }else{
                JSONObject deck = mDropDownDecks.get(i-1);
                try {
                    String deckName = deck.getString("name");
                    allDeckNames.add(deckName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        Log.e("Statistics","allDeckNames>>>>>>>>>>>>" + allDeckNames);

//        //获得统计分析源数据的方法
        mTaskHandler = new AnkiStatsTaskHandler(col);
//        AsyncTask mCreateChartTask1 = mTaskHandler.createChart2(Stats.ChartType.FORECAST, StatisticsAika.this);
//        AsyncTask mCreateChartTask2 = mTaskHandler.createChart2(Stats.ChartType.REVIEW_COUNT, StatisticsAika.this);
//        AsyncTask mCreateChartTask3 = mTaskHandler.createChart2(Stats.ChartType.REVIEW_TIME, StatisticsAika.this);
//        AsyncTask mCreateChartTask4 = mTaskHandler.createChart2(Stats.ChartType.INTERVALS, StatisticsAika.this);
//        AsyncTask mCreateChartTask5 = mTaskHandler.createChart2(Stats.ChartType.HOURLY_BREAKDOWN, StatisticsAika.this);
//        AsyncTask mCreateChartTask6 = mTaskHandler.createChart2(Stats.ChartType.WEEKLY_BREAKDOWN, StatisticsAika.this);//此数据暂时不需要
//        AsyncTask mCreateChartTask7 = mTaskHandler.createChart2(Stats.ChartType.ANSWER_BUTTONS, StatisticsAika.this);//此数据暂时不需要
//        AsyncTask mCreateChartTask8 = mTaskHandler.createChart2(Stats.ChartType.CARDS_TYPES, StatisticsAika.this);






//        mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
//                Stats.ChartType.REVIEW_TIME, mChart, mProgressBar);




//        Timber.d("onCollectionLoaded()");
//        // Add drop-down menu to select deck to action bar.
//        mDropDownDecks = getCol().getDecks().allSorted();
//        mDropDownAdapter = new DeckDropDownAdapter(this, mDropDownDecks);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        mActionBarSpinner = (Spinner) findViewById(R.id.toolbar_spinner);
//        mActionBarSpinner.setAdapter(mDropDownAdapter);
//        mActionBarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectDropDownItem(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // do nothing
//            }
//        });
//        mActionBarSpinner.setVisibility(View.VISIBLE);
//
//        // Setup Task Handler
//        mTaskHandler = new AnkiStatsTaskHandler(mCol);
//
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mViewPager.setOffscreenPageLimit(8);
//        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
//        mSlidingTabLayout.setViewPager(mViewPager);
//        AnkiStatsTaskHandler.createFirstStatisticChooserTask(mCol, mViewPager);
//
//        // Dirty way to get text size from a TextView with current style, change if possible
//        float size = new TextView(this).getTextSize();
//        mTaskHandler.setmStandardTextSize(size);
//        // Prepare options menu only after loading everything
//        supportInvalidateOptionsMenu();
//        mSectionsPagerAdapter.notifyDataSetChanged();
//
        // set the currently selected deck
        //让页面第一次启动时显示当前牌组/上一次浏览的牌组的统计数据
        String currentDeckName;
        try {
            currentDeckName = getCol().getDecks().current().getString("name");
            Log.e("Statistics","onCollectionLoaded>>>>>>>>>currentDeckName>>>" + currentDeckName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if (mIsWholeCollection) {
            selectDropDownItem(0);
        } else {
            for (int dropDownDeckIdx = 0; dropDownDeckIdx < mDropDownDecks.size();
                 dropDownDeckIdx++) {
                JSONObject deck = mDropDownDecks.get(dropDownDeckIdx);
                String deckName;
                try {
                    deckName = deck.getString("name");
                } catch (JSONException e) {
                    throw new RuntimeException();
                }
                if (deckName.equals(currentDeckName)) {
                    selectDropDownItem(dropDownDeckIdx + 1);

                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        Timber.d("onResume()");
        selectNavigationItem(DRAWER_STATISTICS);
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mMenu = menu;
        //System.err.println("in onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.anki_stats, mMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // exit if mTaskHandler not initialized yet
        if (mTaskHandler == null) {
            return true;
        }
        switch (mTaskHandler.getStatType()){
            case Stats.TYPE_MONTH:
                MenuItem monthItem = menu.findItem(R.id.item_time_month);
                monthItem.setChecked(true);
                break;
            case Stats.TYPE_YEAR:
                MenuItem yearItem = menu.findItem(R.id.item_time_year);
                yearItem.setChecked(true);
                break;
            case Stats.TYPE_LIFE:
                MenuItem lifeItem = menu.findItem(R.id.item_time_all);
                lifeItem.setChecked(true);
                break;
        }

        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId =item.getItemId();
        switch (itemId) {
            case R.id.item_time_month:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (mTaskHandler.getStatType() != Stats.TYPE_MONTH) {
                    mTaskHandler.setStatType(Stats.TYPE_MONTH);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.item_time_year:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (mTaskHandler.getStatType() != Stats.TYPE_YEAR) {
                    mTaskHandler.setStatType(Stats.TYPE_YEAR);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.item_time_all:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                if (mTaskHandler.getStatType() != Stats.TYPE_LIFE) {
                    mTaskHandler.setStatType(Stats.TYPE_LIFE);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.action_time_chooser:
                //showTimeDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDropDownItem(int position) {
//        mActionBarSpinner.setSelection(position);
        if (position == 0) {
            mIsWholeCollection = true;
            deck_name.setText("所有牌组");//将标题改为所有牌组
            Log.e("Statistics","selectDropDownItem>>>>>>>>>deckName>>>" + "所有牌组");
        } else {
            mIsWholeCollection = false;
            JSONObject deck = mDropDownDecks.get(position - 1);

            String deckName;
            try {
                deckName = deck.getString("name");
            } catch (JSONException e) {
                throw new RuntimeException();
            }
            Log.e("Statistics","selectDropDownItem>>>>>>>>>deckName>>>" + deckName);
            deck_name.setText(deckName);//将标题改为此牌组名称

            try {
                getCol().getDecks().select(deck.getLong("id"));
            } catch (JSONException e) {
                Timber.e(e, "Could not get ID from deck");
            }
        }
        mTaskHandler.setIsWholeCollection(mIsWholeCollection);

        getData();//获取所选牌组的数据,
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateWebView();//为了保证异步任务数据的获取已经执行完成，延迟0.5s更新WebView
            }
        },500);

    }

    //开启异步任务获得源数据
    private void getData(){
        //获得统计分析源数据的方法
//        mTaskHandler = new AnkiStatsTaskHandler(getCol());
        AsyncTask mCreateChartTask1 = mTaskHandler.createChart2(Stats.ChartType.FORECAST, StatisticsAika.this);
        AsyncTask mCreateChartTask2 = mTaskHandler.createChart2(Stats.ChartType.REVIEW_COUNT, StatisticsAika.this);
        AsyncTask mCreateChartTask3 = mTaskHandler.createChart2(Stats.ChartType.REVIEW_TIME, StatisticsAika.this);
        AsyncTask mCreateChartTask4 = mTaskHandler.createChart2(Stats.ChartType.INTERVALS, StatisticsAika.this);
        AsyncTask mCreateChartTask5 = mTaskHandler.createChart2(Stats.ChartType.HOURLY_BREAKDOWN, StatisticsAika.this);
        AsyncTask mCreateChartTask6 = mTaskHandler.createChart2(Stats.ChartType.WEEKLY_BREAKDOWN, StatisticsAika.this);//此数据暂时不需要
        AsyncTask mCreateChartTask7 = mTaskHandler.createChart2(Stats.ChartType.ANSWER_BUTTONS, StatisticsAika.this);//此数据暂时不需要
        AsyncTask mCreateChartTask8 = mTaskHandler.createChart2(Stats.ChartType.CARDS_TYPES, StatisticsAika.this);


    }

    //从SharedPreference中取得异步任务获得的源数据并传入WebView
    private void updateWebView(){
        //取得异步任务获得的源数据
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(StatisticsAika.this);
        String data_from_FORECAST = pref.getString("FORECAST","");
        String data_from_REVIEW_COUNT = pref.getString("REVIEW_COUNT","");
        String data_from_REVIEW_TIME = pref.getString("REVIEW_TIME","");
        String data_from_INTERVALS = pref.getString("INTERVALS","");
        String data_from_HOURLY_BREAKDOWN = pref.getString("HOURLY_BREAKDOWN","");
        String data_from_WEEKLY_BREAKDOWN = pref.getString("WEEKLY_BREAKDOWN","");
        String data_from_ANSWER_BUTTONS = pref.getString("ANSWER_BUTTONS","");
        String data_from_CARDS_TYPES = pref.getString("CARDS_TYPES","");

        Log.e("Statistics","data_from_FORECAST>>>>>>>>>>>>" + data_from_FORECAST);
        Log.e("Statistics","data_from_REVIEW_COUNT>>>>>>>>>>>>" + data_from_REVIEW_COUNT);
        Log.e("Statistics","data_from_REVIEW_TIME>>>>>>>>>>>>" + data_from_REVIEW_TIME);
        Log.e("Statistics","data_from_INTERVALS>>>>>>>>>>>>" + data_from_INTERVALS);
        Log.e("Statistics","data_from_HOURLY_BREAKDOWN>>>>>>>>>>>>" + data_from_HOURLY_BREAKDOWN);
        Log.e("Statistics","data_from_WEEKLY_BREAKDOWN>>>>>>>>>>>>" + data_from_WEEKLY_BREAKDOWN);
        Log.e("Statistics","data_from_ANSWER_BUTTONS>>>>>>>>>>>>" + data_from_ANSWER_BUTTONS);
        Log.e("Statistics","data_from_CARDS_TYPES>>>>>>>>>>>>" + data_from_CARDS_TYPES);


        JSONObject jo = new JSONObject();

        try {
            JSONArray jsonArray_1 = new JSONArray(data_from_REVIEW_COUNT);
            JSONArray jsonArray_2 = new JSONArray(data_from_FORECAST);
            JSONArray jsonArray_3 = new JSONArray(data_from_REVIEW_TIME);
            JSONArray jsonArray_4 = new JSONArray(data_from_INTERVALS);
            JSONArray jsonArray_5 = new JSONArray(data_from_HOURLY_BREAKDOWN);
            JSONArray jsonArray_6 = new JSONArray(data_from_CARDS_TYPES);
            jo.put("chart1",jsonArray_1);
            jo.put("chart2",jsonArray_2);
            jo.put("chart3",jsonArray_3);
            jo.put("chart4",jsonArray_4);
            jo.put("chart5",jsonArray_5);
            jo.put("chart6",jsonArray_6);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Statistics","jo>>>>>>>>>>>>" + jo);

        String data_to_html = jo.toString();
        webView.loadUrl("javascript:initAllChart('"+data_to_html+"')");
    }


    /**
     * @return text to be used in the subtitle of the drop-down deck selector
     */
    public String getSubtitleText() {
        return getResources().getString(R.string.statistics);
    }


    public AnkiStatsTaskHandler getTaskHandler(){
        return mTaskHandler;
    }

    public ViewPager getViewPager(){
        return mViewPager;
    }

    public SectionsPagerAdapter getSectionsPagerAdapter() {
        return mSectionsPagerAdapter;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //this is called when mSectionsPagerAdapter.notifyDataSetChanged() is called, so checkAndUpdate() here
        //works best for updating all tabs
        @Override
        public int getItemPosition(Object object) {
            if (object instanceof StatisticFragment) {
                ((StatisticFragment) object).checkAndUpdate();
            }
            //don't return POSITION_NONE, avoid fragment recreation.
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment item = StatisticFragment.newInstance(position);
            ((StatisticFragment) item).checkAndUpdate();
            return item;
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case TODAYS_STATS_TAB_POSITION:
                    return getString(R.string.stats_today).toUpperCase(l);
                case FORECAST_TAB_POSITION:
                    return getString(R.string.stats_forecast).toUpperCase(l);
                case REVIEW_COUNT_TAB_POSITION:
                    return getString(R.string.stats_review_count).toUpperCase(l);
                case REVIEW_TIME_TAB_POSITION:
                    return getString(R.string.stats_review_time).toUpperCase(l);
                case INTERVALS_TAB_POSITION:
                    return getString(R.string.stats_review_intervals).toUpperCase(l);
                case HOURLY_BREAKDOWN_TAB_POSITION:
                    return getString(R.string.stats_breakdown).toUpperCase(l);
                case WEEKLY_BREAKDOWN_TAB_POSITION:
                    return getString(R.string.stats_weekly_breakdown).toUpperCase(l);
                case ANSWER_BUTTONS_TAB_POSITION:
                    return getString(R.string.stats_answer_buttons).toUpperCase(l);
                case CARDS_TYPES_TAB_POSITION:
                    return getString(R.string.stats_cards_types).toUpperCase(l);
            }
            return null;
        }
    }

    public static abstract class StatisticFragment extends Fragment{

        //track current settings for each individual fragment
        protected long mDeckId;
        protected ViewPager mActivityPager;
        protected SectionsPagerAdapter mActivitySectionPagerAdapter;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        protected static final String ARG_SECTION_NUMBER = "section_number";



        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static StatisticFragment newInstance(int sectionNumber) {
            Fragment fragment;
            Bundle args;
            switch (sectionNumber){
                case FORECAST_TAB_POSITION:
                case REVIEW_COUNT_TAB_POSITION:
                case REVIEW_TIME_TAB_POSITION:
                case INTERVALS_TAB_POSITION:
                case HOURLY_BREAKDOWN_TAB_POSITION:
                case WEEKLY_BREAKDOWN_TAB_POSITION:
                case ANSWER_BUTTONS_TAB_POSITION:
                case CARDS_TYPES_TAB_POSITION:
                    fragment = new ChartFragment();
                    args = new Bundle();
                    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                    fragment.setArguments(args);
                    return (ChartFragment)fragment;
                case TODAYS_STATS_TAB_POSITION:
                    fragment = new OverviewStatisticsFragment();
                    args = new Bundle();
                    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                    fragment.setArguments(args);
                    return (OverviewStatisticsFragment)fragment;

                default:
                    return null;
            }

        }

        @Override
        public void onResume() {
            super.onResume();
            checkAndUpdate();

        }
        public abstract void invalidateView();
        public abstract void checkAndUpdate();



    }

    /**
     * A chart fragment containing a ChartView.
     */
    public static class ChartFragment extends StatisticFragment {

        private ChartView mChart;
        private ProgressBar mProgressBar;
        private int mHeight = 0;
        private int mWidth = 0;
        private int mSectionNumber;

        private int mType  = Stats.TYPE_MONTH;
        private boolean mIsCreated = false;

        private AsyncTask mCreateChartTask;



        public ChartFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            Bundle bundle = getArguments();
            mSectionNumber = bundle.getInt(ARG_SECTION_NUMBER);
            //int sectionNumber = 0;
            //System.err.println("sectionNumber: " + mSectionNumber);
            View rootView = inflater.inflate(R.layout.fragment_anki_stats, container, false);
            mChart = (ChartView) rootView.findViewById(R.id.image_view_chart);
            if(mChart == null)
                Timber.d("mChart null!!!");
            else
                Timber.d("mChart is not null!");

            //mChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_stats);

            mProgressBar.setVisibility(View.VISIBLE);
            //mChart.setVisibility(View.GONE);

            // TODO: Implementing loader for Collection in Fragment itself would be a better solution.
            if ((((StatisticsAika)getActivity()).getTaskHandler()) == null) {
                // Close statistics if the TaskHandler hasn't been loaded yet
                Timber.e("Statistics.ChartFragment.onCreateView() TaskHandler not found");
                getActivity().finish();
                //dx  add
                getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                return rootView;
            }

            createChart();
            mHeight = mChart.getMeasuredHeight();
            mWidth = mChart.getMeasuredWidth();
//            mChart.addFragment(this);       zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

            mType = (((StatisticsAika)getActivity()).getTaskHandler()).getStatType();
            mIsCreated = true;
            mActivityPager = ((StatisticsAika)getActivity()).getViewPager();
            mActivitySectionPagerAdapter = ((StatisticsAika)getActivity()).getSectionsPagerAdapter();
            mDeckId = CollectionHelper.getInstance().getCol(getActivity()).getDecks().selected();
            if (!isWholeCollection()) {
                try {
                    Collection col = CollectionHelper.getInstance().getCol(getActivity());
                    List<String> parts = Arrays.asList(col.getDecks().current().getString("name").split("::"));
                    if(sIsSubtitle)
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(parts.get(parts.size() - 1));
                    else
                        getActivity().setTitle(parts.get(parts.size() - 1));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if(sIsSubtitle) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.stats_deck_collection);
                } else {
                    getActivity().setTitle(getResources().getString(R.string.stats_deck_collection));
                }
            }

            return rootView;
        }

        private void createChart(){

            switch (mSectionNumber){
                case FORECAST_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.FORECAST, mChart, mProgressBar);
                    break;
                case REVIEW_COUNT_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.REVIEW_COUNT, mChart, mProgressBar);
                    break;
                case REVIEW_TIME_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.REVIEW_TIME, mChart, mProgressBar);
                    break;
                case INTERVALS_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.INTERVALS, mChart, mProgressBar);
                    break;
                case HOURLY_BREAKDOWN_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.HOURLY_BREAKDOWN, mChart, mProgressBar);
                    break;
                case WEEKLY_BREAKDOWN_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.WEEKLY_BREAKDOWN, mChart, mProgressBar);
                    break;
                case ANSWER_BUTTONS_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.ANSWER_BUTTONS, mChart, mProgressBar);
                    break;
                case CARDS_TYPES_TAB_POSITION:
                    mCreateChartTask = (((StatisticsAika)getActivity()).getTaskHandler()).createChart(
                            Stats.ChartType.CARDS_TYPES, mChart, mProgressBar);
                    break;

            }
        }



        @Override
        public void checkAndUpdate(){
            //System.err.println("<<<<<<<checkAndUpdate" + mSectionNumber);
            if(!mIsCreated)
                return;
            int height = mChart.getMeasuredHeight();
            int width = mChart.getMeasuredWidth();

            //are height and width checks still necessary without bitmaps?
            if(height != 0 && width != 0){
                Collection col = CollectionHelper.getInstance().getCol(getActivity());
                if(mHeight != height || mWidth != width ||
                        mType != (((StatisticsAika)getActivity()).getTaskHandler()).getStatType() ||
                        mDeckId != col.getDecks().selected() || isWholeCollection()){
                    mHeight = height;
                    mWidth = width;
                    mType = (((StatisticsAika)getActivity()).getTaskHandler()).getStatType();
                    mProgressBar.setVisibility(View.VISIBLE);
                    mChart.setVisibility(View.GONE);
                    mDeckId = col.getDecks().selected();
                    if(mCreateChartTask != null && !mCreateChartTask.isCancelled()){
                        mCreateChartTask.cancel(true);
                    }
                    createChart();
                }
            }
        }

        private boolean isWholeCollection() {
            return ((StatisticsAika) getActivity()).mIsWholeCollection;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
        @Override
        public void invalidateView(){
            if(mChart != null)
                mChart.invalidate();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(mCreateChartTask != null && !mCreateChartTask.isCancelled()){
                mCreateChartTask.cancel(true);
            }
        }
    }

    public static class OverviewStatisticsFragment extends StatisticFragment{

        private WebView mWebView;
        private ProgressBar mProgressBar;
        private int mType  = Stats.TYPE_MONTH;
        private boolean mIsCreated = false;
        private AsyncTask mCreateStatisticsOverviewTask;



        public OverviewStatisticsFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_anki_stats_overview, container, false);
            AnkiStatsTaskHandler handler = (((StatisticsAika)getActivity()).getTaskHandler());
            // Workaround for issue 2406 -- crash when resuming after app is purged from RAM
            // TODO: Implementing loader for Collection in Fragment itself would be a better solution.
            if (handler == null) {
                Timber.e("Statistics.OverviewStatisticsFragment.onCreateView() TaskHandler not found");
                getActivity().finish();
                //dx  add
                getActivity().overridePendingTransition(R.anim.in_new_activity_translate_anim,R.anim.out_old_activity_translate_anim);
                return rootView;
            }
            mWebView = (WebView) rootView.findViewById(R.id.web_view_stats);
            if(mWebView == null)
                Timber.d("mChart null!!!");
            else
                Timber.d("mChart is not null!");

            //mChart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_stats_overview);

            mProgressBar.setVisibility(View.VISIBLE);
            //mChart.setVisibility(View.GONE);
            createStatisticOverview();
            mType = handler.getStatType();
            mIsCreated = true;
            mActivityPager = ((StatisticsAika)getActivity()).getViewPager();
            mActivitySectionPagerAdapter = ((StatisticsAika)getActivity()).getSectionsPagerAdapter();
            Collection col = CollectionHelper.getInstance().getCol(getActivity());
            if (!isWholeCollection()) {
                mDeckId = col.getDecks().selected();
                try {
                    List<String> parts = Arrays.asList(col.getDecks().current().getString("name").split("::"));
                    if (sIsSubtitle) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(parts.get(parts.size() - 1));
                    } else {
                        getActivity().setTitle(parts.get(parts.size() - 1));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (sIsSubtitle) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.stats_deck_collection);
                } else {
                    getActivity().setTitle(R.string.stats_deck_collection);
                }
            }
            return rootView;
        }

        private boolean isWholeCollection() {
            return ((StatisticsAika) getActivity()).mIsWholeCollection;
        }

        private void createStatisticOverview(){
            AnkiStatsTaskHandler handler = (((StatisticsAika)getActivity()).getTaskHandler());
            mCreateStatisticsOverviewTask = handler.createStatisticsOverview(mWebView, mProgressBar);
        }

        @Override
        public void invalidateView() {
            if(mWebView != null)
                mWebView.invalidate();
        }

        @Override
        public void checkAndUpdate() {
            if(!mIsCreated) {
                return;
            }
            Collection col = CollectionHelper.getInstance().getCol(getActivity());
            if(mType != (((StatisticsAika)getActivity()).getTaskHandler()).getStatType() ||
                    mDeckId != col.getDecks().selected() || isWholeCollection()){
                mType = (((StatisticsAika)getActivity()).getTaskHandler()).getStatType();
                mProgressBar.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                mDeckId = col.getDecks().selected();
                if(mCreateStatisticsOverviewTask != null && !mCreateStatisticsOverviewTask.isCancelled()){
                    mCreateStatisticsOverviewTask.cancel(true);
                }
                createStatisticOverview();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(mCreateStatisticsOverviewTask != null && !mCreateStatisticsOverviewTask.isCancelled()){
                mCreateStatisticsOverviewTask.cancel(true);
            }
        }



    }


    @Override
    public void onBackPressed() {
//        if (isDrawerOpen()) {
//            super.onBackPressed();
//        } else {
            Timber.i("Back key pressed");
            Intent data = new Intent();
            if (getIntent().hasExtra("selectedDeck")) {
                data.putExtra("originalDeck", getIntent().getLongExtra("selectedDeck", 0L));
            }
            setResult(RESULT_CANCELED, data);
            finishWithAnimation(ActivityTransitionAnimation.RIGHT);
//        }
    }

    private void applyKitKatTranslucency() {

        // KitKat translucent navigation/status bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

            mTintManager.setStatusBarTintResource(R.color.material_top_blue);//通知栏所需颜色
//            mTintManager.setStatusBarTintResource(R.color.test5ru5ruu5ru55443543);//通知栏所需颜色
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
////////////////////////////dx start
    public float getMD(){
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        float density  = dm.density;
        return density;
    }
    ////////////////////////////dx end
}
