package rhc.discribute.node.connecter.impl.zookper;

import org.apache.zookeeper.WatchedEvent;

import rhc.discribute.node.exception.CallbackException;

/**监控回调
 * @author rhc
 *
 */
public interface WatchCallback {

	/**回调方法
	 * @param callbackData
	 * @throws CallbackException
	 */
	void executeCallback(WatchedEvent event,Object callbackData) throws CallbackException;
	
	/**是否同步
	 * @return
	 */
	boolean isSync();
	
}
