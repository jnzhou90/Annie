package com.annie.googleplay.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.params.HttpParams;

import com.annie.googleplay.api.HttpEngine;
import com.annie.googleplay.api.HttpEngine.HttpResult;
import com.annie.googleplay.bean.AppInfo;
import com.annie.googleplay.global.GooglePlayApplication;
import com.annie.googleplay.util.CommonUtil;
import com.annie.googleplay.util.UrlUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseArray;

/**
 * @Description: 负责下载功能的管理
 * @author Free
 */
public class DownloadManager {
	// 定义下载目录 : /mnt/sdcard/包名/download
	public static String DOWNLOAD_DIR = Environment.getExternalStorageDirectory()
										+ File.separator+ GooglePlayApplication.context.getPackageName()
										+ File.separator + "download";
	// 定义6种状态常量
	public static final int STATE_NONE = 0;// 未下载的状态，或者是初始化的状态
	public static final int STATE_DOWNLOADING = 1;// 下载中的状态
	public static final int STATE_PAUSE = 2;// 暂停的状态，
	public static final int STATE_FINISH = 3;// 下载完成的状态，
	public static final int STATE_ERROR = 4;// 下载失败的状态，
	// 等待的状态，任务创建但是run方法木有执行，就是等待状态(线程池有空余线程时会自动执行)
	public static final int STATE_WAITING = 5;
	
	// 用来存储所有的下载监听器对象，多个界面需要多个监听器对象
	private ArrayList<DownloadObserver> observerList = new ArrayList<DownloadManager.DownloadObserver>();
	
	// 用来维护所有的下载任务相关数据,注意：此处并没有对下载数据进行持久化保存，应用一旦退出，就全部为空
	//SparseArray映射一个整数到对象,当key为整数的时候用SparseArray比HashMap要好,它是安卓的,被优化了
	private SparseArray<DownloadInfo> downloadInfoMaps = new SparseArray<DownloadInfo>();
	//private HashMap<Integer,DownloadInfo> downloadInfoMaps = new HashMap<Integer, DownloadInfo>();
	 
	
	private static DownloadManager downloadManager = new DownloadManager();
	public static DownloadManager getInstance() {
		return downloadManager;
	}

