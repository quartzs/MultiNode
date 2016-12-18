package rhc.discribute.node.connecter.impl.zookper.watchCallback;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.connecter.impl.zookper.WatchCallback;
import rhc.discribute.node.connecter.impl.zookper.ZookperExecute;
import rhc.discribute.node.exception.CallbackException;
import rhc.discribute.util.AsyncSemaphoreUtil;

/**
 * 主节点加入事件回调
 * 
 * @author rhc
 *
 */
public class NodeJoinWatchCallback extends BaseComponent implements WatchCallback {

	public NodeJoinWatchCallback() {
		super();
	}

	@Override
	public void executeCallback(WatchedEvent event, Object callbackData) throws CallbackException {

		ZookperExecute zkExecute = (ZookperExecute) callbackData;
		try {
			zkExecute.addHostPersistentChildWatch();
			zkExecute.hostChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		EventType eventType = event.getType();
//
//		if (eventType == EventType.NodeCreated || eventType == EventType.NodeChildrenChanged) {
//			String path = event.getPath();
//			logger.info("创建主节点 " + path);
//		} else if (eventType == EventType.NodeDeleted) {
//			String path = event.getPath();
//
//			logger.info("删除主节点 " + path);
//		} else {
//			logger.info("收到未知事件 " + event.toString());
//		}
	}

	@Override
	public boolean isSync() {
		return true;
	}

}
