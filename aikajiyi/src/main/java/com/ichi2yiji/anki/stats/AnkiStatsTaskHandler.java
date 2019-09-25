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
package com.ichi2yiji.anki.stats;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaojiyiji.yiji.R;
import com.ichi2yiji.anki.Statistics;
import com.ichi2yiji.libanki.Collection;
import com.ichi2yiji.libanki.Stats;
import com.wildplot.android.rendering.PlotSheet;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import timber.log.Timber;

public class AnkiStatsTaskHandler {

    private static AnkiStatsTaskHandler sInstance;

    private Collection mCollectionData;
    private float mStandardTextSize = 10f;
    private int mStatType = Stats.TYPE_MONTH;
    private boolean mIsWholeCollection = false;
    private static Lock sLock = new ReentrantLock();


    public AnkiStatsTaskHandler(Collection collection){
        sInstance = this;
        mCollectionData = collection;
    }

    public void setIsWholeCollection(boolean wholeCollection) {
        mIsWholeCollection = wholeCollection;
    }

    public static AnkiStatsTaskHandler getInstance() {
        return sInstance;
    }

    public CreateChartTask createChart(Stats.ChartType chartType, View... views){
        CreateChartTask createChartTask = new CreateChartTask(chartType);
        createChartTask.execute(views);
        return createChartTask;
    }

    /////////////////////////////////zc-add-begin//////////////////////////////////////////
    public CreateChartTask2 createChart2(Stats.ChartType chartType, Object... objects){
        CreateChartTask2 createChartTask = new CreateChartTask2(chartType);
        createChartTask.execute(objects);
        return createChartTask;
    }

    //获取所有牌组复习计数数据的方法（REVIEW_COUNT）
    public CreateChartTask3 createChart3(Stats.ChartType chartType, Object... objects){
        CreateChartTask3 createChartTask = new CreateChartTask3(chartType);
        createChartTask.execute(objects);
        return createChartTask;
    }

    //重做原生图表的方法
    public CreateChartTaskOriginal createChartOriginal(Stats.ChartType chartType, View... views){
        CreateChartTaskOriginal createChartTask = new CreateChartTaskOriginal(chartType);
        createChartTask.execute(views);
        return createChartTask;
    }
    /////////////////////////////////zc-add-end//////////////////////////////////////////

    public CreateStatisticsOverview createStatisticsOverview(View... views){
        CreateStatisticsOverview createChartTask = new CreateStatisticsOverview();
        createChartTask.execute(views);
        return createChartTask;
    }

    /////////////////////////////////zc-add-begin//////////////////////////////////////////
    public CreateStatisticsOverview2 createStatisticsOverview2(Object... objects){
        CreateStatisticsOverview2 createChartTask = new CreateStatisticsOverview2();
        createChartTask.execute(objects);
        return createChartTask;
    }
    /////////////////////////////////zc-add-end//////////////////////////////////////////

    public static CreateSmallTodayOverview createSmallTodayOverview(Collection col, TextView view){
        CreateSmallTodayOverview createSmallTodayOverview = new CreateSmallTodayOverview();
        createSmallTodayOverview.execute(col, view);
        return createSmallTodayOverview;
    }

    public static CreateFirstStatisticChooserTask createFirstStatisticChooserTask(Collection col, ViewPager viewPager){
        CreateFirstStatisticChooserTask createFirstStatisticChooserTask = new CreateFirstStatisticChooserTask();
        createFirstStatisticChooserTask.execute(col, viewPager);
        return createFirstStatisticChooserTask;
    }

    private class CreateChartTask extends AsyncTask<View, Void, PlotSheet>{
        private ChartView mImageView;
        private ProgressBar mProgressBar;

        private boolean mIsRunning = false;
        private Stats.ChartType mChartType;

        public CreateChartTask(Stats.ChartType chartType){
            super();
            mIsRunning = true;
            mChartType = chartType;
        }

        @Override
        protected PlotSheet doInBackground(View... params) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();
            try {
                if (!mIsRunning) {
                    Timber.d("quiting CreateChartTask (%s) before execution", mChartType.name());
                    return null;
                } else
                    Timber.d("starting Create ChartTask, type: %s", mChartType.name());
                mImageView = (ChartView) params[0];
                mProgressBar = (ProgressBar) params[1];
                ChartBuilder chartBuilder = new ChartBuilder(mImageView, mCollectionData,
                       mIsWholeCollection, mChartType);
                return chartBuilder.renderChart(mStatType);
            }finally {
                sLock.unlock();
            }
        }

        @Override
        protected void onCancelled() {
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(PlotSheet plotSheet) {
            if(plotSheet != null && mIsRunning){

                mImageView.setData(plotSheet);
                mProgressBar.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.invalidate();

            }
        }

    }

    /////////////////////////////////zc-add-begin//////////////////////////////////////////
    //重做原生图表的方法
    private class CreateChartTaskOriginal extends AsyncTask<View, Void, PlotSheet>{
        private ChartView mImageView;
        private ProgressBar mProgressBar;

        private boolean mIsRunning = false;
        private Stats.ChartType mChartType;

        public CreateChartTaskOriginal(Stats.ChartType chartType){
            super();
            mIsRunning = true;
            mChartType = chartType;
        }

