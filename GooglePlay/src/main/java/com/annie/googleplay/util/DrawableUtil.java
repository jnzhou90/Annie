package com.annie.googleplay.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build.VERSION;

public class DrawableUtil {
	
	/**
	 * @Description: 在代码中动态生成图片,
	 * @param: 另外其实安卓中显示的图片都是Drawable类型的
	 */
	public static Drawable generateDrawable(float radius) {
		//GradientDrawable这个类对应的就是XML文件中的shape属性
		GradientDrawable gradientDrawable = new GradientDrawable();
		//图片形状设置为矩形,默认就是矩形
		gradientDrawable.setShape(GradientDrawable.RECTANGLE);
		//设置矩形四周的圆角半径
		gradientDrawable.setCornerRadius(radius);
		//设置生成图片的颜色,随机生成颜色!!!
		gradientDrawable.setColor(RandomColorUtil.randomColor());
		return gradientDrawable;
	}
	
	/**
	 * @Description: 在代码中动态生成状态选择器
	 * @param: 之所以是数组,是因为支持多种状态对应同一张图片
	 */
	public static Drawable generateSelector(Drawable pressed,Drawable normal) {
		StateListDrawable stateListDrawable = new StateListDrawable();
		//为相应的事件设置显示的图片,设置按下的图片
		stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed); 
		//设置默认的图片
		stateListDrawable.addState(new int[]{}, normal);
		
		//设置按下时图片的渐变效果
		//对系统版本进行判断
		if (VERSION.SDK_INT >= 14) {
			//如果是4.0之后 的设备才设置动画
			//默认图片到按下图片的过渡时间
			stateListDrawable.setEnterFadeDuration(600); 
			//按下图片到默认图片的过渡时间
			stateListDrawable.setExitFadeDuration(600);
		}
		return stateListDrawable;
	}

}
