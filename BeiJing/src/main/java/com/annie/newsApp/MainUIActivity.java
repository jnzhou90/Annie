package com.annie.newsApp;

import com.annie.newsApp.fragment.ContentFragment;
import com.annie.newsApp.fragment.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.Window;

public class MainUIActivity extends SlidingFragmentActivity {

	private final String LEFT_TAG = "left_tag";
	private final String CONTENT_TAG = "content_tag";
	private FragmentManager fm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 去除标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 设置正文界面
		setContentView(R.layout.activity_main_ui);
		// 设置左侧菜单
		setBehindContentView(R.layout.activity_left_menu);
		// 获取菜单管理器
		SlidingMenu slidingMenu = getSlidingMenu();
		// 设置右侧菜单
		// slidingMenu.setSecondaryMenu(R.layout.activity_right_menu);
		// 设置菜单模式:left:仅左侧 right: 仅右侧 left_right: 左右两侧都可以
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 设置菜单的触摸范围
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置正文界面的宽度
		slidingMenu.setBehindOffset(200);

		// 设置Fragment
		setFragment();
	}

	/**
	 * fTransaction.replace(R.id.mainui_fl_left, fragment, tag) 的原理:
	 * 1.找到R.id.ll_main_content 容器，viewgroup
	 * 2.viewgroup.addView(ContentFragment.onCreateView());
	 */
	/**
	 * @Description: 设置Fragment
	 * @param:
	 */
	private void setFragment() {
		// 创建fragment管理器
		fm = getSupportFragmentManager();
		// 开启服务
		FragmentTransaction fTransaction = fm.beginTransaction();
		// 把fragment对象显示到指定资源id的组件里面; tag: 设置了标签后,就可以通过该标签找到其对应的Fragment对象
		fTransaction.replace(R.id.mainui_fl_content, new ContentFragment(),
				CONTENT_TAG);
		fTransaction.replace(R.id.mainui_fl_left, new LeftFragment(), LEFT_TAG);
		// 提交事务
		fTransaction.commit();

	}

	// 根据标签获取左侧菜单的对象,并返回
	public LeftFragment getLeftFragment() {
		LeftFragment leftFragment = (LeftFragment) fm
				.findFragmentByTag(LEFT_TAG);
		return leftFragment;
	}

	// 根据标签获取正文界面的对象,并返回
	public ContentFragment getContentFragment() {
		ContentFragment contentFragment = (ContentFragment) fm
				.findFragmentByTag(CONTENT_TAG);
		return contentFragment;
	}
}
