package rhc.discribute.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**并行任务工具类
 * @author rhc
 *
 */
public class AsyncTaskUtil {

	private static final int MAX_THREAD=50;
	
	private static Semaphore asyncTaskSemaphore=new Semaphore(MAX_THREAD*2);
	
	private static final ExecutorService asyncTaskExecutor=Executors.newCachedThreadPool();

	private static final Logger logger=Logger.getLogger(AsyncTaskUtil.class);
	
	static{
		SystemCloseHookUtil.addSystemCloseHook(new Runnable(){
			@Override
			public void run() {
				asyncTaskExecutor.shutdown();
			}
		});
	}
	
	/**使用默认的信号将任务加入线程池
	 * @param runnable
	 */
	public static void async(final Runnable runnable) {
		async(asyncTaskSemaphore,runnable);
	}
	
	/**
	 * 将线程加入线程池
	 * 
	 * @param runnable
	 * @throws InterruptedException
	 */
	public static void async(final Semaphore semaphore,final Runnable runnable) {
		//
		if(null != semaphore){
			try {
				semaphore.acquire();
			} catch (InterruptedException e1) {
			}//
		}
		
		asyncTaskExecutor.execute(new Runnable(){
			public void run(){
				try {
					runnable.run();
				} catch (Exception e) {
					logger.error("异步线程执行出错", e);
				}finally{
					if(null != semaphore){
						semaphore.release();
					}
				}
			}
		});
	}
	
	/**使用默认的信号将任务加入线程池
	 * @param runnable
	 * @param latch
	 */
	public static void async(final Runnable runnable,final CountDownLatch latch){
		async(asyncTaskSemaphore,runnable,latch);
	}
	
	/**将任务加入线程池执行并消耗并行信号
	 * @param runnable
	 * @param latch
	 */
	public static void async(final Semaphore semaphore,final Runnable runnable,final CountDownLatch latch){
		//
		if(null != semaphore){
			try {
				semaphore.acquire();
			} catch (InterruptedException e1) {
			}//
		}
		asyncTaskExecutor.execute(new Runnable() {
			public void run(){
				try {
					runnable.run();
				} catch (Exception e) {
					logger.error("异步线程执行出错", e);
				}finally{
					latch.countDown();
					if(null !=semaphore){
						semaphore.release();
					}
				}
			}
		});
	}
	
	/**获取一个执行{taskCount}个并行任务信号
	 * @param taskCount
	 * @return
	 */
	public static CountDownLatch getCountDownLatch(int taskCount){
		return new CountDownLatch(taskCount);
	}
	
	/**等待并行信号消耗完毕
	 * @param latch
	 * @return
	 */
	public static boolean awaitTaskFinish(CountDownLatch latch){
		return awaitTaskFinish(latch,-1);
	}
	
	/** 等待该并行任务信号消耗完毕
	 * @param latch 信号
	 * @param maxMinils 最多等待毫秒数，小于0时表示等待信号消耗完毕
	 * @return
	 */
	public static boolean awaitTaskFinish(CountDownLatch latch,long maxMinils){
		try {
			if(maxMinils<0){
				latch.await();
			}else{
				latch.await(maxMinils, TimeUnit.MILLISECONDS);
			}
		} catch (InterruptedException e) {
			logger.error("等待任务结束出错");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args){
		AsyncTaskUtil.async(new Runnable(){

			@Override
			public void run() {
				System.out.println("first begin");
				AsyncTaskUtil.async(new Runnable(){

					@Override
					public void run() {
						System.out.println("second");
					}
					
				});
				System.out.println("first end");
			}
			
			
		});
	}
}
