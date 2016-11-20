package com.annie.newsApp.pagerMenu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.annie.newsApp.base.MenuBasePager;

public class InterractDetailPager extends MenuBasePager {

	public InterractDetailPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView textView = new TextView(mContext);
		textView.setText("互动详情");
		return textView;
	}

}
