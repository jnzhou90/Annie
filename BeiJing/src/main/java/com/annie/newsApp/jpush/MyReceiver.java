package com.annie.newsApp.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		System.out.println("收到广播了,可以对intent数据进行相应的获取,具体看文档");
	}

}
