package rhc.discribute.node.nodeManage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import rhc.discribute.node.host.Host;

/**
 * @author rhc
 *
 */
public interface NodeManage {
	
	/**连接主机状态
	 * @author rhc
	 *
	 */
	public static enum NodeStatus{
		INIT("初始状态"),DIS_CONNECT("连接断开"),CONNECT("已连接"),NO_MAIN("没有主机");
		private String desc;
		NodeStatus(String desc){
			this.desc=desc;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
	}

	/**获取所有主节点
	 * @return
	 * @throws Exception
	 */
	Collection<Host> getAllHost() throws Exception;
	
	/**获取当前主节点
	 * @return
	 * @throws Exception
	 */
	Host getMainHost() throws Exception;
	
	/**获取本机节点
	 * @return
	 * @throws Exception
	 */
	Host getCurrentHost() throws Exception;
	
	/**节点是否存在
	 * @param host
	 * @return
	 * @throws Exception
	 */
	boolean hostExists(Host host) throws Exception;
	
	/**节点是否是主节点
	 * @param host
	 * @return
	 * @throws Exception
	 */
	boolean hostIsMaster(Host host) throws Exception;
	
	/**获取节点标签
	 * @param host
	 * @return
	 * @throws Exception
	 */
	List<String> getHostLabel(Host host) throws Exception;
	
	/**获取带有标签的节点列表
	 * @param label
	 * @return
	 * @throws Exception
	 */
	Set<Host> getLabelHostSet(String label) throws Exception;
	
	/**获取当前状态
	 * @return
	 * @throws Exception
	 */
	NodeStatus getNodeStatus() throws Exception;
	
	/**是否可以成为主机
	 * @return
	 */
	boolean canMain();
}
