package rhc.discribute.node.connecter.impl.zookper.watchCallback;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.connecter.impl.zookper.WatchCallback;
import rhc.discribute.node.connecter.impl.zookper.ZookperExecute;
import rhc.discribute.node.exception.CallbackException;

/**存放主机host的数据节点监控
 * @author rhc
 *
 */
public class MainHostNodeWatchCallback  extends BaseComponent implements WatchCallback{

	@Override
	public void executeCallback(WatchedEvent event, Object callbackData) throws CallbackException {
		ZookperExecute zkExecute=(ZookperExecute) callbackData;
		try {
			zkExecute.addMainNodeWatch();
			if(event.getType() == EventType.NodeDeleted){
				zkExecute.mainNodeDisConnect();
			}
			zkExecute.mainNodeDataChange();
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
