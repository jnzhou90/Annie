package com.annie.googleplay.adapter;

import java.util.List;

import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HomeHeadAdapter extends PagerAdapter {
	
	//通过构造函数获取list
	List<String> list;
	public HomeHeadAdapter(List<String> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 创建imageview对象作为展示图片的容器
		ImageView imageView = new ImageView(GooglePlayApplication.context);
		
		// 使用ImageLoader,展示图片
		ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + list.get(position), imageView, ImageloaderOptions.options);
		
		//将imageview添加到viewpager中
		container.addView(imageView);
		return imageView;
	}

	

}
