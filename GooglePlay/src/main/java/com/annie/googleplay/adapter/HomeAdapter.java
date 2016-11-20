package com.annie.googleplay.adapter;

import java.util.List;

import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.holder.BaseHolder;
import com.annie.googleplay.holder.HomeHolder;

public class HomeAdapter extends BasicAdapter<AppInfo> {

	//将集合传给父类
	public HomeAdapter(List<AppInfo> list) {
		super(list);
	}

	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// 1.创建holder对象
			holder = new ViewHolder();
		} else {
			// 从convertview中取出holder
			holder = (ViewHolder) convertView.getTag();
		}

		// 绑定数据
		AppInfo appInfo = list.get(position);
		holder.setBindData(appInfo);

		return holder.getHolderView();
	}*/

	

	//获取holder
	@Override
	protected BaseHolder<AppInfo> getHolder(int position) {
		return new HomeHolder();
	}
}