	private DownloadManager() {
		//创建下载目录
		File dir = new File(DOWNLOAD_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	/**
	 * 下载大方法
	 */
	public void download(AppInfo appInfo) {
		// 1.首先获取downloadinfo下载的相关数据,根据key(id)来获取
		DownloadInfo downloadInfo = downloadInfoMaps.get(appInfo.getId());
		if (downloadInfo == null) {
			//说明是第一次下载,之前没下载过,应初始化下载数据
			downloadInfo = DownloadInfo.create(appInfo);
			//将初始化之后的downloadinfo 添加到集合中,存储起来
			downloadInfoMaps.put(downloadInfo.getId(), downloadInfo);
		}
		
		// 2.判断state，是否能够进行下载, 只有这几种状态才能进行下载：none, pause, error
		int state = downloadInfo.getState();
		if (state == STATE_NONE || state == STATE_ERROR || state == STATE_PAUSE) {
			//说明可以下载,那么新建下载任务,交由线程池去执行
			DownloadTask downloadTask = new DownloadTask(downloadInfo);
			
			// 3.暂时将下载状态更改为等待,任务执行时会立即改为下载中
			downloadInfo.setState(STATE_WAITING);
			
			// 将下载状态的改变通知给监听器
			notifyDownloadStateChange(downloadInfo);
			
			//将下载任务交由线程池管理
			ThreadPoolManager.getInstance().execute(downloadTask);
		}
		
	}

	/**
	 * @Description: 通知所有监听器，下载状态更改了
	 * @param:
	 */
	private void notifyDownloadStateChange(final DownloadInfo downloadInfo) {
		CommonUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (DownloadObserver observer : observerList) {
					observer.onDownloadStateChange(downloadInfo);
				}
			}
		});
		
	}
	
	/**
	 * @Description: 通知所有监听器，下载进度状态更改了
	 * @param:
	 */
	private void notifyDownloadProgressChange(final DownloadInfo downloadInfo) {
		CommonUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (DownloadObserver observer : observerList) {
					observer.onDownloadPrrogressChange(downloadInfo);
				}
			}
		});
		
	}
	
	/**
	 * 请求网络保存文件的逻辑写在DownloadTask中
	 */
	class DownloadTask implements Runnable {
		//通过构造函数获取downloadInfo对象
		private DownloadInfo downloadInfo ;
		public DownloadTask(DownloadInfo downloadInfo) {
			super();
			this.downloadInfo = downloadInfo;
		}
		@Override
		public void run() {
			HttpResult httpResult = null;
			// 4.将下载状态更改为下载中
			downloadInfo.setState(STATE_DOWNLOADING);
			notifyDownloadStateChange(downloadInfo);
			
			// 5.开始下载,分两种: a.从头下载    b.断点下载
			File file = new File(downloadInfo.getPath());
			// 如果下载的apk不存在 或者 下载出错导致下载大小和文件大小不一致,须从头开始下载
			if (!file.exists() || downloadInfo.getCurrentLength() != file.length()) {
				file.delete(); //删除无效文件
				downloadInfo.setCurrentLength(0); //清空已经下载的大小
				
				httpResult = HttpEngine.getInstance().download(UrlUtil.download + downloadInfo.getDownloadUrl());
			}else {
				//说明是断点下载
				/*apk 断点下载的url
				String break_download = SERVER_HOST  + "download?name=%s&range=%s";*/
				String url = String.format(UrlUtil.break_download , downloadInfo.getDownloadUrl(),
											downloadInfo.getCurrentLength());
				httpResult = HttpEngine.getInstance().download(url);
			}
			
			// 6.处理返回的结果
			if (httpResult != null && httpResult.getInputStream() != null) {
				//说明请求成功,可以开始流的读取和文件的保存了
				InputStream is = httpResult.getInputStream(); //获取流
				//创建文件输出流
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file,true);
					byte[] arr = new byte[1024*8];
					int len = -1;
					while((len = is.read(arr)) != -1 && downloadInfo.getState() == STATE_DOWNLOADING) {
						fos.write(arr, 0, len);
						
						// 在将文件写入本地的同时应更新currentLength
						downloadInfo.setCurrentLength(downloadInfo.getCurrentLength() + len);
						//进度改变,需要通知状态监听器进度更新了
						notifyDownloadProgressChange(downloadInfo);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					file.delete(); //删除失败的问价
					downloadInfo.setCurrentLength(0); //清空已下载的大小
					downloadInfo.setState(STATE_ERROR); //将状态更改为下载失败
					notifyDownloadStateChange(downloadInfo); //通知状态监听器状态改变了
				}finally{
					//关流
					httpResult.close();
					try {
						if (fos != null ) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				//当代码走到最后有两种情况: a.下载完成   b.暂停
				if (downloadInfo.getSize() == file.length() && downloadInfo.getState() == STATE_DOWNLOADING) {
					//说明下载完成了
					downloadInfo.setState(STATE_FINISH); //修改状态
					notifyDownloadStateChange(downloadInfo); //通知状态改变了
				}else if (STATE_PAUSE == downloadInfo.getState()) {
					//说明是暂停
					notifyDownloadStateChange(downloadInfo);
				}
				
			}else {
				//说明请求失败
				file.delete(); //删除失败的问价
				downloadInfo.setCurrentLength(0); //清空已下载的大小
				downloadInfo.setState(STATE_ERROR); //将状态更改为下载失败
				notifyDownloadStateChange(downloadInfo); //通知状态监听器状态改变了
			}
		}
	}
	
	public void pause(AppInfo appInfo) {
		DownloadInfo downloadInfo = getDownloadInfo(appInfo);
		if (downloadInfo != null) {
			// 将state设置为暂停
			downloadInfo.setState(STATE_PAUSE);
		}
	}

	
	public DownloadInfo getDownloadInfo(AppInfo appInfo) {
		return downloadInfoMaps.get(appInfo.getId());
	}
	
	/**
	 * 安装的方法
	 */
	public void installApk(AppInfo appInfo) {
		DownloadInfo downloadInfo = downloadInfoMaps.get(appInfo.getId());
		if (downloadInfo != null) {
			// 开启系统的安装apk的界面， PackageInstaller;
			/*
			 * <action android:name="android.intent.action.VIEW" /> <category
			 * android:name="android.intent.category.DEFAULT" /> <data
			 * android:scheme="content" /> <data android:scheme="file" /> <data
			 * android:mimeType="application/vnd.android.package-archive" />
			 */

			Intent intent = new Intent(Intent.ACTION_VIEW);
			// 添加flag
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(
					Uri.parse("file://" + downloadInfo.getPath()),
					"application/vnd.android.package-archive");
			GooglePlayApplication.context.startActivity(intent);
		}
	}
	
	/**
	 * 对外提供回调
	 */
	//注册下载监听器
	public void registerDownloadObserver(DownloadObserver downloadObserver) {
		if (!observerList.contains(downloadObserver)) {
			observerList.add(downloadObserver);
		}
	}
	
	//移除下载监听器
		public void unRegisterDownloadObserver(DownloadObserver downloadObserver) {
			if (observerList.contains(downloadObserver)) {
				observerList.remove(downloadObserver);
			}
		}
	
	/**
	 * 提供下载进度和下载状态的监听器
	 */
	public interface DownloadObserver {
		//下载进度更新的回调
		void onDownloadPrrogressChange(DownloadInfo downloadInfo);
		
		//下载状态的回调
		void onDownloadStateChange(DownloadInfo downloadInfo);
	}
	
}