        @Override
        protected PlotSheet doInBackground(View... params) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();
            try {
                if (!mIsRunning) {
                    Timber.d("quiting CreateChartTask (%s) before execution", mChartType.name());
                    return null;
                } else
                    Timber.d("starting Create ChartTask, type: %s", mChartType.name());
                mImageView = (ChartView) params[0];
//                mProgressBar = (ProgressBar) params[1];
                ChartBuilder chartBuilder = new ChartBuilder(mImageView, mCollectionData,
                        mIsWholeCollection, mChartType);
                return chartBuilder.renderChart(mStatType);
            }finally {
                sLock.unlock();
            }
        }

        @Override
        protected void onCancelled() {
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(PlotSheet plotSheet) {
            if(plotSheet != null && mIsRunning){

                mImageView.setData(plotSheet);
//                mProgressBar.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.invalidate();

            }
        }

    }




    private int mType;
    int mMaxCards = 0;
    private int[] mValueLabels;
    private int[] mColors;
    private int[] mAxisTitles;
    private double[][] mSeriesList;
    private double mLastElement = 0;
    private double[][] mCumulative = null;
    private double mFirstElement;
    private boolean mBackwards;
    private boolean mHasColoredCumulative;
    private double mMcount;
    private boolean mDynamicAxis;


    private class CreateChartTask2 extends AsyncTask<Object, Void, String>{
        private ChartView mImageView;
        private ProgressBar mProgressBar;


        private boolean mIsRunning = false;
        private Stats.ChartType mChartType;

        public CreateChartTask2(Stats.ChartType chartType){
            super();
            mIsRunning = true;
            mChartType = chartType;
        }

        @Override
        protected String doInBackground(Object... objects) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();

