/****************************************************************************************
 * Copyright (c) 2014 Michael Goldbach <michael@wildplot.com>                           *
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


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import android.view.View;

import com.ichi2yiji.anki.Statistics;
import com.ichi2yiji.anki.StatisticsAikaOriginal;
import com.wildplot.android.rendering.PlotSheet;
import com.wildplot.android.rendering.graphics.wrapper.GraphicsWrap;
import com.wildplot.android.rendering.graphics.wrapper.RectangleWrap;

import timber.log.Timber;

public class ChartView extends View{

//    private Statistics.ChartFragment mFragment;
    private Statistics.ChartFragment mmFragment;//zc change
    private StatisticsAikaOriginal.ChartFragment mFragment;//zc add
    private RectangleWrap mRectangle;
    private PlotSheet mPlotSheet;
    private boolean mDataIsSet;

    //The following constructors are needed for the layout inflater
    public ChartView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //Timber.d("drawing chart");
        if(mDataIsSet){
            //Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            GraphicsWrap g = new GraphicsWrap(canvas, paint);

            Rect field = new Rect();
            this.getDrawingRect(field);
            mRectangle = new RectangleWrap(field);
            g.setClip(mRectangle);
            if(mPlotSheet != null){
                mPlotSheet.paint(g);
            }
            else {
                super.onDraw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }

    }

    public void addFragment(Statistics.ChartFragment fragment){
//        mFragment = fragment;
        mmFragment = fragment;//zc change
    }

    /////////////////////////zc add begin///////////////////////////
    public void addFragment(StatisticsAikaOriginal.ChartFragment fragment){
        mFragment = fragment;
    }
    /////////////////////////zc add end///////////////////////////

    public void setData(PlotSheet plotSheet){
        mPlotSheet = plotSheet;
        mDataIsSet = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Timber.d("ChartView sizeChange!");
        if(mFragment != null)
            mFragment.checkAndUpdate();
    }
}
