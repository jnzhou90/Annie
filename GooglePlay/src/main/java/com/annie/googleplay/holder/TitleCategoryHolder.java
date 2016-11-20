package com.annie.googleplay.holder;

import com.annie.googleplay.R;
import com.annie.googleplay.global.GooglePlayApplication;

import android.view.View;
import android.widget.TextView;

public class TitleCategoryHolder extends BaseHolder {

	private TextView mTitle;

	@Override
	public View initView() {
		//初始化大标题分类的view
		View view = View.inflate(GooglePlayApplication.context, R.layout.title_category,null);
		mTitle = (TextView) view.findViewById(R.id.title_category);
		return view;
	}

	@Override
	public void setBindData(Object data) {
		String title = (String) data;
		mTitle.setText(title);
	}

}
