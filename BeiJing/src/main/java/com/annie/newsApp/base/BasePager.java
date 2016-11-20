package com.annie.newsApp.base;

import com.annie.newsApp.MainUIActivity;
import com.annie.newsApp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BasePager implements OnClickListener {

	protected Context mContext; // 提供给子类使用的上下文对象
	public View rootView; // 保存自己身上的布局
	public TextView mTitle;
	public ImageButton mMenuIcon;
	public FrameLayout mContainer;
	public  ImageButton mType;

	public BasePager(Context context) {
		this.mContext = context;
		rootView = initView();
	}

	/**
	 * @Description: 让子类实现，返回具体的子类布局
	 * @param:
	 */
	public View initView() {
		View view = View.inflate(mContext, R.layout.basepager, null);
		mTitle = (TextView) view.findViewById(R.id.basepager_tv_title);
		mMenuIcon = (ImageButton) view.findViewById(R.id.basepager_ib_menuIcon);
		mContainer = (FrameLayout) view.findViewById(R.id.basepager_fl_container);
		mType = (ImageButton) view.findViewById(R.id.basepager_ib_type);

		// 设置打开左侧菜单的开关监听
		mMenuIcon.setOnClickListener(this);
		return view;
	}

	/**
	 * @Description: 让子类更新布局，不必须实现
	 * @param:
	 */
	public void initData() {
	}

	// 是否开启左侧菜单
	@Override
	public void onClick(View v) {
		MainUIActivity mainUIActivity = (MainUIActivity) mContext;
		// 获取菜单管理器
		SlidingMenu slidingMenu = mainUIActivity.getSlidingMenu();
		// 设置左侧菜单是否开启
		slidingMenu.toggle();
	}
}
