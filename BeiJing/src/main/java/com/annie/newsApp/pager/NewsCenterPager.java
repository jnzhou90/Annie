package com.annie.newsApp.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.annie.newsApp.MainUIActivity;
import com.annie.newsApp.base.BasePager;
import com.annie.newsApp.base.MenuBasePager;
import com.annie.newsApp.been.NewsCenterBeen;
import com.annie.newsApp.fragment.LeftFragment;
import com.annie.newsApp.pagerMenu.InterractDetailPager;
import com.annie.newsApp.pagerMenu.NewsDetailPager;
import com.annie.newsApp.pagerMenu.PhotoDetailPager;
import com.annie.newsApp.pagerMenu.TopicDetailPager;
import com.annie.newsApp.util.Constant;
import com.annie.newsApp.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	private NewsCenterBeen newsCenterBeen;
	private List<MenuBasePager> list;

	public NewsCenterPager(Context context) {
		super(context);
	}

	@Override
	public void initData() {
		// System.out.println("新闻中心加载数据");
		mTitle.setText("新闻中心");
		
		// 访问网络之前先展示缓存数据
		String cacheJson = SharedPreferencesUtil.getString(mContext, Constant.NEWSCENTERPAGER_JSON,"");
		if (!TextUtils.isEmpty(cacheJson)) {
			// 有缓存数据，展示数据
			parseJson(cacheJson);
		}
		// 请求网络获取新闻 数据,有没有缓存都会请求网络,只是有缓存的时候会先展示缓存数据,网络请求如果成功的话
		//会立即覆盖掉原先的缓存数据
		getDataFromServer();

	}

	/**
	 * @Description:请求网络获取新闻 数据
	 * @param:
	 */
	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		// 参数1 访问网络的方式 get 异步方法; T 代表的是访问网络回来的数据类型
		httpUtils.send(HttpMethod.GET, Constant.NEWSCENTER_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("请求成功" + responseInfo.result);
						//当数据请求成功的时候,将返回的数据缓存到本地
						SharedPreferencesUtil.saveString(mContext, Constant.NEWSCENTERPAGER_JSON, responseInfo.result);
						// 解析数据
						parseJson(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("请求失败");
					}

				});
	}

	/**
	 * @Description: 使用Gson解析数据
	 * @param:
	 */
	protected void parseJson(String result) {
		// 创建gson对象
		Gson gson = new Gson();
		newsCenterBeen = gson.fromJson(result, NewsCenterBeen.class);
		System.out.println(newsCenterBeen.data.get(0).children.get(1).title);

		// 将解析后得到的新闻页签传给左侧菜单!!!
		MainUIActivity mActivity = (MainUIActivity) mContext;
		LeftFragment leftFragment = mActivity.getLeftFragment();

		// 初始化左侧菜单对应的4个对象
		list = new ArrayList<MenuBasePager>();
		list.add(new NewsDetailPager(mContext,newsCenterBeen.data.get(0).children));
		list.add(new TopicDetailPager(mContext));
		list.add(new PhotoDetailPager(mContext));
		list.add(new InterractDetailPager(mContext));

		leftFragment.getNewsTag(newsCenterBeen.data);

		// 设置新闻中心默认显示新闻详情
		switchPager(0);
	}

	/**
	 * @Description: 接收左侧菜单传回的数据
	 * @param:
	 */
	public void switchPager(int position) {
		mTitle.setText(newsCenterBeen.data.get(position).title);//显示标题

		// 根据位置获取相对应的Pager对象
		MenuBasePager menuBasePager = list.get(position);

		// 添加之前先把之前的布局删掉
		mContainer.removeAllViews();

		// 获取Pager对象的布局
		View view = menuBasePager.rootView;
		// 将view对象添加到viewpager中展示
		mContainer.addView(view);
		// 加载数据
		menuBasePager.initData();
		
		//根据显示的界面判断标题栏显示的内容
		if (position == 2) {
			mType.setVisibility(View.VISIBLE);
			
			//获取组图界面对象
			final PhotoDetailPager photoDetailPager = (PhotoDetailPager) list.get(2);
			//当点击的时候通过组图界面对象,调用组图界面中切换显示类型的方法
			mType.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					photoDetailPager.switchType(mType);
				}
			});
		}else {
			mType.setVisibility(View.GONE);
		}
	}

}
