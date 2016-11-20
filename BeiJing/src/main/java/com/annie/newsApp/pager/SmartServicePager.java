package com.annie.newsApp.pager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.annie.newsApp.base.BasePager;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		// System.out.println("智慧服务加载数据");
		mTitle.setText("智慧服务");

		TextView textView = new TextView(mContext);
		textView.setText("智慧服务Fragment");
		textView.setGravity(Gravity.CENTER);

		// 先清空之前的所有view再添加新的view
		mContainer.removeAllViews();
		mContainer.addView(textView);
	}
}
