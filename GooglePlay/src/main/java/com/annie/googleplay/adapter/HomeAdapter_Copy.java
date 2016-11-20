//package com.annie.googleplay.adapter;
//
//import java.util.List;
//
//import android.text.format.Formatter;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import com.annie.googleplay.R;
//import com.annie.googleplay.bean.AppInfo;
//import com.annie.googleplay.global.GooglePlayApplication;
//
//public class HomeAdapter_Copy extends BasicAdapter<AppInfo> {
//
//	public HomeAdapter_Copy(List<AppInfo> list) {
//		super(list);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder;
//		if (convertView == null) {
//			convertView = View.inflate(GooglePlayApplication.context, R.layout.homefragment_item, null);
//			// 1.创建holder对象
//			holder = new ViewHolder();
//			// 2.初始化控件
//			holder.appDes = (TextView) convertView.findViewById(R.id.homefragment_tv_des);
//			holder.appIcon = (ImageView) convertView.findViewById(R.id.homefragment_iv_icon);
//			holder.appName = (TextView) convertView.findViewById(R.id.homefragment_tv_name);
//			holder.appSize = (TextView) convertView.findViewById(R.id.homefragment_tv_size);
//			holder.star = (RatingBar) convertView.findViewById(R.id.homefragment_rg_star);
//			// 3.设置Tag
//			convertView.setTag(holder);
//		}else {
//			// 从convertview中取出holder
//			holder = (ViewHolder) convertView.getTag();
//		}
//		
//		//绑定数据
//		AppInfo appInfo = list.get(position);
//		holder.appDes.setText(appInfo.des);
//		holder.appName.setText(appInfo.name);
//		holder.appSize.setText(Formatter.formatFileSize(GooglePlayApplication.context, appInfo.size));
//		holder.star.setRating(appInfo.stars);
//		return convertView;
//	}
//	
//	class ViewHolder{
//		TextView appName,appSize,appDes;
//		ImageView appIcon;
//		RatingBar star;
//	}
//}
