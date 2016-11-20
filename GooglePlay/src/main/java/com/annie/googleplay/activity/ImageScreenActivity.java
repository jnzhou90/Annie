package com.annie.googleplay.activity;

import java.util.ArrayList;

import com.annie.googleplay.R;
import com.annie.googleplay.R.layout;
import com.annie.googleplay.R.menu;
import com.annie.googleplay.adapter.ImageScreenAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class ImageScreenActivity extends Activity {

	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_screen);
		
		//初始化viewpager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		//获取传递得到数据
		ArrayList<String> list = getIntent().getStringArrayListExtra("screenInfos");
		
		//为viewpager设置适配器展示图片
		viewPager.setAdapter(new ImageScreenAdapter(list));
	}

}
