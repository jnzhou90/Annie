package com.annie.newsApp.pager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.annie.newsApp.base.BasePager;

public class SettingPager extends BasePager {

	public SettingPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		// System.out.println("设置加载数据");
		mTitle.setText("设置");
		mMenuIcon.setVisibility(View.GONE);

		TextView textView = new TextView(mContext);
		textView.setText("设置Fragment");
		textView.setGravity(Gravity.CENTER);

		// 先清空之前的所有view再添加新的view
		mContainer.removeAllViews();
		mContainer.addView(textView);
	}
}
