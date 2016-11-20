package com.annie.newsApp.pager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.annie.newsApp.base.BasePager;

public class GoveraffairsPager extends BasePager {

	public GoveraffairsPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		// System.out.println("政务加载数据");
		mTitle.setText("政务");

		TextView textView = new TextView(mContext);
		textView.setText("政务Fragment");
		textView.setGravity(Gravity.CENTER);

		// 先清空之前的所有view再添加新的view
		mContainer.removeAllViews();
		mContainer.addView(textView);
	}
}