            String data = null;//要返回的字符串数据
            try {
                if (!mIsRunning) {
                    Timber.d("quiting CreateChartTask (%s) before execution", mChartType.name());
                    return null;
                } else
                    Timber.d("starting Create ChartTask, type: %s", mChartType.name());

                mType = mStatType;
                Stats stats = new Stats(mCollectionData, mIsWholeCollection);
                Object[] metaData = null;

                switch (mChartType){
                    case FORECAST://预测
                        stats.calculateDue(mType);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
                        double[][] mCumulative_new_1 = new double[mCumulative.length][];
                        double[][] mSeriesList_new_1 = new double[mSeriesList.length][];
                        if(mStatType == 0){
                            mCumulative_new_1 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                            mSeriesList_new_1 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 1){
                            mCumulative_new_1 = changeOriginalDataToCount53(mCumulative, mSeriesList,0);
                            mSeriesList_new_1 = changeOriginalDataToCount53(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 2){
                            mCumulative_new_1 = changeOriginalDataToCount13(mCumulative, mSeriesList,0);
                            mSeriesList_new_1 = changeOriginalDataToCount13(mSeriesList, mSeriesList,1);
                        }
//                        double[][] mCumulative_new_1 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                        Log.e("ChartBuilder", "mCumulative_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative_new_1));

                        Log.e("ChartBuilder", "mSeriesList>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));
//                        double[][] mSeriesList_new_1 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        Log.e("ChartBuilder", "mSeriesList_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList_new_1));

                        double[] mAxisInfo_1 = CreateAxisInfoData(metaData, mSeriesList_new_1, false, false, mStatType);
                        Log.e("ChartBuilder", "mAxisInfo_1>>>>>>" + mChartType+ ">>>>"+ Arrays.toString(mAxisInfo_1));

                        JSONArray ja1 = new JSONArray();
                        ja1.put(changArrayToJSONArray(mSeriesList_new_1[0]));//天数
                        ja1.put(changArrayToJSONArray(mSeriesList_new_1[2]));//熟悉（柱状）->成熟柱状
                        ja1.put(changArrayToJSONArray(mSeriesList_new_1[1]));//有待熟悉（柱状）->年轻柱状
                        ja1.put(changArrayToJSONArray(mCumulative_new_1[1]));//累计（曲线）->累计曲线
                        ja1.put(changArrayToJSONArray(mAxisInfo_1));//坐标轴信息
                        data =ja1.toString();

                        break;
                    case REVIEW_COUNT://复习数量
                        stats.calculateDone(mType, true);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        double[][] mCumulative_new_2 = new double[mCumulative.length][];
                        double[][] mSeriesList_new_2 = new double[mSeriesList.length][];
                        if(mStatType == 0){
                            mCumulative_new_2 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                            mSeriesList_new_2 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 1){
                            mCumulative_new_2 = changeOriginalDataToCount53(mCumulative, mSeriesList,0);
                            mSeriesList_new_2 = changeOriginalDataToCount53(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 2){
                            mCumulative_new_2 = changeOriginalDataToCount13(mCumulative, mSeriesList,0);
                            mSeriesList_new_2 = changeOriginalDataToCount13(mSeriesList, mSeriesList,1);
                        }

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
//                        double[][] mCumulative_new_2 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                        Log.e("ChartBuilder", "mCumulative_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative_new_2));

                        Log.e("ChartBuilder", "mSeriesList>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));
//                        double[][] mSeriesList_new_2 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        Log.e("ChartBuilder", "mSeriesList_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList_new_2));

                        double[] mAxisInfo_2 = CreateAxisInfoData(metaData, mSeriesList_new_2, false, false, mStatType);
                        Log.e("ChartBuilder", "mAxisInfo_2>>>>>>" + mChartType+ ">>>>"+ Arrays.toString(mAxisInfo_2));

                        JSONArray ja2 = new JSONArray();
                        ja2.put(changArrayToJSONArray(mSeriesList_new_2[0]));//天数
                        ja2.put(changArrayToJSONArray(mSeriesList_new_2[3]));//有待熟悉（柱状）->年轻柱状
                        ja2.put(changArrayToJSONArray(mCumulative_new_2[3]));//有待熟悉（曲线）->年轻曲线
                        ja2.put(changArrayToJSONArray(mSeriesList_new_2[2]));//重新学习（柱状）->重新学习柱状
                        ja2.put(changArrayToJSONArray(mCumulative_new_2[2]));//重新学习（曲线）->重新学习曲线
                        ja2.put(changArrayToJSONArray(mSeriesList_new_2[1]));//学习（柱状）->学习柱状
                        ja2.put(changArrayToJSONArray(mCumulative_new_2[1]));//学习（曲线）->学习曲线
                        ja2.put(changArrayToJSONArray(mAxisInfo_2));//坐标轴信息

                        data =ja2.toString();

                        break;
                    case REVIEW_TIME://复习时间
                        stats.calculateDone(mType, false);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        double[][] mCumulative_new_3 = new double[mCumulative.length][];
                        double[][] mSeriesList_new_3 = new double[mSeriesList.length][];
                        if(mStatType == 0){
                            mCumulative_new_3 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                            mSeriesList_new_3 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 1){
                            mCumulative_new_3 = changeOriginalDataToCount53(mCumulative, mSeriesList,0);
                            mSeriesList_new_3 = changeOriginalDataToCount53(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 2){
                            mCumulative_new_3 = changeOriginalDataToCount13(mCumulative, mSeriesList,0);
                            mSeriesList_new_3 = changeOriginalDataToCount13(mSeriesList, mSeriesList,1);
                        }

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
//                        double[][] mCumulative_new_3 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                        Log.e("ChartBuilder", "mCumulative_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative_new_3));

                        Log.e("ChartBuilder", "mSeriesList>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));
//                        double[][] mSeriesList_new_3 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        Log.e("ChartBuilder", "mSeriesList_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList_new_3));

                        double[] mAxisInfo_3 = CreateAxisInfoData(metaData, mSeriesList_new_3, false, false, mStatType);
                        Log.e("ChartBuilder", "mAxisInfo_3>>>>>>" + mChartType+ ">>>>"+ Arrays.toString(mAxisInfo_3));

                        JSONArray ja3 = new JSONArray();
                        ja3.put(changArrayToJSONArray(mSeriesList_new_3[0]));//天数
                        ja3.put(changArrayToJSONArray(mSeriesList_new_3[3]));//有待熟悉（柱状）->年轻柱状
                        ja3.put(changArrayToJSONArray(mCumulative_new_3[3]));//有待熟悉（曲线）->年轻曲线
                        ja3.put(changArrayToJSONArray(mSeriesList_new_3[2]));//重新学习（柱状）->重新学习柱状
                        ja3.put(changArrayToJSONArray(mCumulative_new_3[2]));//重新学习（曲线）->重新学习曲线
                        ja3.put(changArrayToJSONArray(mSeriesList_new_3[1]));//学习（柱状）->学习柱状
                        ja3.put(changArrayToJSONArray(mCumulative_new_3[1]));//学习（曲线）->学习曲线
                        ja3.put(changArrayToJSONArray(mAxisInfo_3));//坐标轴信息
                        data =ja3.toString();

                        break;
                    case INTERVALS://间隔
                        stats.calculateIntervals((Activity)objects[0], mType);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        double[][] mCumulative_new_4 = new double[mCumulative.length][];
                        double[][] mSeriesList_new_4 = new double[mSeriesList.length][];
                        if(mStatType == 0){
                            mCumulative_new_4 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                            mSeriesList_new_4 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 1){
                            mCumulative_new_4 = changeOriginalDataToCount53(mCumulative, mSeriesList,0);
                            mSeriesList_new_4 = changeOriginalDataToCount53(mSeriesList, mSeriesList,1);
                        }
                        if(mStatType == 2){
                            mCumulative_new_4 = changeOriginalDataToCount13(mCumulative, mSeriesList,0);
                            mSeriesList_new_4 = changeOriginalDataToCount13(mSeriesList, mSeriesList,1);
                        }

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
//                        double[][] mCumulative_new_4 = changeOriginalDataToCount32(mCumulative, mSeriesList,0);
                        Log.e("ChartBuilder", "mCumulative_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative_new_4));

                        Log.e("ChartBuilder", "mSeriesList>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));
//                        double[][] mSeriesList_new_4 = changeOriginalDataToCount32(mSeriesList, mSeriesList,1);
                        Log.e("ChartBuilder", "mSeriesList_new>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList_new_4));

                        double[] mAxisInfo_4 = CreateAxisInfoData(metaData, mSeriesList_new_4, true, false, mStatType);
                        Log.e("ChartBuilder", "mAxisInfo_4>>>>>>" + mChartType+ ">>>>"+ Arrays.toString(mAxisInfo_4));

                        JSONArray ja4 = new JSONArray();
                        ja4.put(changArrayToJSONArray(mSeriesList_new_4[0]));//天数
                        ja4.put(changArrayToJSONArray(mSeriesList_new_4[1]));//卡片间隔 ->卡片间隔柱状
                        ja4.put(changArrayToJSONArray(mCumulative_new_4[1]));//累计百分比 ->累计百分比曲线
                        ja4.put(changArrayToJSONArray(mAxisInfo_4));//坐标轴信息

                        data =ja4.toString();

                        break;
                    case HOURLY_BREAKDOWN://按小时分类
                        stats.calculateBreakdown(mType);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
                        Log.e("ChartBuilder", "mSeriesList>>>>>>"  + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));

                        double[][] mSeriesList_new_5 = changeHourlyDataToCount24(mSeriesList);
                        Log.e("ChartBuilder", "mSeriesList_new_5>>>>>>"  + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList_new_5));

                        double[] mAxisInfo_5 = CreateAxisInfoData(metaData, mSeriesList_new_5, false, true, mStatType);
                        Log.e("ChartBuilder", "mAxisInfo_5>>>>>>" + mChartType+ ">>>>"+ Arrays.toString(mAxisInfo_5));

                        JSONArray ja5 = new JSONArray();
                        ja5.put(changArrayToJSONArray(mSeriesList_new_5[0]));//天数
                        ja5.put(changArrayToJSONArray(mSeriesList_new_5[1]));//正确百分比 ->正确
                        ja5.put(changArrayToJSONArray(mSeriesList_new_5[2]));//答案 ->答案
                        ja5.put(changArrayToJSONArray(mAxisInfo_5));//坐标轴信息

                        data =ja5.toString();

                        break;
                    case WEEKLY_BREAKDOWN://按周分类
                        stats.calculateWeeklyBreakdown(mType);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
                        Log.e("ChartBuilder", "mSeriesList>>>>>>"  + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));

                        break;
                    case ANSWER_BUTTONS://显示答案按钮
                        stats.calculateAnswerButtons(mType);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
                        Log.e("ChartBuilder", "mSeriesList>>>>>>"  + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));

                        break;
                    case CARDS_TYPES://卡牌类型
                        stats.calculateCardsTypes(mType);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
                        Log.e("ChartBuilder", "mSeriesList>>>>>>"  + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));

                        JSONArray ja6 = new JSONArray();

                        try {
                            //仅放一个数组类型的元素
                            ja6.put(mSeriesList[0][2]);//未学习 ->未看
                            ja6.put(mSeriesList[0][1]);//有待熟悉+学习 ->学习
                            ja6.put(mSeriesList[0][0]);//熟悉 ->熟练
                            ja6.put(mSeriesList[0][3]);//暂停 ->暂停
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        data =ja6.toString();
                        break;
                }

                mBackwards = (Boolean) metaData[2];
                mValueLabels = (int[]) metaData[3];
                mColors = (int[]) metaData[4];
                mAxisTitles = (int[]) metaData[5];
                mMaxCards = (Integer) metaData[7];
                mLastElement = (Double) metaData[10];
                mFirstElement = (Double) metaData[9];
                mHasColoredCumulative = (Boolean) metaData[19];
                mMcount = (Double) metaData[18];
                mDynamicAxis = (Boolean) metaData[20];
//                Log.e("ChartBuilder", "mCumulative>>>>>>" + mChartType+ ">>>>"+ Arrays.deepToString(mCumulative));
//                Log.e("ChartBuilder", "mSeriesList>>>>>>"  + mChartType+ ">>>>"+ Arrays.deepToString(mSeriesList));
                Log.e("ChartBuilder", "metaData>>>>>>"  + mChartType+ ">>>>"+ Arrays.deepToString(metaData));

            }finally {
                sLock.unlock();
            }

            //保存数据至SharedPreferences
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences((Activity)objects[0]);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(mChartType.toString(), data);
            editor.commit();
            return data;
        }

        @Override
        protected void onCancelled() {
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(String data) {

        }

    }

    //将最底层数组转换成Json数组格式
    private JSONArray changArrayToJSONArray(double[] array){
        JSONArray ja_in = new JSONArray();
        for(int i = 0;i < array.length; i++){
            try {
                ja_in.put((float)array[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return ja_in;
    }



    /**
     * 将从数据库查询得到的原始数据补全为32位的格式(mStatType = 0 时，查询到的数据为“一个月”)
     * @param data_to_be_process 需要处理的二维数组数据
     * @param dataHasday 确保内部含有天数的二维数组数据（应传入mSeriesList）
     * @param tag 标签，0 代表要处理的为mCumulative，1 代表要处理的为mSeriesList
     * @return 返回补全好的每个子数组元素个数为32的二维数组
     */
    private double[][] changeOriginalDataToCount32(double[][] data_to_be_process, double[][] dataHasday, int tag){
        double[][] data_to_be_return = new double[data_to_be_process.length][];
        for(int i = 0; i < data_to_be_process.length; i++){
            double[] array_day = dataHasday[0];
            double[] array_other;
            double[] new_array_1= new double[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
            double[] new_array_2= new double[]{-31,-30,-29,-28,-27,-26,-25,-24,-23,-22,-21,-20,-19,-18,-17,-16,-15,-14,-13,-12,-11,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0};
            double[] new_array_3= new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            if(array_day[0] < 0 && array_day[array_day.length-1] > 0){//此情况为预测的天数数据类型（-31到31）
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        if (array_day[j] >= 0){//舍去小于0的天数数据
                            new_array_1[(int)(array_day[j])] = array_day[j];
                        }
                    }
                    data_to_be_return[0] = new_array_1;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        if(array_day[k] >= 0){
                            new_array_3[(int)(array_day[k])] = array_other[k];
                        }
                    }
                    data_to_be_return[i] = new_array_3;
                }

            }
            if (array_day[0] >= 0){//此情况为天数从0到31的情况
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        new_array_1[(int)(array_day[j])] = array_day[j];
                    }
                    data_to_be_return[0] = new_array_1;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        new_array_3[(int)(array_day[k])] = array_other[k];
                    }
                    data_to_be_return[i] = new_array_3;
                }
            }
            if (array_day[0] < 0 && array_day[array_day.length-1] == 0) {//此情况为天数从-31到0的情况
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        new_array_2[(int)(array_day[j]) + 31] = array_day[j];
                    }
                    data_to_be_return[0] = new_array_2;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        new_array_3[(int)(array_day[k]) + 31] = array_other[k];
                    }
                    data_to_be_return[i] = new_array_3;
                }
            }

        }

        if(tag == 0){//如果传入的待处理的数据为mCumulative, 则将数组内两个大于0.0的数据之间的所有0.0补全为与前一个大于0.0的数据相同的值
            for(int i = 1; i < data_to_be_return.length; i++){
                for(int j = 0; j < data_to_be_return[i].length - 1; j++){
                    if (data_to_be_return[i][j] > data_to_be_return[i][j + 1]){
                        data_to_be_return[i][j + 1] = data_to_be_return[i][j];
                    }
                }
            }
        }
        return data_to_be_return;
    }

