package com.ichi2yiji.anki.adapter;


import android.support.v7.widget.GridLayoutManager;
import android.util.Log;


/**
 * Created by Administrator on 2017/4/4.
 */
public class SectionedSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private SectionedRecyclerViewAdapter<?, ?, ?> adapter = null;
    private GridLayoutManager gridLayoutManager = null;

    public SectionedSpanSizeLookup(SectionedRecyclerViewAdapter<?, ?, ?> adapter, GridLayoutManager gridLayoutManager) {
        this.adapter = adapter;
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        if(adapter.isSectionHeaderPosition(position) || adapter.isSectionFooterPosition(position)){
            return gridLayoutManager.getSpanCount();
        }else{
            return 1;
        }
    }
}