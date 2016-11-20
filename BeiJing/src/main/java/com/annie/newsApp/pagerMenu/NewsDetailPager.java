package com.annie.newsApp.pagerMenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annie.newsApp.MainUIActivity;
import com.annie.newsApp.R;
import com.annie.newsApp.base.MenuBasePager;
import com.annie.newsApp.been.NewsCenterBeen.newsTab;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

public class NewsDetailPager extends MenuBasePager {

	private List<newsTab> mData; //新闻页签对应的数据
	private TabPageIndicator tIndicator; //ViewPager的指示器
	private ViewPager viewPager;
	private List<MenuBasePager> list;

	public NewsDetailPager(Context context, List<newsTab> children) {
		super(context);
		this.mData = children;
	}

	@Override
	public View initView() {
		// 加载布局
		View view = View.inflate(mContext, R.layout.newsdetails, null);
		//初始化控件
		tIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		viewPager = (ViewPager) view.findViewById(R.id.pager);
		

		return view;
	}

	@Override
	public void initData() {
		//将viewpager中要展示的布局添加到集合中
		list = new ArrayList<MenuBasePager>();
		for (int i = 0; i < mData.size(); i++) {
			list.add(new TabDetailPaper(mContext,mData.get(i)));
		}
		
		// 设置适配器
		viewPager.setAdapter(new MyAdapter());

		// 将Indicato 和viewpager关联在一起,之所以可以关联,实现同步滑动是因为Indicato设置了viewpager的界面滑动监听
		tIndicator.setViewPager(viewPager);
		
		//设置viewpager的界面滑动监听,只有当显示北京也就是第一个新闻页签的时候才可以让左侧菜单处理事件!!!
		//这样写会覆盖掉Indicato中的viewpager的界面滑动监听,导致不能实现同步滑动,必须把OnPageChangeListener设置给Indicator
		//viewPager.setOnPageChangeListener(new MyOnPageChangeListener());  
		tIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	//为viewpager设置届满滑动监听,只有当显示北京也就是第一个新闻页签的时候才可以让左侧菜单处理事件!!!
	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			MainUIActivity mainUIActivity = (MainUIActivity) mContext;
			//获取菜单管理器
			SlidingMenu slidingMenu = mainUIActivity.getSlidingMenu();
			if (position == 0) {
				//通过设置左侧菜单的触摸范围,来使左侧菜单生效
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			}else {
				//通过设置左侧菜单的触摸范围,来使左侧菜单失效
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//为viewpager设置适配器
	private class MyAdapter extends PagerAdapter {
		
		//ViewPager的指示器调用的方法,
		@Override
		public CharSequence getPageTitle(int position) {
			return mData.get(position).title;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//根据位置获取pager对象
			MenuBasePager menuBasePager = list.get(position);
			//获取pager对象的布局
			View view = menuBasePager.rootView;
			//将该布局天降到viewpager中展示
			container.addView(view);
			//加载数据
			menuBasePager.initData();
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);
			container.removeView((View) object);
		} 
		
		
		
	}
}
