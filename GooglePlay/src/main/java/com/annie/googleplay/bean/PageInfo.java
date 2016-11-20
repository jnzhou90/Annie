package com.annie.googleplay.bean;

import android.support.v4.app.Fragment;

/**
 * @Description: 封装每一页的数据,包含标题和fragment
 * @author Free
 * @date 2016-6-26 下午5:26:52
 */
public class PageInfo {
	
	private String title;
	private Fragment fragment;
	
	public PageInfo(String title, Fragment fragment) {
		super();
		this.title = title;
		this.fragment = fragment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	
}
