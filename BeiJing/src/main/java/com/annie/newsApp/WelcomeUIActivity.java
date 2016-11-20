package com.annie.newsApp;

import com.annie.newsApp.util.Constant;
import com.annie.newsApp.util.SharedPreferencesUtil;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeUIActivity extends Activity {

	private ImageView mIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_ui);

		// 初始化控件
		initView();

	}

	/**
	 * @Description:初始化控件
	 * @param:
	 */
	private void initView() {
		mIcon = (ImageView) findViewById(R.id.welcome_iv_icon);

		// 设置动画
		setAnimation();
	}

	/**
	 * @Description:创建动画
	 * @param:
	 */
	private void setAnimation() {
		// 旋转动画
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1500);
		ra.setFillAfter(true); // 保持动画的结束状态

		// 缩放动画
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(1200);
		sa.setFillAfter(true);

		// 渐变动画(透明动画)
		AlphaAnimation la = new AlphaAnimation(0, 1);
		la.setDuration(1800);
		sa.setFillAfter(true);

		// 创建动画集合
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ra);
		as.addAnimation(la);
		as.addAnimation(sa);

		// 启动动画集合
		mIcon.startAnimation(as);

		// 为动画设置监听
		as.setAnimationListener(new MyAnimationListener());

	}

	// 创建动画监听
	private class MyAnimationListener implements AnimationListener {

		// 动画开始的时候调用
		@Override
		public void onAnimationStart(Animation animation) {

		}

		// 动画结束的时候调用
		@Override
		public void onAnimationEnd(Animation animation) {
			// 判断是否是第一次进入应用, 是-->引导界面 否-->主界面
			if (SharedPreferencesUtil.getBoolean(WelcomeUIActivity.this,
					Constant.IS_FIRST_OPEN_APP, true)) {
				// 动画结束后,暂停1.5秒后进入引导界面
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						Intent intent = new Intent(WelcomeUIActivity.this,
								GuideUIActivity.class);
						startActivity(intent);
						finish();
					}
				}, 1500);
			} else {
				// 直接进入主界面
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						Intent intent = new Intent(WelcomeUIActivity.this,
								MainUIActivity.class);
						startActivity(intent);
						finish();
					}
				}, 1500);
			}

		}

		// 动画重复的时候调用
		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

}
