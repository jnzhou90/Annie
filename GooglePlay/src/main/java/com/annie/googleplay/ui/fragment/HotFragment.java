package com.annie.googleplay.ui.fragment;

import java.lang.reflect.Type;
import java.util.List;

import com.annie.googleplay.R;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.ui.view.FlowLayout;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.DrawableUtil;
import com.annie.googleplay.util.RandomColorUtil;
import com.annie.googleplay.util.ToastUtil;
import com.annie.googleplay.util.UrlUtil;
import com.google.gson.reflect.TypeToken;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class HotFragment extends BaseFragment {

	private List<String> hotInfos;
	private FlowLayout flowLayout;
	private ScrollView scrollView;
	private int dp12;
	private int dp6;
	
	//定义一个变量用于标识requestData方法第几次被调用,背面重复调用requestData
	private int times;
	private int padding;

	@Override
	public View getSuccessPage() {
		//定义一个变量用于标识requestData方法第几次被调用,背面重复调用requestData
		times = 0;
		
		//获取dimen.xml中的数据
		dp12 = CommonUtil.getDimens(R.dimen.dp12);
		dp6 = CommonUtil.getDimens(R.dimen.dp6);
		
		scrollView = new ScrollView(getActivity());
		flowLayout = new FlowLayout(getActivity());
		
		padding = CommonUtil.getDimens(R.dimen.dp15);
		flowLayout.setPadding(padding, padding, padding, padding);
		
		
		//将自定义的流式布局添加到scrollView,使其可以上下滑动
		scrollView.addView(flowLayout);
		/*TextView textView = new TextView(getActivity());
		textView.setText("aa");
		return textView;*/
		return scrollView;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object requestData() {
		//标识自增一
		times++;
		//if (times >= 2) {
			//清空流式布局中的所有子view,避免数据被重复加载,确保数据的正确唯一!!!
			//flowLayout.removeAllViews();
			
			//从网络获取热门界面的数据
			//Type type = new TypeToken<List<String>>(){}.getType();
			hotInfos = ((List<String>) DataEngine.getInstance().loadListData(UrlUtil.hot, new TypeToken<List<String>>(){}.getType()));
			if (hotInfos != null) {
				CommonUtil.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (times >= 2) {
							//flowLayout.removeAllViews();
							flowLayout = new FlowLayout(getActivity());
							flowLayout.setPadding(padding, padding, padding, padding);
							scrollView.removeAllViews();
							scrollView.addView(flowLayout);
						}
						
						
						//遍历集合,给FlowLayout添加子view
						for(int i= 0; i<hotInfos.size();i++) {
							final TextView textView = new TextView(getActivity());
							textView.setTextSize(16);
							textView.setText(hotInfos.get(i));
							
							//设置内容居中
							textView.setGravity(Gravity.CENTER);
							
							//设置字体内边距
							textView.setPadding(dp12, dp6, dp12, dp6);
							
							//设置字体颜色
							textView.setTextColor(Color.WHITE);
							
							//为view设置状态选择器
							// 动态生成所需要的图片
							Drawable pressed = DrawableUtil.generateDrawable(dp12);
							Drawable normal = DrawableUtil.generateDrawable(dp12);
							textView.setBackgroundDrawable(DrawableUtil.generateSelector(pressed, normal));
							
							//设置显示的随机背景颜色
							//textView.setBackgroundColor(RandomColorUtil.randomColor());
							flowLayout.addView(textView,i);
							
							//设置点击事件
							textView.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ToastUtil.toastShow(textView.getText().toString());
								}
							});
						}
					}
				});
			}
		//}
		
		return hotInfos;
	} 
}
