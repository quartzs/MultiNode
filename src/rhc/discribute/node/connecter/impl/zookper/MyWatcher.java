package rhc.discribute.node.connecter.impl.zookper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.exception.CallbackException;
import rhc.discribute.util.AsyncSemaphoreUtil;

/**
 * @author rhc
 *
 */
public class MyWatcher extends BaseComponent implements Watcher{

	/**
	 * 回调对象
	 */
	private WatchCallback watchCallback;
	
	/**
	 * 回调数据
	 */
	private Object watchCallbackData;
	
	/**
	 * 关注的事件类型
	 */
	private Set<EventType> focusEventTypeSet;
	
	/**
	 * 是否只使用一次，只使用一次需要将引用去掉，否则回收不了
	 */
	private boolean useOne;
	
	public MyWatcher(WatchCallback watchCallback,Object watchCallbackData,boolean useOne){
		this.watchCallback=watchCallback;
		this.watchCallbackData=watchCallbackData;
		this.useOne=useOne;
		focusEventTypeSet=new HashSet<EventType>();
	}
	
	/**添加关注的事件类型
	 * @param eventType
	 */
	final public void addFocusEventType(EventType eventType){
		focusEventTypeSet.add(eventType);
	}
	
	/**添加多个关注的事件类型
	 * @param eventTypes
	 */
	final public void addFocusEventType(Collection<EventType> eventTypes){
		focusEventTypeSet.addAll(eventTypes);
	}

	@Override
	public void process(WatchedEvent event) {
		if(null == watchCallback){
			logger.error("没有注册回调对象");
		}
		
		EventType eventType=event.getType();
		logger.debug("接收到事件类型 "+eventType.toString());
		if(!focusEventTypeSet.isEmpty() && focusEventTypeSet.contains(eventType)){
			logger.debug("不关注事件类型 "+eventType.toString());
			return;
		}
		
		if(watchCallback.isSync()){
			try {
				AsyncSemaphoreUtil.createSemaphore(watchCallback.getClass(), 1, false);
			} catch (InterruptedException e) {
			}
		}
		AsyncSemaphoreUtil.acquireSemaphore(watchCallback.getClass(), 1, -1);
		try {
			watchCallback.executeCallback(event, watchCallbackData);
		} catch (CallbackException e) {
			logger.error("收到关注事件执行回调出错");
			e.printStackTrace();
		}finally{
			if(watchCallback.isSync()){
				AsyncSemaphoreUtil.releaseSemaphore(watchCallback.getClass(),1);
			}
			if(useOne){
				clear();
			}
		}
	}
	
	/**
	 * 清除引用，方便回收
	 */
	public void clear(){
		focusEventTypeSet.clear();
		this.watchCallback=null;
		this.watchCallbackData=null;
	}
}
