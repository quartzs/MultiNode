package rhc.discribute.node.node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rhc.discribute.node.host.Host;

/**节点
 * @author rhc
 *
 */
public class Node {

	private Host currentHost;
	
	private Host mainHost;
	
	/**
	 * 保存所有节点地址
	 */
	private Set<Host> hostSet=new HashSet<Host>();
	
	/**
	 * 每一个标签有哪些地址
	 */
	private Map<String,Set<Host>> label_hostSet=new HashMap<String,Set<Host>>();
	
	/**
	 * 每一个节点有哪些标签
	 */
	private Map<Host,List<String>> host_labelList=new HashMap<Host,List<String>>();
	
	public Host getCurrentHost() {
		return currentHost;
	}

	public void setCurrentHost(Host currentHost) {
		this.currentHost = currentHost;
	}

	public Host getMainHost() {
		return mainHost;
	}

	public void setMainHost(Host mainHost) {
		this.mainHost = mainHost;
	}

	public Set<Host> getHostSet() {
		return hostSet;
	}

	public void setHostSet(Set<Host> hostSet) {
		this.hostSet = hostSet;
	}

	public Map<String, Set<Host>> getLabel_hostSet() {
		return label_hostSet;
	}

	public void setLabel_hostSet(Map<String, Set<Host>> label_hostSet) {
		this.label_hostSet = label_hostSet;
	}

	public Map<Host, List<String>> getHost_labelList() {
		return host_labelList;
	}

	public void setHost_labelList(Map<Host, List<String>> host_labelList) {
		this.host_labelList = host_labelList;
	}
	
}
