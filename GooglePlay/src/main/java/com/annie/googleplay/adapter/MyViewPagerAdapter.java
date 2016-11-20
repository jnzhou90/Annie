package com.annie.googleplay.adapter;

import java.util.ArrayList;
import java.util.List;

import com.annie.googleplay.bean.PageInfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

	private List<PageInfo> list;
	public MyViewPagerAdapter(FragmentManager fm,List<PageInfo> list) {
		super(fm);
		this.list = list;
	}

	/**
	 * 返回指定position的fragment
	 */
	@Override
	public Fragment getItem(int position) {
		return list.get(position).getFragment();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	/**
	 * 返回指定的标题
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return list.get(position).getTitle();
	}
}
