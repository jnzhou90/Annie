package com.annie.googleplay.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.annie.googleplay.R;
import com.annie.googleplay.adapter.HomeAdapter;
import com.annie.googleplay.api.CacheEngine;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.UrlUtil;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @Description: 应用界面和首页类似,可复用首页的adapter
 * @author Free
 * @date 2016-6-30 下午4:27:46
 */
public class ApplicationFragment extends BaseFragment {

	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	List<AppInfo> list = new ArrayList<AppInfo>();
	private HomeAdapter appAdapter;
 
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
		appAdapter = new HomeAdapter(list);
		listView.setAdapter(appAdapter);
		
		return pullToRefreshListView;
	}

	@Override
	public Object requestData() {
		//如果当前是下拉刷新,需要先清空集合中原先的数据
		if (pullToRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
			list.clear();
		}
		
		//获取要展示的集合数据
		//new TypeToken<List<yourbean>>(){}.getType()
		Type type = new TypeToken<List<AppInfo>>(){}.getType();
		List<AppInfo> appInfos = (List<AppInfo>) DataEngine.getInstance().loadListData(UrlUtil.app + list.size(), type);
		
		if (appInfos != null) {
			// 将数据添加到集合中
			list.addAll(appInfos);

			// 集合集合数据更新了,需要通知适配器adapter更新数据
			CommonUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// 更新界面
					appAdapter.notifyDataSetChanged();

					// 上拉,下拉结束后需要调用刷新完成的方法
					pullToRefreshListView.onRefreshComplete();
				}
			});
		}
		return list;
		
		 
		
		/*// 如果是下拉刷新，则应该先清空集合中的数据
				if (pullToRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
					list.clear();// 清空集合中的数据
				}

				ArrayList<AppInfo> appInfos = (ArrayList<AppInfo>) DataEngine.getInstance().loadListData(
						UrlUtil.app + list.size(),new TypeToken<List<AppInfo>>(){}.getType());
				if (appInfos != null) {
					// 更新数据
					list.addAll(appInfos);
					CommonUtil.runOnUiThread(new Runnable() { 
						@Override 
						public void run() {
							// 更新UI，就是更新adapter
							appAdapter.notifyDataSetChanged();

							// 让refreshListView完成刷新操作
							pullToRefreshListView.onRefreshComplete();
						}
					});
 
				}

				return null;*/
		
		//return null;
	}

}
