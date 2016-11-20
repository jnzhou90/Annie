package com.annie.googleplay.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {
	protected  LaodingPage laodingPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		/**
		 * 由于viewpager的预加载功能,当滑到后几页的时候最开始的首页,就会被删除,当用户重新点击首页,
		 * 返回首页界面时需要重新加载界面,这样不仅浪费流量还会影响用户体验,所以应该改造一下,当重新返回首页
		 * 时不需要重新加载(页面刷新由用户手动上拉刷新下拉加载来完成),
		 * 这就需要先判断一下,只有当laodingpager对象为空时,才去重新创建laodingpager对象
		 */
		if (laodingPage == null) {
			
			laodingPage = new LaodingPage(getActivity()) {
				@Override
				protected View createSuccessPage() {
					
					//加载数据,刷新界面
					//loadDataAndRefreshPage();
					//具体的加载完成的界面各不相同,所以应交由子类去 实现
						return getSuccessPage();
				}
				
				@Override
				protected Object loadData() {
					
					//各个界面需要请求的数据各不相同,所以需要子类自己去实现
		 			return requestData();
		 			
				}
			};
		}else {
			//注: 只有在低版本的v4中的FragmentManager中才会这样：在高版本5.0 的v4或studio中都不会出现
			//这种情况
			//第二次进来不是空，是有爹的孩子，先打印一下孩子的爹是谁,那么
			System.out.println(laodingPage.getParent().getClass().getSimpleName());
			//获取它爹
			ViewParent parent = laodingPage.getParent();
			//需要将LoadingPage从它爹中移除,但它爹中没有removeView方法,所以 将其强转为viewgroup类型
			if (parent != null && parent instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) parent;
				//移除子View
				group.removeView(laodingPage);
			}
		}
		
		//若为加载成功,则返回loadingpage 对象,由父类负责显示加载中还是加载失败
		return laodingPage;
	}
	
	/**
	 * 加载完成的界面,让子类自己去实现
	 */
	public abstract View getSuccessPage();
	
	/**
	 * 具体请求数据的方法,让子类自己去实现
	 */
	public abstract Object requestData();
	
}
