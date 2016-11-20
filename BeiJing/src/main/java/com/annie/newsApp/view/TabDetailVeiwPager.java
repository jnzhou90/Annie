package com.annie.newsApp.view;

import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TabDetailVeiwPager extends ViewPager {

	private int downX;
	private int downY;

	public TabDetailVeiwPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 1、不处理上下滑动，让父容器拦截，getParent().requestDisallowInterceptTouchEvent(false);
	 * 2、左右滑动
	 * 	2.1、第1页时，且手指从左往右，不处理事件
	 * 	2.2、最后一页时，且手指从右往左，不处理事件
	 * 	2.3、其他情况，自己处理事件，不让父容器拦截事件getParent().requestDisallowInterceptTouchEvent(true);
	 *
	 */
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//请求父元素不拦截事件
		getParent().requestDisallowInterceptTouchEvent(true);
		
		// 1.不处理上下滑动
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			
			//因为在安卓中像viewgroup这些控件拦截的都是move事件,至少会将down事件传下来,
			//所以为了保证move事件可以执行,在down事件中应请求父控件不要拦截事件
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			
			//获取移动的X,Y的距离,需要去绝对值!!!!!
			int distanceX = Math.abs(moveX - downX);
			int distanceY = Math.abs(moveY - downY);
			if (distanceY > distanceX) {
				//请求父控件拦截事件
				getParent().requestDisallowInterceptTouchEvent(false);
			}else {
				// 2.1 如果是第一页,且由左向右滑动,请求父控件拦截事件
				if (getCurrentItem() ==0 && (moveX - downX) > 0) {
					getParent().requestDisallowInterceptTouchEvent(false);
				}else if (getCurrentItem() == getAdapter().getCount() - 1 && (moveX - downX) < 0 ) {
					
					// 2.2 如果是最后一页,且由右向左滑动时,请求父控件拦截事件
					getParent().requestDisallowInterceptTouchEvent(false);
				}else {
					// 2.3 其他情况,则需要请求父控件不拦截事件,由孩子来处理事件
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
