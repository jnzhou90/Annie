package com.annie.googleplay.util;

import java.util.Random;

import android.graphics.Color;

public class RandomColorUtil {
	public static  int randomColor() {
		Random random = new Random();
		
		int red = random.nextInt(200);
		int green = random.nextInt(190);
		int blue = random.nextInt(180);
		
		//使用rgb三原色混合生成一种新的颜色
		return Color.rgb(red, green, blue);
	}

}
