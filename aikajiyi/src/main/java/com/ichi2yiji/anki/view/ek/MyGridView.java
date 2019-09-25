package com.ichi2yiji.anki.view.ek;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决与scrollview的冲突
 * 
 * @author shiqing 2015-6-25
 * 
 */
public class MyGridView extends GridView {

	private float mDownX;
	private float mDownY;

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
//		int childCount = getChildCount();
		
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/*
	 * @Override public boolean dispatchTouchEvent(MotionEvent ev) { // TODO
	 * Auto-generated method stub switch (ev.getAction()) { case
	 * MotionEvent.ACTION_DOWN: mDownX = ev.getX(); mDownY = ev.getY();
	 * getParent().getParent().requestDisallowInterceptTouchEvent(true); break;
	 * case MotionEvent.ACTION_MOVE: if (Math.abs(ev.getX() - mDownX) <
	 * Math.abs(ev.getY() - mDownY)) { getParent().getParent()
	 * .requestDisallowInterceptTouchEvent(true); } else {
	 * getParent().getParent().requestDisallowInterceptTouchEvent( false); }
	 * break; case MotionEvent.ACTION_UP: case MotionEvent.ACTION_CANCEL:
	 * getParent().getParent().requestDisallowInterceptTouchEvent(false); break;
	 * default:
	 * getParent().getParent().requestDisallowInterceptTouchEvent(true); break;
	 * 
	 * }
	 * 
	 * return super.dispatchTouchEvent(ev); }
	 */

}
