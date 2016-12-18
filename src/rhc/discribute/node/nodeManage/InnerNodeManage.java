package rhc.discribute.node.nodeManage;

import java.util.List;

import rhc.discribute.node.host.Host;
import rhc.discribute.node.nodeManage.NodeManage.NodeStatus;

public interface InnerNodeManage extends NodeManage{

	
	/**增加子节点
	 * @param host
	 * @throws Exception
	 */
	void addHost(Host host,List<String> labelList) throws Exception;
	
	/**设置当前节点
	 * @param host
	 * @throws Exception
	 */
	void setCurrentHost(Host host,List<String> labelList) throws Exception;
	
	/**设置主节点
	 * @param host
	 * @throws Exception
	 */
	void setMainHost(Host host) throws Exception;
	
	/**移除节点
	 * @param host
	 * @throws Exception
	 */
	void removeHost(Host host) throws Exception;
	
	/**更新节点状态
	 * @param nodeStatus
	 * @throws Exception
	 */
	void changeNodeStatus(NodeStatus nodeStatus) throws Exception;
	
	/**从一个字符串获取到
	 * @param label
	 * @return
	 * @throws Exception
	 */
	List<String> getLabelList(String label) throws Exception;
	
}
