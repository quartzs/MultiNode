package rhc.discribute.util;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


/**多任务执行辅助类
 * @author rhc
 *
 */
public class AsyncSemaphoreUtil {

	private static Logger logger=Logger.getLogger(AsyncSemaphoreUtil.class);
	
	/**
	 * 一个唯一标记对应的信号
	 */
	private volatile static Map<Object,Semaphore> ID_SEMAPHORE=new ConcurrentHashMap<Object,Semaphore>();
	
	/**
	 * 内部锁
	 */
	private static final Object LOCK=new Object();
	
	/**创建一个 有count个信号，是自己创建的返回true，否则返回false
	 * @param count
	 * @return
	 * @throws InterruptedException 
	 */
	public static boolean createSemaphore(Object id,int count,boolean acquire) throws InterruptedException{
		if(ID_SEMAPHORE.containsKey(id)){
			return false;
		}
		synchronized(LOCK){
			if(ID_SEMAPHORE.containsKey(id)){
				return false;
			}
			Semaphore semaphore=createSemaphore(count);
			ID_SEMAPHORE.put(id, semaphore);
			if(acquire){
				semaphore.acquire(count);
			}
			return true;
		}
	}
	
	/**获取一个信号
	 * @param id
	 * @param count
	 * @return
	 */
	public static boolean acquireSemaphore(Object id,int count,long waitMinis){
		if(!ID_SEMAPHORE.containsKey(id)){
			return false;
		}
		Semaphore semaphore=ID_SEMAPHORE.get(id);
		
		if(waitMinis<0){
			try {
				semaphore.acquire(count);
				return true;
			} catch (Exception e) {
				return false;
			}
		}else if(waitMinis == 0){
			return semaphore.tryAcquire(count);
		}else{
			try {
				return semaphore.tryAcquire(count, waitMinis, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				return false;
			}	
		}
	}
	
	/**释放一个信号
	 * @param id
	 * @param count
	 * @return
	 */
	public static boolean releaseSemaphore(Object id,int count){
		if(!ID_SEMAPHORE.containsKey(id)){
			return false;
		}
		Semaphore semaphore=ID_SEMAPHORE.get(id);
		
		semaphore.release(count);
		
		return true;
	}
	
	/**等待一个信号
	 * @param id
	 * @param count
	 * @param timeout
	 * @return
	 */
	public static boolean waitSemaphore(Object id,int count,long timeout){
		
		if(ID_SEMAPHORE.containsKey(id)){
			Semaphore semaphore=ID_SEMAPHORE.get(id);
			boolean exception=false;
			if(timeout > 0){
				try {
					semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					exception=true;
				}finally{
					if(!exception){
						semaphore.release(count);
					}
				}
			}else{
				try {
					semaphore.acquire(count);
				} catch (InterruptedException e) {
					exception=true;
				}finally{
					if(!exception){
						semaphore.release(count);
					}
				}
			}
			return !exception;
		}else{
			return false;
		}
		
	}
	
	/**创建信号
	 * @param count
	 * @return
	 */
	private static Semaphore createSemaphore(int count){
		Semaphore semaphore=new Semaphore(count);
		return semaphore;
	}
	
	/**获取数据，先获取一份数据，如果没获取到则从另一个地方获取数据，否则直接返回
	 * @param semaphoreId
	 * @param before
	 * @param current
	 * @param count
	 * @return
	 */
	public static <T> T getFirstOrLastData(Object semaphoreId,Callable<T> before,Callable<T> current,int count,long time){
		
		try {
			AsyncSemaphoreUtil.createSemaphore(semaphoreId, count, false);
			if(AsyncSemaphoreUtil.acquireSemaphore(semaphoreId, count, time)){
				try{
					T data=null;
					if(null != before && null != (data=(T) before.call())) {
						return data;
					}
					data=(T) current.call();
					
					return data;
				}finally{
					AsyncSemaphoreUtil.releaseSemaphore(semaphoreId, count);
				}
			}else{
				logger.warn("信号id："+semaphoreId+" 在超时时间:"+time+" 未获取到信号，放弃计算");
			}
		} catch (Exception e) {
			logger.error("获取数据出错，"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}
