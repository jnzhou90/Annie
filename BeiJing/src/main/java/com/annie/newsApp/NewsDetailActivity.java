package com.annie.newsApp;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.annie.newsApp.been.NewsCenterBeen.newsTab;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsDetailActivity extends Activity{

	private TextView mTitle;
	private ImageButton menuIcon;
	private ImageButton mBlack;
	private ImageButton mShare;
	private ImageButton mTextsize;
	private WebView mWeb;
	private ProgressBar mProgress;
	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//去除标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		
		//初始化控件
		initView();
	}

	/**
	 * @Description: 初始化控件
	 * @param:
	 */
	private void initView() {
		mTitle = (TextView) findViewById(R.id.basepager_tv_title);
		menuIcon = (ImageButton) findViewById(R.id.basepager_ib_menuIcon);
		mBlack = (ImageButton) findViewById(R.id.basepager_ib_back);
		mShare = (ImageButton) findViewById(R.id.basepager_ib_share);
		mTextsize = (ImageButton) findViewById(R.id.basepager_ib_textsize);
		mWeb = (WebView) findViewById(R.id.newsdetail_wv_web);
		mProgress = (ProgressBar) findViewById(R.id.newsdetail_pb_progress);
		
		
		mTitle.setVisibility(View.GONE);
		menuIcon.setVisibility(View.GONE);
		mShare.setVisibility(View.VISIBLE);
		mTextsize.setVisibility(View.VISIBLE);
		mBlack.setVisibility(View.VISIBLE);
		
		//初始化网页
		initWebView();
	}

	/**
	 * @Description: 初始化网页
	 * @param:
	 */
	private void initWebView() {
		String url = getIntent().getStringExtra("url");
		System.out.println(url);
		
		settings = mWeb.getSettings();
		settings.setBuiltInZoomControls(true);// 支持缩放按钮
		settings.setUseWideViewPort(true);// 支持双击缩放
		settings.setJavaScriptEnabled(true);// 支持JavaScript
		
		//监听webview
		mWeb.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 网页加载完成后回调
				//网页加载完成后隐藏进度条
				mProgress.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});
		
		//加载URL
		mWeb.loadUrl(url);
	}

	//返回的点击事件
	public void back(View view) {
		finish();
	}
	
	//设置字体的点击事件
	private int currentIndex = 2;
	private int tempIndex; //临时变量
	private WebSettings settings;
	public void size(View view ) {
		AlertDialog.Builder builder = new Builder(this);
		String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
		builder.setSingleChoiceItems(items, currentIndex, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//将选择的字体的索引先保存在临时变量中,只有点击确定的时候才保存
				tempIndex = which;
			}
		});
		
		//确定按钮
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				currentIndex = tempIndex;
				//根据索引选择字体
				switchSize(currentIndex);
			}
		});
		
		//取消按钮
		builder.setNegativeButton("取消", null);
		
		//显示
		builder.show();
	}
	
	//根据索引选择字体
	protected void switchSize(int currentIndex2) {
		switch (currentIndex2) {
		case 0:
			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;

		default:
			break;
		}
	}

	//设置分享的点击事件
	public void share(View view ) {
		showShare();
	}
	
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("哦哈哈哈哈哈哈哈哈哈");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("测试是是是是");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }
	
}
