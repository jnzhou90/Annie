package com.annie.googleplay.activity;

import com.annie.googleplay.R;
import com.annie.googleplay.R.layout;
import com.annie.googleplay.R.menu;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.holder.AppDesHolder;
import com.annie.googleplay.holder.DownloadHolder;
import com.annie.googleplay.holder.SafeHolder;
import com.annie.googleplay.holder.ScreenHolder;
import com.annie.googleplay.holder.infoHolder;
import com.annie.googleplay.ui.fragment.LaodingPage;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.attr;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class AppDetailActivity extends ActionBarActivity {

	private String packageName;
	
	private AppInfo appDetailInfos;
	//private LaodingPage laodingPage;
	private infoHolder appInfoHolder;
	private LinearLayout holder_container;
	private SafeHolder safeHolder;
	private ScreenHolder screenHolder;
	private AppDesHolder appDesHolder;
	private ScrollView scrollView;
	private FrameLayout download_container;
	private DownloadHolder downloadHolder;
	
	ImageView iv_icon;
	RatingBar rb_star;
	TextView tv_name,tv_download_num,tv_version,tv_date,tv_size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 1.获取传递的值
		packageName = getIntent().getStringExtra("packageName");
		// 2.设置actionba
		setActionBar();

		// 3.将loadingpage作为activity的view对象
		LaodingPage laodingPage = new LaodingPage(this) {
			@Override
			protected Object loadData() {
				return requestData();
			}
			

			@Override
			protected View createSuccessPage() {
				View view = View.inflate(AppDetailActivity.this, R.layout.activity_app_detail,null );
				//作为存放holder的容器
				holder_container = (LinearLayout) view .findViewById(R.id.holder_container);
				//初始化scrollview控件
				scrollView = (ScrollView) view.findViewById(R.id.scrollview);
				download_container = (FrameLayout) view.findViewById(R.id.download_container);	
				
				// 1.添加appinfo模块
				appInfoHolder = new infoHolder();
				//将holder中初始化的view添加到容器中
				holder_container.addView(appInfoHolder.getHolderView());
				
				// 2.添加安全信息模块
				safeHolder = new SafeHolder();
				holder_container.addView(safeHolder.getHolderView());
				
				// 3.添加screen模块
				screenHolder = new ScreenHolder();
				holder_container.addView(screenHolder.getHolderView());
				
				// 4.添加app描述信息模块
				appDesHolder = new AppDesHolder();
				holder_container.addView(appDesHolder.getHolderView());
				//将scrollview对象设置给DesHolder
				appDesHolder.setScrollView(scrollView);
				
				// 5.download模块
				downloadHolder = new DownloadHolder();
				download_container.addView(downloadHolder.getHolderView());
				
				return view;
			}
		};
		setContentView(laodingPage);
		
		
	}

	/**
	 * 请求数据
	 */
	private Object requestData() {

		appDetailInfos = DataEngine.getInstance().loadBeanData(UrlUtil.detail + packageName, AppInfo.class);
		// 更新UI
		CommonUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 1.设置info模块的数据
				appInfoHolder.setBindData(appDetailInfos);
				
				// 2.设置safe模块的数据
				safeHolder.setBindData(appDetailInfos);
				
				// 3.设置Screen模块的数据
				screenHolder.setBindData(appDetailInfos);
				
				// 4.设置app描述信息的数据
				appDesHolder.setBindData(appDetailInfos);
				
				// 5.设置download数据
				downloadHolder.setBindData(appDetailInfos);
				
			}
		});

		return appDetailInfos;
	}
	
	/**
	 * @Description: 设置actionba
	 * @param:
	 */
	private void setActionBar() {
		//创建actionbar对象
		ActionBar actionBar = getSupportActionBar();
		//设置actionbar的标题
		actionBar.setTitle(getString(R.string.app_detail));
		
		//设置actionbar的home按钮可以被点击
		actionBar.setDisplayHomeAsUpEnabled(true); //设置home按钮可以被点击
		actionBar.setDisplayShowHomeEnabled(true); //设置home按钮可用
	}
	 
	/**
	 * 当点击actionbar的home按钮或菜单时此方法会被调用
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//当点击actionbar的home按钮时,销毁当前activity
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	//当activity销毁的时候,移除监听器
	@Override
	protected void onDestroy() {
		super.onDestroy();
		downloadHolder.unregisterDownloadObserver();
	}
}


