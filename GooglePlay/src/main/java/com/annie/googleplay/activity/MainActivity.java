package com.annie.googleplay.activity;

import java.util.ArrayList;
import java.util.List;
import com.annie.googleplay.R;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.annie.googleplay.adapter.MyViewPagerAdapter;
import com.annie.googleplay.bean.PageInfo;
import com.annie.googleplay.ui.fragment.ApplicationFragment;
import com.annie.googleplay.ui.fragment.CategoryFragment;
import com.annie.googleplay.ui.fragment.GameFragment;
import com.annie.googleplay.ui.fragment.HomeFragment;
import com.annie.googleplay.ui.fragment.HotFragment;
import com.annie.googleplay.ui.fragment.RecommendFragment;
import com.annie.googleplay.ui.fragment.SubjectFragment;
import com.annie.googleplay.ui.view.PagerSlidingTab;
import com.annie.googleplay.util.CommonUtil;

public class MainActivity extends ActionBarActivity {

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ViewPager mViewPager;
	private List<PageInfo> list;
	private PagerSlidingTab mpPagerSlidingTab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//初始化控件
		initView();
		//加载数据
		fillData();
		
		//设置setActionBar
		setActionBar(); 
		
		//绑定ViewPager和PagerSlidingTab(必须在在adapter中重写getPageTitle()方法)
		mpPagerSlidingTab.setViewPager(mViewPager);
		
	}

	/**
	 * @Description: 初始化控件
	 * @param:
	 */
	private void initView() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_main);
		mViewPager = (ViewPager) findViewById(R.id.main_vp_viewpager);
		mpPagerSlidingTab = (PagerSlidingTab) findViewById(R.id.main_pst_pagerslidlingtab);
		
		
	}

	/**
	 * @Description: 设置setActionBar
	 * @param:
	 */
	private void setActionBar() {
		//1.获取ActionBar对象，进行设置
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setTitle(getResources().getString(R.string.app_name));
		
		//2.设置ActionBar的home按钮可用
		actionBar.setDisplayShowHomeEnabled(true); //设置home按钮可用
		actionBar.setDisplayHomeAsUpEnabled(true); //设置home按钮可以被点击
		
		//3.设置显示汉堡包按钮，就是3条线
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer_am, 0, 0);
		drawerToggle.syncState(); //同步actionbar和drawerLayout的状态
		
		//4.添加DrawerLayout的滑动监听，让ActionBarDrawerToggle进行滑动动画
		//翻看源码得知ActionBarDrawerToggle已经实现了DrawerListener监听
		drawerLayout.setDrawerListener(drawerToggle);
	}
	
	/**
	 * @Description: 加载要展示的页面数据
	 * @param:
	 */
	private void fillData() {
		//创建集合用于存储要展示的界面
		list = new ArrayList<PageInfo>();
		//获取字符串数据
		String[] str = CommonUtil.getStrings(R.array.tab_names);
		System.out.println(str);
		//将要展示的界面添加到集合中去
		list.add(new PageInfo(str[0], new HomeFragment()));
		list.add(new PageInfo(str[1], new ApplicationFragment()));
		list.add(new PageInfo(str[2], new GameFragment()));
		list.add(new PageInfo(str[3], new SubjectFragment()));
		list.add(new PageInfo(str[4], new RecommendFragment()));
		list.add(new PageInfo(str[5], new CategoryFragment()));
		list.add(new PageInfo(str[6], new HotFragment()));
		
		//加载完数据,设置适配器显示数据
		mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), list));
	}

	//当点击ActionBar的home按钮时会调用此方法,当点击菜单条目的时候同样也会调用该方法
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//说明点击的是ActionBar的home按钮
		case android.R.id.home:
			/*if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
				drawerLayout.closeDrawer(Gravity.LEFT);
			}else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}*/
			//相当于上面的代码,也可以控制侧滑菜单的开关
			drawerToggle.onOptionsItemSelected(item);
			break;
		case R.id.menu_annie:
			Toast.makeText(this, "Annie菜单点击事件", 0).show();
			break;
		case R.id.menu_free:
			Toast.makeText(this, "Free菜单点击事件", 0).show();
			break;
		case R.id.menu_settings:
			Toast.makeText(this, "ettings菜单点击事件", 0).show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//加载菜单的布局
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
