package com.annie.googleplay.holder;

import java.util.ArrayList;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.bean.SafeInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.global.ImageloaderOptions;
import com.annie.googleplay.util.UrlUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SafeHolder extends BaseHolder<AppInfo> implements OnClickListener{
	
	ImageView iv_image1,iv_image2,iv_image3,iv_safe_arrow;
	ImageView iv_safe_icon1,iv_safe_icon2,iv_safe_icon3;
	TextView tv_safe_des1,tv_safe_des2,tv_safe_des3;
	LinearLayout ll_safe1,ll_safe2,ll_safe3,ll_safe_anim;

	@Override
	public View initView() {
		View view = View.inflate( GooglePlayApplication.context, R.layout.layout_detail_safe, null);
		iv_image1 = (ImageView) view.findViewById(R.id.iv_image1);
		iv_image2 = (ImageView) view.findViewById(R.id.iv_image2);
		iv_image3 = (ImageView) view.findViewById(R.id.iv_image3);
		iv_safe_arrow = (ImageView) view.findViewById(R.id.iv_safe_arrow);
		iv_safe_icon1 = (ImageView) view.findViewById(R.id.iv_safe_icon1);
		iv_safe_icon2 = (ImageView) view.findViewById(R.id.iv_safe_icon2);
		iv_safe_icon3 = (ImageView) view.findViewById(R.id.iv_safe_icon3);
		tv_safe_des1 = (TextView) view.findViewById(R.id.tv_safe_des1);
		tv_safe_des2 = (TextView) view.findViewById(R.id.tv_safe_des2);
		tv_safe_des3 = (TextView) view.findViewById(R.id.tv_safe_des3);
		
		ll_safe1 = (LinearLayout) view.findViewById(R.id.ll_safe2);
		ll_safe2 = (LinearLayout) view.findViewById(R.id.ll_safe2);
		ll_safe3 = (LinearLayout) view.findViewById(R.id.ll_safe3);
		ll_safe_anim = (LinearLayout) view.findViewById(R.id.ll_safe_anim);
		
		//为该view设置监听器
		view.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void setBindData(AppInfo appInfo) {
		ArrayList<SafeInfo> safeList = appInfo.getSafe();
		
		//因为对于每个应用而言,所拥有的该模块的图片数量不同,
		//所以应先进行判断; 显示到第一个
		if (safeList.size() >0) {
			SafeInfo safeinfo1 = safeList.get(0);
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + safeinfo1.getSafeUrl(), iv_image1, ImageloaderOptions.FadeIn_options);
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + safeinfo1.getSafeDesUrl(), iv_safe_icon1, ImageloaderOptions.options);
			tv_safe_des1.setText(safeinfo1.getSafeDes());
		}else {
			//否则需隐藏第一个
			ll_safe1.setVisibility(View.GONE);
		}
		
		//至少显示到第二个
		if (safeList.size() > 1) {
			SafeInfo safeinfo2 = safeList.get(1);
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + safeinfo2.getSafeUrl(), iv_image2, ImageloaderOptions.FadeIn_options);
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + safeinfo2.getSafeDesUrl(), iv_safe_icon2, ImageloaderOptions.options);
			tv_safe_des2.setText(safeinfo2.getSafeDes());
		}else {
			//否则需隐藏第二个
			ll_safe2.setVisibility(View.GONE);
		}
		
		//显示到第三个
		if (safeList.size() >2) {
			SafeInfo safeinfo3 = safeList.get(2);
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + safeinfo3.getSafeUrl(), iv_image3, ImageloaderOptions.FadeIn_options);
			ImageLoader.getInstance().displayImage(UrlUtil.IMAGE_HEAD + safeinfo3.getSafeDesUrl(), iv_safe_icon3, ImageloaderOptions.options);
			tv_safe_des3.setText(safeinfo3.getSafeDes());
		}else {
			//否则需隐藏第三个
			ll_safe3.setVisibility(View.GONE);
		}
		
		//记录最初的paddingTop值
		originalPaddingTop = ll_safe_anim.getPaddingTop();
		
		//在一开始的时候需要先隐藏布局,让它的paddingTop减小变为负的高度
		//获取控件的宽高有两种方式,这里使用 设置全局布局监听器的方式
		ll_safe_anim.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			/**
			 * 该方法在ll_safe_anim进行完布局之后执行
			 */
			@Override
			public void onGlobalLayout() {
				//一般会立即移除监听器
				ll_safe_anim.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
				paddingTop = - ll_safe_anim.getHeight();
				ll_safe_anim.setPadding(ll_safe_anim.getPaddingLeft(), paddingTop, ll_safe_anim.getPaddingRight(),
						ll_safe_anim.getPaddingBottom());
			}
		});
		
		/**
		 * getMeasuredHeight:获取onMeasure方法中设置的高度
		 * getHeight:是获取控件最终显示到屏幕上的高度，
		 * 只是一般情况下，两者是相等的。
		 */
		
	}

	private int paddingTop; //一开始隐藏后的paddingTop值
	private int originalPaddingTop; //最初的paddingTop值
	private boolean isOpen; //用于判断当前view是否被打开
	private boolean isAnim; //用于判断当前动画是否正在执行
	
	// 最开始隐藏后,需要设置动画打开和关闭该布局,需要为该布局设置监听器
	// 使用animator来实现自定义动画,功能强大,可实现各种属性动画的效果
	// ValueAnimator只是值的动画器，它只会让你传入的值进行一个变化，并不会有具体的动画效果；
	// 我们一般需要监听值改变的过程，然后自己实现自己的动画逻辑
	@Override
	public void onClick(View v) {
		// 当快速点击时如果当前动画正在执行还未执行完,则直接返回
		if (isAnim) {
			return;
		}
		
		ValueAnimator valueAnimator = null;
		if (isOpen) {
			//需要关闭view; 创建值动画,值从参1-->参2
			valueAnimator = ValueAnimator.ofInt(originalPaddingTop,paddingTop);
		}else {
			//需要打开view; 创建值动画,值从参1-->参2
			valueAnimator = ValueAnimator.ofInt(paddingTop,originalPaddingTop);
		}
		
		//创建动画监听器
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			/**
			 * 动画值更新的监听器，只要动画没有结束，会一直执行
			 */
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				//不断获取动画值
				int animatedValue = (Integer) animation.getAnimatedValue();
				//将获取的动画值赋值给布局,以实现动画
				ll_safe_anim.setPadding(ll_safe_anim.getPaddingLeft(), animatedValue, 
						ll_safe_anim.getPaddingRight(),ll_safe_anim.getPaddingBottom());
			}
		});
		valueAnimator.setDuration(520); //设置值动画的持续时间
		valueAnimator.start(); //开启值动画
		
		
		//使用nineoldandroids 第三方类库来实现箭头的旋转,
		//也可以使用objectAnimator属性动画来完成
		ViewPropertyAnimator.animate(iv_safe_arrow)
							.rotationBy(180)
							.setListener(new MyAnimatorListener())
							.setDuration(520)
							.start();
		
		// 最后重新设置标识
		isOpen = !isOpen;
	}
	
	//设置监听器,当快速点击时如果当前动画正在执行还未执行完,
	//则先执行完此次动画才可以继续下一次
	class MyAnimatorListener implements AnimatorListener {

		@Override
		public void onAnimationCancel(Animator arg0) {
			
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
			//当动画结束的时候将标识置为false,表示动画执行完毕
			isAnim = false;
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
			
		}

		@Override
		public void onAnimationStart(Animator arg0) {
			//当动画开始的时候将标识置为true,表示正在执行动画
			isAnim = true;
		}
		
	}

}
