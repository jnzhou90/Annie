package com.annie.googleplay.holder;

import java.util.ArrayList;

import com.annie.googleplay.R;
import com.annie.googleplay.activity.ImageScreenActivity;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ScreenHolder extends BaseHolder<AppInfo> {

	private LinearLayout ll_screen;

	@Override
	public View initView() {
		View view = View.inflate(GooglePlayApplication.context, R.layout.layout_detail_screen, null);
		// 1.初始化用来盛放展示图片的imageview的容器
		ll_screen = (LinearLayout) view.findViewById(R.id.ll_screen);
		
		return view;
	}

	@Override
	public void setBindData(AppInfo appInfo) {
		int width = CommonUtil.getDimens(R.dimen.dp90);
		int height = CommonUtil.getDimens(R.dimen .dp150);
		int margin = CommonUtil.getDimens(R.dimen.dp8);
		final ArrayList<String> screenInfos = appInfo.getScreen();
		
		for (int i = 0; i < screenInfos.size(); i++) {
			// 2.创建imagevi,用来显示图片
			ImageView imageView = new ImageView(GooglePlayApplication.context);
			
			// b.设置imageview的宽高属性值(imagview是新创建的所以new LayoutParams,否则getLayoutParams)
			LinearLayout.LayoutParams params = new LayoutParams(width, height);
			params.leftMargin = (i == 0 ) ? 0 : margin;
			// a.设置imageview的宽高
			imageView.setLayoutParams(params);
			// 3.展示图片
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + screenInfos.get(i), imageView, ImageloaderOptions.options);
			// 4.将imageview添加到容器LinearLayout中
			ll_screen.addView(imageView);
			
			//添加点击监听,当点击图片的时候将图片全屏显示
			//实际就是开启一个activity,activity中包含一个viewpager用来展示图片
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(GooglePlayApplication.context,ImageScreenActivity.class);
					intent.putStringArrayListExtra("screenInfos", screenInfos);
					
					//此处因为是在activity的上下文之外开启activity,(说白了就是开启activity所用的上下文不是activity的)
					//所以需要指定任务栈
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					GooglePlayApplication.context.startActivity(intent);
					
				}
			});
		}

	}

}
