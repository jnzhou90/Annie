package com.annie.newsApp.base;

import android.content.Context;
import android.view.View;

/**
 * 左侧菜单对应的4个菜单界面
 * 
 * @author h
 * 
 */
public abstract class MenuBasePager {
	public Context mContext;
	public View rootView;// 给新闻中心的Framelayout展示

	public MenuBasePager(Context context) {
		this.mContext = context;
		rootView = initView();
	}

	/**
	 * 让子类返回布局，必须实现
	 * 
	 * @return
	 */
	public abstract View initView();

	/**
	 * 子类更新布局，不必须实现
	 */
	public void initData() {
	}
}
