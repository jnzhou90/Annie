package com.annie.googleplay.holder;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class HomeHolder extends BaseHolder<AppInfo> {
	TextView appName, appSize, appDes;
	ImageView appIcon;
	RatingBar star;

	/**
	 * 初始化holderview
	 */
	@Override
	public View initView() {
		View view = View.inflate(GooglePlayApplication.context,R.layout.homefragment_item, null);

		appDes = (TextView) view.findViewById(R.id.homefragment_tv_des);
		appIcon = (ImageView) view.findViewById(R.id.homefragment_iv_icon);
		appName = (TextView) view.findViewById(R.id.homefragment_tv_name);
		appSize = (TextView) view.findViewById(R.id.homefragment_tv_size);
		star = (RatingBar) view.findViewById(R.id.homefragment_rg_star);

		return view;
	}
	
	/**
	 * 绑定数据的操作 
	 */
	@Override
	public void setBindData(AppInfo appInfo) {
		appDes.setText(appInfo.getDes());
		appName.setText(appInfo.getName());
		appSize.setText(Formatter.formatFileSize(GooglePlayApplication.context, appInfo.getSize()));
		star.setRating(appInfo.getStars());

		//使用ImageLoader,展示图片
		ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + appInfo.getIconUrl(), appIcon, ImageloaderOptions.options);
	}
}
