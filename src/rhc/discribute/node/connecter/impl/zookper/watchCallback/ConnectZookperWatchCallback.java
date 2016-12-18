package rhc.discribute.node.connecter.impl.zookper.watchCallback;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.connecter.impl.zookper.WatchCallback;
import rhc.discribute.node.exception.CallbackException;

/**连接上zookper回调
 * @author rhc
 *
 */
public class ConnectZookperWatchCallback extends BaseComponent implements WatchCallback{

	public ConnectZookperWatchCallback(){
		
	}
	
	@Override
	public void executeCallback(WatchedEvent event, Object callbackData) throws CallbackException {
		if(!(callbackData instanceof CountDownLatch)){
			throw new CallbackException("参数不是 CountDownLatch");
		}
		CountDownLatch latch=(CountDownLatch)callbackData;
		if (event.getState() == Event.KeeperState.SyncConnected) {
			logger.info("connected zookper");
		}else{
			logger.error("收到异常事件 "+event);
		}
		latch.countDown();
	}

	@Override
	public boolean isSync() {
		return false;
	}

}