    /**
     * 将从数据库查询得到的原始数据补全为53位的格式(mStatType = 1 时，查询到的数据为“一年”52周)
     * @param data_to_be_process 需要处理的二维数组数据
     * @param dataHasday 确保内部含有天数的二维数组数据（应传入mSeriesList）
     * @param tag 标签，0 代表要处理的为mCumulative，1 代表要处理的为mSeriesList
     * @return 返回补全好的每个子数组元素个数为53的二维数组
     */
    private double[][] changeOriginalDataToCount53(double[][] data_to_be_process, double[][] dataHasday, int tag){
        double[][] data_to_be_return = new double[data_to_be_process.length][];
        for(int i = 0; i < data_to_be_process.length; i++){
            double[] array_day = dataHasday[0];
            double[] array_other;
            double[] new_array_1 = new double[53];
            double[] new_array_2 = new double[53];
            double[] new_array_3 = new double[53];
            for(int count = 0; count < 53; count++){
                new_array_1[count] = count;
                new_array_2[count] = count - 52;
                new_array_3[count] = 0;
            }
            if(array_day[0] < 0 && array_day[array_day.length-1] > 0){//此情况为预测的天数数据类型（-52到52）
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        if (array_day[j] >= 0){//舍去小于0的天数数据
                            new_array_1[(int)(array_day[j])] = array_day[j];
                        }
                    }
                    data_to_be_return[0] = new_array_1;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        if(array_day[k] >= 0){
                            new_array_3[(int)(array_day[k])] = array_other[k];
                        }
                    }
                    data_to_be_return[i] = new_array_3;
                }

            }
            if (array_day[0] >= 0){//此情况为天数从0到52的情况
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        new_array_1[(int)(array_day[j])] = array_day[j];
                    }
                    data_to_be_return[0] = new_array_1;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        new_array_3[(int)(array_day[k])] = array_other[k];
                    }
                    data_to_be_return[i] = new_array_3;
                }
            }
            if (array_day[0] < 0 && array_day[array_day.length-1] == 0) {//此情况为天数从-52到0的情况
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        new_array_2[(int)(array_day[j]) + 52] = array_day[j];
                    }
                    data_to_be_return[0] = new_array_2;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        new_array_3[(int)(array_day[k]) + 52] = array_other[k];
                    }
                    data_to_be_return[i] = new_array_3;
                }
            }

        }

        if(tag == 0){//如果传入的待处理的数据为mCumulative, 则将数组内两个大于0.0的数据之间的所有0.0补全为与前一个大于0.0的数据相同的值
            for(int i = 1; i < data_to_be_return.length; i++){
                for(int j = 0; j < data_to_be_return[i].length - 1; j++){
                    if (data_to_be_return[i][j] > data_to_be_return[i][j + 1]){
                        data_to_be_return[i][j + 1] = data_to_be_return[i][j];
                    }
                }
            }
        }
        return data_to_be_return;
    }

    /**
     * 将从数据库查询得到的原始数据补全为13位的格式(mStatType = 2 时，查询到的数据为“所有时间”（暂时设定上限为12个月）)
     * @param data_to_be_process 需要处理的二维数组数据
     * @param dataHasday 确保内部含有天数的二维数组数据（应传入mSeriesList）
     * @param tag 标签，0 代表要处理的为mCumulative，1 代表要处理的为mSeriesList
     * @return 返回补全好的每个子数组元素个数为13的二维数组
     */
    private double[][] changeOriginalDataToCount13(double[][] data_to_be_process, double[][] dataHasday, int tag){
        double[][] data_to_be_return = new double[data_to_be_process.length][];
        for(int i = 0; i < data_to_be_process.length; i++){
            double[] array_day = dataHasday[0];
            double[] array_other;
            double[] new_array_1 = new double[13];
            double[] new_array_2 = new double[13];
            double[] new_array_3 = new double[13];
            for(int count = 0; count < 13; count++){
                new_array_1[count] = count;
                new_array_2[count] = count - 12;
                new_array_3[count] = 0;
            }
            if(array_day[0] < 0 && array_day[array_day.length-1] > 0){//此情况为预测的天数数据类型（-12到12）
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        if (array_day[j] >= 0){//舍去小于0的天数数据
                            new_array_1[(int)(array_day[j])] = array_day[j];
                        }
                    }
                    data_to_be_return[0] = new_array_1;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        if(array_day[k] >= 0){
                            new_array_3[(int)(array_day[k])] = array_other[k];
                        }
                    }
                    data_to_be_return[i] = new_array_3;
                }

            }
            if (array_day[0] >= 0){//此情况为天数从0到12的情况
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        new_array_1[(int)(array_day[j])] = array_day[j];
                    }
                    data_to_be_return[0] = new_array_1;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        new_array_3[(int)(array_day[k])] = array_other[k];
                    }
                    data_to_be_return[i] = new_array_3;
                }
            }
            if (array_day[0] < 0 && array_day[array_day.length-1] == 0) {//此情况为天数从-12到0的情况
                if ( i == 0){
                    for(int j = 0; j < array_day.length; j++){
                        new_array_2[(int)(array_day[j]) + 12] = array_day[j];
                    }
                    data_to_be_return[0] = new_array_2;
                }else {
                    array_other = data_to_be_process[i];
                    for(int k = 0; k < array_other.length; k++){
                        new_array_3[(int)(array_day[k]) + 12] = array_other[k];
                    }
                    data_to_be_return[i] = new_array_3;
                }
            }

        }

        if(tag == 0){//如果传入的待处理的数据为mCumulative, 则将数组内两个大于0.0的数据之间的所有0.0补全为与前一个大于0.0的数据相同的值
            for(int i = 1; i < data_to_be_return.length; i++){
                for(int j = 0; j < data_to_be_return[i].length - 1; j++){
                    if (data_to_be_return[i][j] > data_to_be_return[i][j + 1]){
                        data_to_be_return[i][j + 1] = data_to_be_return[i][j];
                    }
                }
            }
        }
        return data_to_be_return;
    }

    /**
     * 将从数据库查询得到的按小时分类的数据补全为24位的格式
     * @param data 原始数据
     * @return 24个元素的数据
     */
    private double[][] changeHourlyDataToCount24(double[][] data){
        double[][] data_to_be_return = new double[data.length][];
        for(int i = 0; i < data.length; i++){
            double[] new_array_1 = new double[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
            double[] new_array_2 = new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

            if ( i == 0){
                data_to_be_return[0] = new_array_1;
            }else {
                for(int j = 0; j < data[i].length; j++){
                    if(data[0][j] == 20){
                        new_array_2[0] = data[i][j];
                    }else{
                        new_array_2[(int)(data[0][j] + 4)] = data[i][j];//数据库中的时间从凌晨4点开始计算
                    }
                }
                data_to_be_return[i] = new_array_2;
            }
        }
        return data_to_be_return;
    }

    /**
     * 构建坐标轴信息的方法
     * @param data 待处理的metaData的数据
     * @return 坐标轴信息数组  如[0.0, 31.0, 4.0, 0.0, 19.95, 4.0, 0.0, 100.0, 20.0, 0]
     */
    private double[] CreateAxisInfoData(Object[] data, double[][] seriesList, boolean isIntervals, boolean isHourly, int mStatType){
//        double[] mAxisInfo = new double[9];
        double[] mAxisInfo = new double[10];
        mAxisInfo[0] = (double)data[9];//X轴起始值 mFirstElement
        mAxisInfo[1] = (double)data[10];//X轴结束值 mLastElement
        if(mStatType == 0 || isHourly){
            mAxisInfo[2] = 4;//X轴刻度间隔ticks 4
        }else if(mStatType == 1){
            mAxisInfo[2] = 12;//X轴刻度间隔ticks 13
        }else if(mStatType == 2){
            mAxisInfo[2] = 2;//X轴刻度间隔ticks 2
        }

        mAxisInfo[3] = 0;//左Y轴起始值 0
        if(isHourly){
            mAxisInfo[4] = 100;//如果是按小时分类数据,则左Y轴最结束值设定100
        }else{
            mAxisInfo[4] = cumulativeMaxValue(seriesList) * 1.05;//左Y轴结束值(设定为最大值的1.05倍，避免柱状图超出与图表顶部，或与顶部齐平)
        }
        DecimalFormat df = new DecimalFormat("#.0");
        mAxisInfo[5] = Double.parseDouble(df.format(mAxisInfo[4]/5));//左Y轴刻度间隔ticks （取值方法有待商榷）

        mAxisInfo[6] = 0;//右Y轴起始值 0
        if(isIntervals){
            mAxisInfo[7] = 100;//如果是间隔数据,则右Y轴最结束值设定100
        }else{
            mAxisInfo[7] = (double)data[18] * 1.05;//右Y轴结束值 mMcount
        }
        mAxisInfo[8] = Double.parseDouble(df.format(mAxisInfo[7]/5));//右Y轴刻度间隔ticks （取值方法有待商榷）

        if(mStatType == 0){
            mAxisInfo[9] = 0;//X轴单位标识符 0 代表天
        }else if (mStatType == 1){
            mAxisInfo[9] = 1;//X轴单位标识符 1 代表周
        }else if (mStatType == 2){
            mAxisInfo[9] = 2;//X轴单位标识符 2 代表月
        }
        return mAxisInfo;
    }

    /**
     * 获取左Y轴的上限值，将mSeriesList的各个最大值累加
     * @param data
     * @return mSeriesList中各个一维数组内最大元素值之和
     */
    private double cumulativeMaxValue(double[][] data){
        double value = 0;
        for(int i = 1; i < data.length;i++ ){
            double temp = 0 ;
            for(int j = 0; j < data[i].length; j++){
                if(data[i][j] > temp){
                    temp = data[i][j];
                }
            }
            value += temp;
        }
        return value;
    }

    /**
     * //获取所有牌组1年时间内的复习计数数据的方法（REVIEW_COUNT）
     */
    private class CreateChartTask3 extends AsyncTask<Object, Void, String>{
        private ChartView mImageView;
        private ProgressBar mProgressBar;


        private boolean mIsRunning = false;
        private Stats.ChartType mChartType;

        public CreateChartTask3(Stats.ChartType chartType){
            super();
            mIsRunning = true;
            mChartType = chartType;
        }

        @Override
        protected String doInBackground(Object... objects) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();

            String data = null;//要返回的字符串数据
            try {
                if (!mIsRunning) {
                    Timber.d("quiting CreateChartTask (%s) before execution", mChartType.name());
                    return null;
                } else
                    Timber.d("starting Create ChartTask, type: %s", mChartType.name());

                mType = mStatType;
                mIsWholeCollection = true;//此处限定为所有牌组
                Stats stats = new Stats(mCollectionData, mIsWholeCollection);//此处限定为所有牌组
                Object[] metaData = null;

                switch (mChartType){
                    case REVIEW_COUNT://复习数量
                        mType = 1;//此处限定选择的时间为1年
                        stats.calculateDone(mType, true);
                        mCumulative = stats.getCumulative();
                        mSeriesList = stats.getSeriesList();
                        metaData = stats.getMetaInfo();

                        double totalReviewCountNumber_double = mCumulative[1][mCumulative[1].length - 1]
                               + mCumulative[2][mCumulative[2].length -1] + mCumulative[3][mCumulative[3].length -1];

                        int totalReviewCountNumber = (int)totalReviewCountNumber_double;
                        Log.e("AnkiStatsTaskHandler", "totalReviewCountNumber>>>>>>" + totalReviewCountNumber);
                        data = String.valueOf(totalReviewCountNumber);
                        break;
                }

            }finally {
                sLock.unlock();
            }
            //保存数据至SharedPreferences
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences((Activity)objects[0]);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("AllDeckReviewCount", data);
            editor.commit();
            return data;
        }

        @Override
        protected void onCancelled() {
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(String data) {

        }

    }

    /////////////////////////////////zc-add-end//////////////////////////////////////////


    private class CreateStatisticsOverview extends AsyncTask<View, Void, String>{
        private WebView mWebView;
        private ProgressBar mProgressBar;

        private boolean mIsRunning = false;

        public CreateStatisticsOverview(){
            super();
            mIsRunning = true;
        }

        @Override
        protected String doInBackground(View... params) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();
            try {
                if (!mIsRunning) {
                    Timber.d("quiting CreateStatisticsOverview before execution");
                    return null;
                } else
                    Timber.d("starting CreateStatisticsOverview" );
                mWebView = (WebView) params[0];
                mProgressBar = (ProgressBar) params[1];
                String html = "";
                InfoStatsBuilder infoStatsBuilder = new InfoStatsBuilder(mWebView, mCollectionData, mIsWholeCollection);
                html = infoStatsBuilder.createInfoHtmlString();
                return html;
            }finally {
                sLock.unlock();
            }
        }

        @Override
        protected void onCancelled() {
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(String html) {
            if(html != null && mIsRunning){

                try {
                    mWebView.loadData(URLEncoder.encode(html, "UTF-8").replaceAll("\\+"," "), "text/html; charset=utf-8",  "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.invalidate();

            }
        }

    }

    /////////////////////////////////zc-add-begin//////////////////////////////////////////
    private class CreateStatisticsOverview2 extends AsyncTask<Object, Void, int[]>{
        private WebView mWebView;
        private ProgressBar mProgressBar;

        private boolean mIsRunning = false;

        public CreateStatisticsOverview2(){
            super();
            mIsRunning = true;
        }

        @Override
        protected int[] doInBackground(Object... objects) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();
            try {
                if (!mIsRunning) {
                    Timber.d("quiting CreateStatisticsOverview before execution");
                    return null;
                } else
                    Timber.d("starting CreateStatisticsOverview" );
//                mWebView = (WebView) params[0];
//                mProgressBar = (ProgressBar) params[1];
//                String html = "";
//                InfoStatsBuilder infoStatsBuilder = new InfoStatsBuilder(mWebView, mCollectionData, mIsWholeCollection);
//                html = infoStatsBuilder.createInfoHtmlString();

                Stats stats = new Stats(mCollectionData, mIsWholeCollection);
                int[] todayStats = stats.calculateTodayStats();
                Log.e("StatisticsOverview2", ">>>>>>>>>>>>>" + Arrays.toString(todayStats));

                //保存数据至SharedPreferences
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences((Activity)objects[0]);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("TODAYSTATS", Arrays.toString(todayStats));
                editor.commit();
                return todayStats;
            }finally {
                sLock.unlock();
            }
        }

        @Override
        protected void onCancelled() {
//            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(int[] html) {
//            if(html != null && mIsRunning){
//
//                try {
//                    mWebView.loadData(URLEncoder.encode(html, "UTF-8").replaceAll("\\+"," "), "text/html; charset=utf-8",  "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                mProgressBar.setVisibility(View.GONE);
//                mWebView.setVisibility(View.VISIBLE);
//                mWebView.invalidate();

//            }
        }

    }
    /////////////////////////////////zc-add-end//////////////////////////////////////////


    private static class CreateSmallTodayOverview extends AsyncTask<Object, Void, String>{
        private TextView mTextView;

        private boolean mIsRunning = false;

        public CreateSmallTodayOverview(){
            super();
            mIsRunning = true;
        }

        @Override
        protected String doInBackground(Object... params) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();
            try {
                Collection collection = (Collection) params[0];
                if (!mIsRunning || collection == null || collection.getDb() == null) {
                    Timber.d("quiting CreateSmallTodayOverview before execution");
                    return null;
                } else
                    Timber.d("starting CreateSmallTodayOverview" );
                mTextView = (TextView) params[1];

                //eventually put this in Stats (in desktop it is not though)
                int cards;
                int minutes;
                Cursor cur = null;
                String query = "select count(), sum(time)/1000 from revlog where id > " + ((collection.getSched().getDayCutoff()-86400)*1000);
                Timber.d("CreateSmallTodayOverview query: " + query);

                try {
                    cur = collection.getDb()
                            .getDatabase()
                            .rawQuery(query, null);

                    cur.moveToFirst();
                    cards = cur.getInt(0);
                    minutes = (int) Math.round(cur.getInt(1)/60.0);
                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                    }
                }
                Resources res = mTextView.getResources();
                final String span = res.getQuantityString(R.plurals.time_span_minutes, minutes, minutes);
                return res.getQuantityString(R.plurals.studied_cards_today, cards, cards, span);
            }finally {
                sLock.unlock();
            }
        }

        @Override
        protected void onCancelled() {
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(String todayStatString) {
            if(todayStatString != null && mIsRunning){
                mTextView.setText(todayStatString);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.invalidate();
            }
        }

    }

    private static class CreateFirstStatisticChooserTask extends AsyncTask<Object, Void, Integer>{
        private ViewPager mViewPager;

        private boolean mIsRunning = false;

        public CreateFirstStatisticChooserTask(){
            super();
            mIsRunning = true;
        }

        @Override
        protected Integer doInBackground(Object... params) {
            //make sure only one task of CreateChartTask is running, first to run should get sLock
            //only necessary on lower APIs because after honeycomb only one thread is used for all asynctasks
            sLock.lock();
            try {
                Collection collection = (Collection) params[0];
                if (!mIsRunning || collection == null || collection.getDb() == null) {
                    Timber.d("quiting CreateTodayLearnCountOnly before execution");
                    return null;
                } else
                    Timber.d("starting CreateTodayLearnCountOnly" );
                mViewPager = (ViewPager) params[1];

                //eventually put this in Stats (in desktop it is not though)
                int cards;
                Cursor cur = null;
                String query = "select count() from revlog where id > " + ((collection.getSched().getDayCutoff()-86400)*1000);
                Timber.d("CreateSmallTodayOverview query: " + query);

                try {
                    cur = collection.getDb()
                            .getDatabase()
                            .rawQuery(query, null);

                    cur.moveToFirst();
                    cards = cur.getInt(0);



                } finally {
                    if (cur != null && !cur.isClosed()) {
                        cur.close();
                    }
                }
                Log.e("cards", ">>>>>>>>>>>>>>>" + cards);
                return cards;
            }finally {
                sLock.unlock();
            }
        }

        @Override
        protected void onCancelled() {
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(Integer todayStatString) {
            if(todayStatString != null && mIsRunning && mViewPager != null){
                int chosen = todayStatString;
                switch (chosen){
                    case 0:
                        mViewPager.setCurrentItem(Statistics.FORECAST_TAB_POSITION);
                }
            }
        }

    }


    public float getmStandardTextSize() {
        return mStandardTextSize;
    }
    public void setmStandardTextSize(float mStandardTextSize) {
        this.mStandardTextSize = mStandardTextSize;
    }

    public int getStatType() {
        return mStatType;
    }

    public void setStatType(int mStatType) {
        this.mStatType = mStatType;
    }

}
