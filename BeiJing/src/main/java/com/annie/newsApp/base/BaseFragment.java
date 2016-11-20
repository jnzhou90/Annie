package com.annie.newsApp.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	protected Activity mActivity; // 与当前Fragment建立联系的activity,也就是让子类使用的context

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		View view = initView();
		return view;
	}

	/**
	 * 让子类实现，提供子类的布局
	 */
	protected abstract View initView();

	// 在onCreateView方法之后执行!!!
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 修改布局,跟新控件
		initData();
	}

	/**
	 * 让子类去修改布局，更新控件,不必须实现
	 */
	protected void initData() {
	}
}
