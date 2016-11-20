package com.annie.googleplay.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.annie.googleplay.R;
import com.annie.googleplay.activity.AppDetailActivity;
import com.annie.googleplay.adapter.HomeAdapter;
import com.annie.googleplay.adapter.HomeHeadAdapter;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.bean.HomeBean;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.UrlUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends BaseFragment implements OnItemClickListener {

	private ViewPager mViewPager;
	List<AppInfo> list = new ArrayList<AppInfo>();
	private HomeAdapter homeAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private ViewPager homeViewpager;
	private ListView listView;
	private HomeBean homeBeanData;
	

	@Override
	public View getSuccessPage() {
		// 1.初始化pullToRefreshListView
		pullToRefreshListView = (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_listview, null);
		// 2.设置刷新模式,BOTH:表示 下拉或上拉都生效
		pullToRefreshListView.setMode(Mode.BOTH); 
		// 3.为pullToRefreshListView 设置监听器
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			/**
			 * @Description:上拉刷新和下拉加载更多都会调用的方法
			 * @param:
			 */
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//如果当前是下拉刷新
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
					//调用loadingpage中的方法加载数据刷新界面
					laodingPage.loadDataAndRefreshPage();
				}else {
					//属于上拉加载更多的情况,也应该调用loadingpage中的方法加载下一页的数据刷新界面
					laodingPage.loadDataAndRefreshPage();
				}
			}
		});
		// 4.获取包含的listview
		listView = pullToRefreshListView.getRefreshableView();
		
		//去掉listview自带的蓝色状态选择器
		listView.setSelector(android.R.color.transparent);
		//去掉liseview自带的分割线divider
		listView.setDividerHeight(0); //代码中只设置高度就可以了
		
		//在listview的头部添加viewpager
		addHeadPage();
		
		// 5.为listview设置adapter
		homeAdapter = new HomeAdapter(list);
		listView.setAdapter(homeAdapter);
		// 6.为listview设置监听事件
		listView.setOnItemClickListener(this);
		
		return pullToRefreshListView;
	}


	/**
	 * @Description: 在listview的头部添加viewpager
	 * @param:
	 */
	private void addHeadPage() {
		// 1.初始化viewpager,填充viewpager 布局
		View headView = View.inflate(getActivity(), R.layout.home_viewpager, null);
		homeViewpager = (ViewPager) headView.findViewById(R.id.home_viewpager);
		
		// 2.将viewpager添加到listview中
		listView.addHeaderView(headView);
	}

	@Override 
	public Object requestData() { 
		
		
		//请求数据之前需要先判断,如果是下拉刷新的话,应该先清空集合中之前的信息
		if (pullToRefreshListView.getCurrentMode() == Mode.PULL_FROM_START) {
			//清空集合
			list.clear(); 
		}
		// UrlUtil.home + list.size(): 重新加载下一页数据;当为上拉加载的时候,继续加载下一页数据
		final HomeBean homeBeanData = DataEngine.getInstance().loadBeanData(UrlUtil.home + list.size(), HomeBean.class);
		
		if (homeBeanData != null) {
			//更新数据
			list.addAll(homeBeanData.getList());
			
			CommonUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					//因为上面添加viewpager头的时候还没有数据,所以在这里设置viewpager的adapter
					//因为viewpager的图片只是在第0页的时候才会加载,所以为了确保加载下几页的时候
					//viewpager中仍有数据,需要先判读一下,确保真正有数据后才设置adapter展示图片
					if (homeBeanData.getPicture() != null && homeBeanData.getPicture().size() > 0) {
						homeViewpager.setAdapter(new HomeHeadAdapter(homeBeanData.getPicture()));
					}
					
					//更新UI,也就是更新adapter,只要数据更新了,就要通知adapter!!!
					homeAdapter.notifyDataSetChanged();
					
					//刷新完之后调用刷新完成的方法
					pullToRefreshListView.onRefreshComplete();
				}
			});
		}
		
		//System.out.println(homeBeanData.toString());
		return list;
		//return new String("aaaaaaaa");  //模拟有数据的情况
	}


	// listview 的条目点击事件
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(),AppDetailActivity.class);
		intent.putExtra("packageName", list.get(position - 2).getPackageName());
		startActivity(intent);
	}
	
}
