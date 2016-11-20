package com.annie.googleplay.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;


/**
 * 负责封装http请求，提供get和post请求方法
 * Http请求的实现方案：
 * android原生：HttpURLConnection, HttpClient
 * 第三方类库：XUtil, OKHttp, Volley, async-http, Retrofit等；
 * @author Free
 *
 */
public class HttpEngine {
	
	//因为访问网络一个HttpClient对象就可以了,所以这里使用单例模式
	private static HttpEngine mHttpEngine = new HttpEngine();
	private HttpClient httpClient;
	//private String result;//result不能设为成员变量,需要通过方法返回!!!
	
	private HttpEngine() {
		httpClient = new DefaultHttpClient();
	}

	public static HttpEngine getInstance() {
		return mHttpEngine;
	}
	
	

	/**
	 * 执行get请求,注意是同步请求
	 * @param url
	 */
	public String get(String url) {
		System.out.println("请求的URL:" +url);
		String result = null;
		HttpGet httpGet = new HttpGet(url);
		try {
			//其实server响应数据的封装，包含响应头和响应体
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				//说明请求成功，那么可以取数据了,得到响应体-->得到相应体的内容
				InputStream is = response.getEntity().getContent();
				
				//创建内存输出流去读取数据
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte arr[]  = new byte[1024];
				int len = -1;
				while((len = is.read(arr)) != -1) {
					baos.write(arr, 0, len);
				}
				  
				//获取网络请求的结果
				result = new String(baos.toByteArray(), "utf-8");
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		System.out.println("网络请求的结果:" + result);
		return result; 
	}
	
	/**
	 * 执行异步get请求,需要像XUtils一样通过回调函数返回网络请求的结果,不能直接return!!!
	 * @param url
	 * @return
	 */
	public void asynGet(final String url,final HttpCallback callback) {
		new Thread(){
			public void run() {
				String result = get(url);
				
				//调用回调函数的方法
				if (callback != null) {
					callback.onSuccess(url);
				}else {
					callback.onFail(null);
				}
			};
		}.start();
	}
	
	/**
	 * http回调接口
	 * @author Free
	 *
	 */
	public interface HttpCallback{
		void onSuccess(String url);
		void onFail(Exception exception);
	}
	
	
	
	/**
	 * 下载文件，返回流对象
	 * 
	 * @param url
	 * @return
	 */
	public HttpResult download(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		boolean retry = true;//重试机制
		while (retry) {
			try {
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if(httpResponse!=null){
					return new HttpResult(httpClient, httpGet, httpResponse);
				}
			} catch (Exception e) {
				retry = false;
				e.printStackTrace();
				//LogUtil.e("HttpHelper", "download: "+e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Http返回结果的进一步封装
	 * @author Administrator
	 *
	 */
	public static class HttpResult {
		private HttpClient httpClient;
		private HttpGet httpGet;
		private HttpResponse httpResponse;
		private InputStream inputStream;

		public HttpResult(HttpClient httpClient, HttpGet httpGet,
				HttpResponse httpResponse) {
			super();
			this.httpClient = httpClient;
			this.httpGet = httpGet;
			this.httpResponse = httpResponse;
		}

		/**
		 * 获取状态码
		 * @return
		 */
		public int getStatusCode() {
			StatusLine status = httpResponse.getStatusLine();
			return status.getStatusCode();
		}

		/**
		 * 获取输入流
		 * @return
		 */
		public InputStream getInputStream(){
			if(inputStream==null && getStatusCode()<300){
				HttpEntity entity = httpResponse.getEntity();
				try {
					inputStream =  entity.getContent();
				} catch (Exception e) {
					e.printStackTrace();
					//LogUtil.e(this, "getInputStream: "+e.getMessage());
				}
			}
			return inputStream;
		}

		/**
		 * 关闭链接和流对象
		 */
		public void close() {
			if (httpGet != null) {
				httpGet.abort();
			}
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					//LogUtil.e(this, "close: "+e.getMessage());
				}
			}
			//关闭链接
			if (httpClient != null) {
				httpClient.getConnectionManager().closeExpiredConnections();
			}
		}
	}
	
	
}

