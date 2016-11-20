package com.annie.googleplay.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.annie.googleplay.R;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.manager.DownloadInfo;
import com.annie.googleplay.manager.DownloadManager;
import com.annie.googleplay.manager.DownloadManager.DownloadObserver;
import com.annie.googleplay.util.CommonUtil;

public class DownloadHolder extends BaseHolder<AppInfo> implements 
			OnClickListener,DownloadObserver{

	ProgressBar pb_download;
	Button btn_download;
	
	@Override
	public View initView() {
		View view = View.inflate(GooglePlayApplication.context, R.layout.layout_detail_download, null);
		pb_download = (ProgressBar) view.findViewById(R.id.pb_download);
		btn_download = (Button) view.findViewById(R.id.btn_download);
		
		//为下载按钮设置点击事件
		btn_download.setOnClickListener(this);
		//调用回调函数,更新UI
		DownloadManager.getInstance().registerDownloadObserver(this);
		return view;
	}

	private AppInfo appInfo;
	@Override
	public void setBindData(AppInfo appInfo) {
		this.appInfo = appInfo;
		
		//在刚进入界面的时候就根据状态刷新UI
		DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
		if (downloadInfo != null) {
			//downloadInfo不为空说明有下载任务,因此需要刷新一下UI
			onDownloadStateChange(downloadInfo);
		}
	}

	/**
	 * 移除监听器
	 */
	public void unregisterDownloadObserver() {
		DownloadManager.getInstance().unRegisterDownloadObserver(this);
	}
	//为下载按钮设置点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download:
			// 根据stat判断对应的逻辑
			DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
			if (downloadInfo == null) {
				//说明没有下载过,应立即下载
				DownloadManager.getInstance().download(appInfo);
			}else {
				// 说明下载过
				if (downloadInfo.getState() == DownloadManager.STATE_DOWNLOADING
						|| downloadInfo.getState() == DownloadManager.STATE_WAITING) {
					// 需要暂停
					DownloadManager.getInstance().pause(appInfo);
				} else if (downloadInfo.getState() == DownloadManager.STATE_PAUSE
						|| downloadInfo.getState() == DownloadManager.STATE_ERROR) {
					// 需要继续下载
					DownloadManager.getInstance().download(appInfo);
				} else if (downloadInfo.getState() == DownloadManager.STATE_FINISH) {
					// 安装
					DownloadManager.getInstance().installApk(appInfo);
				}
			}
			break;
		}
	}

	//监听下载进度
	@Override
	public void onDownloadPrrogressChange(final DownloadInfo downloadInfo) {
		//判断当前下载的downloadinfo和appinfo是否是同一个个
		if (appInfo == null || downloadInfo.getId() != appInfo.getId()) {
			return;
		}
		
		// 1.计算进度
		int percent = (int) (downloadInfo.getCurrentLength() * 100f
				/ downloadInfo.getSize() + 0.5f);
		// 2.设置进度
		pb_download.setProgress(percent);
		// 3.让下载按钮显示百分比
		btn_download.setText(percent + "%");
		// 去掉btn的背景
		btn_download.setBackgroundResource(0);

	}

	//监听下载状态
	@Override
	public void onDownloadStateChange(final DownloadInfo downloadInfo) {
		// 判断当前下载的downloadinfo和appinfo是否是同一个
		if (appInfo == null || downloadInfo.getId() != appInfo.getId()) {
			return;
		}
		
		switch (downloadInfo.getState()) {
		case DownloadManager.STATE_ERROR:
			btn_download.setText("出错,重下");
			break;
		case DownloadManager.STATE_FINISH:
			btn_download.setText("安装");
			break;
		case DownloadManager.STATE_WAITING:
			btn_download.setText("等待中...");
			break;
		case DownloadManager.STATE_PAUSE:
			btn_download.setText("继续下载");
			
			//当显示继续下载的同时应该显示出下载的进度条.因此..
			// 1.计算进度
			int percent = (int) (downloadInfo.getCurrentLength() * 100f / downloadInfo.getSize() +0.5f);
			// 2.为进度条设置进度
			pb_download.setProgress(percent);
			// 3.隐藏button按钮的背景
			btn_download.setBackgroundResource(0);
			
			break;

		}
	}

}
