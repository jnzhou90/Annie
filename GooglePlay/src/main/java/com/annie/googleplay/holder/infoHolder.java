package com.annie.googleplay.holder;

import com.annie.googleplay.R;
import com.annie.googleplay.activity.AppDetailActivity;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class infoHolder extends BaseHolder<AppInfo> {

	ImageView iv_icon;
	RatingBar rb_star;
	TextView tv_name,tv_download_num,tv_version,tv_date,tv_size;
	
	@Override
	public View initView() {
		View view = View.inflate(GooglePlayApplication.context, R.layout.layout_detail_info,null );
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		rb_star = (RatingBar) view.findViewById(R.id.rb_star);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_download_num = (TextView) view.findViewById(R.id.tv_download_num);
		tv_version = (TextView) view.findViewById(R.id.tv_version);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_size = (TextView) view.findViewById(R.id.tv_size);
		return view;
	}

	@Override
	public void setBindData(AppInfo appDetailInfos) {

		tv_name.setText(appDetailInfos.getName());
		rb_star.setRating(appDetailInfos.getStars());
		tv_download_num.setText("下载："+appDetailInfos.getDownloadNum());
		tv_version.setText("版本："+appDetailInfos.getVersion());
		tv_date.setText("日期："+appDetailInfos.getDate());
		tv_size.setText("大小："+Formatter.formatFileSize(GooglePlayApplication.context, appDetailInfos.getSize()));
		ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD+appDetailInfos.getIconUrl(), iv_icon,ImageloaderOptions.options);
	
	}

}
