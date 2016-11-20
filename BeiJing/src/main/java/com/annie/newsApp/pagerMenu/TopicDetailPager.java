package com.annie.newsApp.pagerMenu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.annie.newsApp.base.MenuBasePager;

public class TopicDetailPager extends MenuBasePager {

	public TopicDetailPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(mContext);
		textView.setText("专题详情");
		return textView;
	}

}
