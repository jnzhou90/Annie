package com.annie.googleplay.ui.fragment;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import com.annie.googleplay.R;
import com.annie.googleplay.api.DataEngine;
import com.annie.googleplay.ui.view.randomlayout.StellarMap;
import com.annie.googleplay.ui.view.randomlayout.StellarMap.Adapter;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.RandomColorUtil;
import com.annie.googleplay.util.ToastUtil;
import com.annie.googleplay.util.UrlUtil;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendFragment extends BaseFragment {

	private StellarMap stellarMap;
	private List<String> list;

	@Override
	public View getSuccessPage() {
		
		stellarMap = new StellarMap(getActivity());
		
		//设置随机内容距离边框的距离,这里要注意屏幕适配,需要用到dimens!!!!
		//例如1920X1080的手机用dp无法完成适配
		int padding = CommonUtil.getDimens(R.dimen.dp15);
		stellarMap.setInnerPadding(padding, padding, padding, padding);
		return stellarMap;
	}

	@Override
	public Object requestData() {
		
		//获取要展示的数据
		Type type = new TypeToken<List<String>>(){}.getType();
		list = (List<String>) DataEngine.getInstance().loadListData(UrlUtil.recommend, type);
		if (list != null) {
			//更新UI
			CommonUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//给stellarMap设置数据
					stellarMap.setAdapter(new StellarMapAdapter());
					
					//默认不显示数据,设置默认显示第0 组的数据
					stellarMap.setGroup(0, true);
					
					//设置每页显示的数据密度或规则，参数设置不要过大也不要不小,略大于每组的个数就好
					stellarMap.setRegularity(3, 4);
				}
			});
			
		}
		return list;
		//return null;
	}
	
	class StellarMapAdapter implements Adapter { 
		/** 
		 * 返回有几组数据 
		 */
		@Override
		public int getGroupCount() {
			return 3;
		}
		/**
		 * 返回每组多少数据
		 */
		@Override
		public int getCount(int group) {
			return 11;
		}
		/**
		 * 返回要随机摆放的view对象
		 * group: 表示当前是第几组
		 * position: 表示的是组中的位置
		 */
		@Override
		public View getView(int group, int position, View convertView) {
			//因为这里要返回的是字符串,所以使用textview
			final TextView textView = new TextView(getActivity());
			// 1.给textview设置数据
			// 计算对应的list集合中的位置
			int listPosition = group * getCount(group) + position;
			textView.setText(list.get(listPosition));
			
			// 2.设置字体大小,随机大小
			Random random = new Random();
			textView.setTextSize(random.nextInt(12) + 12); //12-24
			
			// 3.设置字体颜色,随机颜色,抽取到工具类中
			
			/*int red = random.nextInt(200);
			int green = random.nextInt(190);
			int blue = random.nextInt(180);
			
			//使用rgb三原色混合生成一种新的颜色
			int textcolor = Color.rgb(red, green, blue);*/
			
			
			textView.setTextColor(RandomColorUtil.randomColor());
			
			//4.设置点击事件
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.toastShow(textView.getText().toString());
				}
			});
			return textView;
		}
		/**
		 * 获取执行完平移动画后,下一组加载那组数据,
		 * 但是此方法定以后并没有使用,然而并没有什么用
		 */
		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return 0;
		}
		/**
		 * 获取执行完缩放动画后,下一组加载那组数据
		 * group: 表示当前是第几组
		 */
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			//0->1->2->0
			return (group + 1) % getGroupCount();
		}
		
	}
}
