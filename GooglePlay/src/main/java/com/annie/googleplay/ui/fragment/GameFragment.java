package com.annie.googleplay.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.annie.googleplay.R;
import com.annie.googleplay.adapter.HomeAdapter;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.UrlUtil;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class GameFragment extends BaseFragment {

	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	List<AppInfo> list = new ArrayList<AppInfo>();
	private HomeAdapter gameAdapter;
 
	@Override
	public View getSuccessPage() {
		// 1初始化pullToRefreshListView,即填充pullToRefreshListView布局
		pullToRefreshListView = (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_listview, null);
		// 2.设置刷新模式
		pullToRefreshListView.setMode(Mode.BOTH);
		// 3.为pullToRefreshListView设置监听
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (pullToRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
					laodingPage.loadDataAndRefreshPage();
				}else {
					laodingPage.loadDataAndRefreshPage();
				}
			}
		});
		
		// 4.获取包含的listview
		listView = pullToRefreshListView.getRefreshableView();
		//去掉listview自带的蓝色状态选择器
		listView.setSelector(android.R.color.transparent);
		//去掉liseview自带的分割线divider
		listView.setDividerHeight(0);
		
		// 5.为listview设置适配器
		gameAdapter = new HomeAdapter(list);
		listView.setAdapter(gameAdapter);
		
		return pullToRefreshListView;
	}

	@Override
	public Object requestData() {
		//如果是下拉刷新的话需要先清空集合中原先的数据
		if (pullToRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
			list.clear();
		}
		
		//获取要展示的集合数据
		Type type = new TypeToken<List<AppInfo>>(){}.getType();
		List<AppInfo> gameInfos = (List<AppInfo>) DataEngine.getInstance().loadListData(UrlUtil.game + list.size(), type);
		
		//将数据添加到集合中展示
		if (gameInfos != null) {
			list.addAll(gameInfos);
		}
		
		//集合数据更新了需要通知适配器更新数据
		CommonUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//更新数据
				gameAdapter.notifyDataSetChanged();
				
				//下拉,上拉完之后,需要调用刷新完成的方法
				pullToRefreshListView.onRefreshComplete();
			}
		});
		return list;
		//return null;
	}
}
