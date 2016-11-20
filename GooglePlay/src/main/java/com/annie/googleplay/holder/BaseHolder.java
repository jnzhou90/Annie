package com.annie.googleplay.holder;

import android.view.View;

public abstract class BaseHolder<T> {

	// 使用变量记录convertview,就是说getView的convertview的初始化已经移动到构造里了
	protected View holderView;
	
	public BaseHolder() {
		// 初始化holderview
		holderView = initView();
		// 3.设置Tag
		holderView.setTag(this);
	}
	
	/**
	 * @Description: 初始化holderview
	 * @param:
	 */
	public abstract View initView();
	
	/**
	 * @Description: 绑定数据的方法
	 * @param:
	 */
	public abstract void setBindData(T data);
	
	// 返回holderView对象
	public View getHolderView() {
		return holderView;
	}
}
