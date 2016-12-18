package rhc.discribute.node.connecter.impl.zookper.watchCallback;

import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.connecter.impl.zookper.WatchCallback;
import rhc.discribute.node.connecter.impl.zookper.ZookperExecute;
import rhc.discribute.node.exception.CallbackException;

/**节点数据监控
 * 每一个节点
 * @author rhc
 *
 */
public class NodeDataWatchCallback extends BaseComponent implements WatchCallback{

	@Override
	public void executeCallback(WatchedEvent event, Object callbackData) throws CallbackException {
		
		String path=event.getPath();
		
		ZookperExecute zkExecute=(ZookperExecute) callbackData;
		try {
			if(event.getType() != EventType.NodeDeleted){
				zkExecute.addHostDataWatch(path);
				zkExecute.hostDataChange(path);
			}else{
				logger.debug("节点 "+path+" 失去连接");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isSync() {
		return true;
	}

}
