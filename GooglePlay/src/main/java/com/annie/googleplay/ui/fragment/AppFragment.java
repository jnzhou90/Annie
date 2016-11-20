//package com.annie.googleplay.ui.fragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.annie.googleplay.R;
//import com.annie.googleplay.adapter.HomeAdapter;
//import com.annie.googleplay.adapter.HomeHeadAdapter;
//import com.annie.googleplay.api.DataEngine;
//import com.annie.googleplay.bean.AppInfo;
//import com.annie.googleplay.bean.HomeBean;
//import com.annie.googleplay.util.CommonUtil;
//import com.annie.googleplay.util.UrlUtil;
//import com.google.gson.reflect.TypeToken;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class AppFragment extends BaseFragment {
//
//	List<AppInfo> list = new ArrayList<AppInfo>();
//	private HomeAdapter homeAdapter;
//	private PullToRefreshListView pullToRefreshListView;
//	private ViewPager homeViewpager;
//	private ListView listView;
//	
//	@Override
//	public View getSuccessPage() {
//		// 1.初始化pullToRefreshListView
//				pullToRefreshListView = (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_listview, null);
//				// 2.设置刷新模式,BOTH:表示 下拉或上拉都生效
//				pullToRefreshListView.setMode(Mode.BOTH); 
//				// 3.为pullToRefreshListView 设置监听器
//				pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//					/**
//					 * @Description:上拉刷新和下拉加载更多都会调用的方法
//					 * @param:
//					 */
//					@Override
//					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//						//如果当前是下拉刷新
//						if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
//							//调用loadingpage中的方法加载数据刷新界面
//							laodingPage.loadDataAndRefreshPage();
//						}else {
//							//属于上拉加载更多的情况,也应该调用loadingpage中的方法加载下一页的数据刷新界面
//							laodingPage.loadDataAndRefreshPage();
//						}
//					}
//				});
//				// 4.获取包含的listview
//				listView = pullToRefreshListView.getRefreshableView();
//				
//				//去掉listview自带的蓝色状态选择器
//				listView.setSelector(android.R.color.transparent);
//				//去掉liseview自带的分割线divider
//				listView.setDividerHeight(0); //代码中只设置高度就可以了
//				//为listview设置adapter
//				homeAdapter = new HomeAdapter(list);
//				listView.setAdapter(homeAdapter);
//				return pullToRefreshListView;
//	}
//
//	@Override
//	public Object requestData() {
//		//请求数据之前需要先判断,如果是下拉刷新的话,应该先清空集合中之前的信息
//				if (pullToRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
//					//清空集合
//					list.clear(); 
//				}
//				// UrlUtil.home + list.size(): 重新加载下一页数据;当为上拉加载的时候,继续加载下一页数据
//				List<AppInfo> appInfos = (List<AppInfo>) DataEngine.getInstance().loadListData(UrlUtil.app + list.size(), new TypeToken<List<AppInfo>>(){}.getType());
//				if (appInfos != null) {
//					//更新数据
//					list.addAll(appInfos);
//					
//					CommonUtil.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							
//							
//							//更新UI,也就是更新adapter,只要数据更新了,就要通知adapter!!!
//							homeAdapter.notifyDataSetChanged();
//							
//							//刷新完之后调用刷新完成的方法
//							pullToRefreshListView.onRefreshComplete();
//						}
//					});
//				}
//				
//				//System.out.println(homeBeanData.toString());
//				return list;
//	}
//}
