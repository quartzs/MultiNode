package rhc.discribute.node.nodeManage.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.host.Host;
import rhc.discribute.node.nodeManage.InnerNodeManage;
import rhc.discribute.node.nodeManage.NodeManage;

public class NodeManageImpl extends BaseComponent implements NodeManage {

	private NodeManage nodeManage;
	
	public NodeManageImpl(NodeManage nodeManage){
		this.nodeManage=nodeManage;
	}
	
	@Override
	public Collection<Host> getAllHost() throws Exception {
		return nodeManage.getAllHost();
	}

	@Override
	public Host getMainHost() throws Exception {
		return nodeManage.getMainHost();
	}

	@Override
	public Host getCurrentHost() throws Exception {
		return nodeManage.getCurrentHost();
	}

	@Override
	public boolean hostExists(Host host) throws Exception {
		return nodeManage.hostExists(host);
	}

	@Override
	public boolean hostIsMaster(Host host) throws Exception {
		return nodeManage.hostIsMaster(host);
	}

	@Override
	public List<String> getHostLabel(Host host) throws Exception {
		return nodeManage.getHostLabel(host);
	}

	@Override
	public Set<Host> getLabelHostSet(String label) throws Exception {
		return nodeManage.getLabelHostSet(label);
	}

	@Override
	public NodeStatus getNodeStatus() throws Exception {
		return nodeManage.getNodeStatus();
	}

	@Override
	public boolean canMain() {
		return nodeManage.canMain();
	}

}
