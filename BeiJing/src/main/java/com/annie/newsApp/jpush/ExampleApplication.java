package com.annie.newsApp.jpush;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;

public class ExampleApplication extends Application {
@Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
