package com.annie.newsApp.fragment;

import java.util.ArrayList;
import java.util.List;

import com.annie.newsApp.MainUIActivity;
import com.annie.newsApp.R;
import com.annie.newsApp.base.BaseFragment;
import com.annie.newsApp.base.BasePager;
import com.annie.newsApp.pager.GoveraffairsPager;
import com.annie.newsApp.pager.HomePager;
import com.annie.newsApp.pager.NewsCenterPager;
import com.annie.newsApp.pager.SettingPager;
import com.annie.newsApp.pager.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ContentFragment extends BaseFragment {

	// 使用xutils注解找控件
	@ViewInject(R.id.content_vp_pagers)
	private ViewPager mViewPagers;

	@ViewInject(R.id.content_rgp_bottomButton)
	// radioGroup
	private RadioGroup mBottomButton;

	private List<BasePager> list;

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.content_fragment, null);
		ViewUtils.inject(this, view);
		// 内部实现
		// 1、找到this对象身上的@ViewInject成员变量，取 R.id.vp_content_pagers
		// 2、vp_content_pagers = view.findViewById(R.id.vp_content_pagers);
		// 3.在抽象类里面不能使用ViewUtils注解找控件，因为内部使用的是子类中声明的成员变量getDeclaredFields
		return view;
	}

	// 修改布局,更新控件
	@Override
	public void initData() {
		// 如果不抽取基类的话,需要先填充一个布局给view,进行相应的初始化,将它添加到集合中
		list = new ArrayList<BasePager>();
		list.add(new HomePager(mActivity));
		list.add(new NewsCenterPager(mActivity));
		list.add(new SmartServicePager(mActivity));
		list.add(new GoveraffairsPager(mActivity));
		list.add(new SettingPager(mActivity));

		// 为viewpager设置适配器Adapter
		mViewPagers.setAdapter(new MyAdapter());

		// 为viewpager设置界面滑动监听
		mViewPagers.setOnPageChangeListener(new MyOnPageChangeListener());

		// 初始化,进入主界面的时候自动加载数据
		list.get(0).initData();

		// 设置底部按钮的监听事件,用来切换界面
		mBottomButton
				.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		// 设置让底部单选按钮默认选中首页
		mBottomButton.check(R.id.content_rb_home);
	}

	// 设置底部按钮的监听事件,用来切换界面
	private class MyOnCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.content_rb_home:
				// 注: 设置viewpager当前显示的界面，参数：显示的界面的位置,必须显示数据之后，才能设置当前显示的位置
				// 设置当前显示的条目,false表示取消动画效果
				mViewPagers.setCurrentItem(0, false);
				enableSlidingMenu(false);
				break;
			case R.id.content_rb_newsCenter:
				mViewPagers.setCurrentItem(1, false);
				enableSlidingMenu(true);
				break;
			case R.id.content_rb_smartService:
				mViewPagers.setCurrentItem(2, false);
				enableSlidingMenu(true);
				break;
			case R.id.content_rb_goveraffirs:
				mViewPagers.setCurrentItem(3, false);
				enableSlidingMenu(true);
				break;
			case R.id.content_rb_setting:
				mViewPagers.setCurrentItem(4, false);
				enableSlidingMenu(false);
				break;

			default:
				break;
			}
		}

	}

	/**
	 * @Description:根据底部单选按钮的位置,判断是否禁用左侧菜单
	 * @param:
	 */
	public void enableSlidingMenu(boolean enable) {
		MainUIActivity mainUIActivity = (MainUIActivity) getActivity();
		if (enable) {
			SlidingMenu slidingMenu = mainUIActivity.getSlidingMenu();
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			SlidingMenu slidingMenu = mainUIActivity.getSlidingMenu();
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	// 为viewpager设置界面滑动监听
	private class MyOnPageChangeListener implements OnPageChangeListener {

		// 当界面切换的时候调用,positionOffset:移动比例; positionOffsetPixels:移动的像素
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		// 界面切换完成时调用
		@Override
		public void onPageSelected(int position) {
			// 当界面切换完成的时候加载数据
			BasePager basePager = list.get(position);
			basePager.initData();
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}

	}

	// 为viewpager设置适配器
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 根据位置获取相对应的Pager对象
			BasePager basePager = list.get(position);

			/**
			 * 获取Pager对象的布局,但这样的话因为viewpager的预加载,
			 * 每次显示该布局之前都要重新调用inITview方法创建该对象的布局,浪费资源,
			 * 因此可以将该布局在第一次创建的时候就绑定到该对象身上,之后直接拿过来用,不再调用inITview方法 View view =
			 * basePager.initView();
			 */
			// 获取Pager对象的布局
			View view = basePager.rootView;
			// 将view对象添加到viewpager中展示
			container.addView(view);

			/**
			 * 不能在这个地方更新Pager对象的布局,加载数据，因为viewpager有预加载功能,
			 * 所以会提前加载下一个要显示的界面,这样做会在用户不知情的情况下浪费用户流量,所以应该为viewpager设置界面滑动监听,
			 * 当界面切换完成的时候加载数据,屏蔽预加载数据 basePager.initData();
			 */
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}
	}

	// 获取并返回新闻中心界面对象的方法
	public NewsCenterPager getNewsCenterPager() {
		NewsCenterPager newsCenterPager = (NewsCenterPager) list.get(1);
		return newsCenterPager;
	}

}
