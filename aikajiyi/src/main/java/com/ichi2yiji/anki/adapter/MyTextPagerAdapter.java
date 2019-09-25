package com.ichi2yiji.anki.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

public class MyTextPagerAdapter extends PagerAdapter {
	
	private List<GridView> gvList;
	
	public void setData(List<GridView> gvList){
		this.gvList = gvList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(gvList == null){
			return 0;
		}
		return gvList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(gvList.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View v=gvList.get(position);
		ViewGroup parent = (ViewGroup) v.getParent();
		//Log.i("ViewPaperAdapter", parent.toString());
		if (parent != null) {
			parent.removeAllViews();
		}
		container.addView(gvList.get(position));
		return gvList.get(position);
	}
	
	@Override    
	public int getItemPosition(Object object) {    
	    return POSITION_NONE;    
	}   
	
}
