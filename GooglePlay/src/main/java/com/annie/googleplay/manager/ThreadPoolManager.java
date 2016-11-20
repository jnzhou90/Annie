package com.annie.googleplay.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理的封装:
 * 精通java线程池的使用
 *  1.java还提供了封装好的方便我们创建线程池的类和方法:
 *    Executors.newFixedThreadPool(nThreads);
 *    Executors.newCachedThreadPool(threadFactory);
 *    Executors.newSingleThreadExecutor();
 *  2.我们也可以自定义线程池的配置，使用ThreadPoolExecutor自定义，如下：
 */
public class ThreadPoolManager {
	private int corePoolSize;//核心线程池的大小，就是指能够同时执行的任务数量
	private int maximumPoolSize;//最大线程池的大小，线程池数量的上限
	private int keepAliveTime = 1;//存活时间
	private TimeUnit unit = TimeUnit.HOURS;//时间单位
	private ThreadPoolExecutor threadPoolExecutor;
	
	private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();
	public static ThreadPoolManager getInstance() {
		return threadPoolManager;
	}
	
	//通过构造函数进行初始化
	private ThreadPoolManager() {
		
		//计算核心线程池的大小：设备的可用处理器核心数*2 + 1，该数量的线程能够让cpu的效率得到最大发挥
		corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
		maximumPoolSize = corePoolSize; //最大线程池数量不要小于核心线程池数量
		
		//线程池的处理机制：优先核心线程池->缓冲队列->最大线程池->拒绝线程的策略
		threadPoolExecutor = new ThreadPoolExecutor(
				corePoolSize, //3
				maximumPoolSize,//10,当缓冲队列满的时候，会判断最大线程池 ,注意：最大线程池的大小是包含核心线程池大小的
				keepAliveTime, //表示的最大线程池中等待任务的存活时间
				unit, 
				new LinkedBlockingQueue<Runnable>(5), //缓冲队列,当核心线程池满的时候，会将任务存放到缓冲队列里等着
				Executors.defaultThreadFactory(), //创建线程的工厂
				new ThreadPoolExecutor.CallerRunsPolicy()); //拒绝线程的处理策略,CallerRunsPolic将当前线程移除
	}
	/**
	 * 线程池的处理机制：
	 *  优先核心线程池->缓冲队列->最大线程池->拒绝线程的策略
	 *  存活时间,时间单位,拒绝线程的策略,都是只对最大线程池而言的
	 *  在实际中当我们不设缓冲队列的上限时,这些都没有了
	 */
	
	/**
	 * 添加任务
	 */
	public void execute(Runnable runnable) {
		if (runnable != null) {
			threadPoolExecutor.execute(runnable);
		}
	}
	
	/**
	 * 将任务从线程池中移除，如果任务正在执行，会先终止线程，再移除
	 */
	public void remove(Runnable runnable) {
		if (runnable != null) {
			threadPoolExecutor.remove(runnable);
		}
	}
}
