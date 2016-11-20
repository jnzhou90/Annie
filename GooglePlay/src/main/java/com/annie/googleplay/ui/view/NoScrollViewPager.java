package com.annie.googleplay.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
		
			//这里需要重写这个构造,这个构造函数是布局文件中调用的
			public NoScrollViewPager(Context context, AttributeSet attrs) {
				super(context, attrs);
			}
		
			//这个是在代码中调用的
			/*public NoScrollViewPager(Context context) {
				super(context);
			}*/
		
			@Override
			public boolean onTouchEvent(MotionEvent ev) {
				//返回true表示自己消耗掉该事件
				return false;
			}
		}