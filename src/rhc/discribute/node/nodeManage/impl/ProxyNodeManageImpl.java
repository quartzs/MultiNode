package rhc.discribute.node.nodeManage.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.host.Host;
import rhc.discribute.node.nodeManage.InnerNodeManage;
import rhc.discribute.signal.Signal;
import rhc.discribute.signal.SignalManage;
import rhc.discribute.signal.signalInterface.HostJoinSignal;
import rhc.discribute.signal.signalInterface.HostRemoveSignal;
import rhc.discribute.signal.signalInterface.MainDisConnectSignal;

/**代理节点管理，方便加入事件调用
 * @author rhc
 *
 */
public class ProxyNodeManageImpl extends BaseComponent implements InnerNodeManage{

	private InnerNodeManageImpl nodeManageImpl;
	
	private SignalManage signalManage;
	
	public ProxyNodeManageImpl(InnerNodeManageImpl nodeManageImpl,SignalManage signalManage){
		this.nodeManageImpl=nodeManageImpl;
		this.signalManage=signalManage;
	}
	
	@Override
	public Collection<Host> getAllHost() throws Exception {
		return nodeManageImpl.getAllHost();
	}

	@Override
	public Host getMainHost() throws Exception {
		return nodeManageImpl.getMainHost();
	}

	@Override
	public Host getCurrentHost() throws Exception {
		return nodeManageImpl.getCurrentHost();
	}

	@Override
	public boolean hostExists(Host host) throws Exception {
		return nodeManageImpl.hostExists(host);
	}

	@Override
	public boolean hostIsMaster(Host host) throws Exception {
		return nodeManageImpl.hostIsMaster(host);
	}

	@Override
	public List<String> getHostLabel(Host host) throws Exception {
		return nodeManageImpl.getHostLabel(host);
	}

	@Override
	public Set<Host> getLabelHostSet(String label) throws Exception {
		return nodeManageImpl.getLabelHostSet(label);
	}

	@Override
	public NodeStatus getNodeStatus() throws Exception {
		return nodeManageImpl.getNodeStatus();
	}

	@Override
	public boolean canMain() {
		return nodeManageImpl.canMain();
	}

	@Override
	public void addHost(Host host, List<String> labelList) throws Exception {
		nodeManageImpl.addHost(host, labelList);
		
		//触发节点加入事件
		Map<String,Object> signalObject=new HashMap<String,Object>(2);
		signalObject.put(HostJoinSignal.HOST, host);
		signalObject.put(HostJoinSignal.LABEL_LIST, labelList);
		signalManage.touchSignal(Signal.HOST_JOIN, signalObject);
	}

	@Override
	public void setCurrentHost(Host host, List<String> labelList) throws Exception {
		nodeManageImpl.setCurrentHost(host, labelList);
	}

	@Override
	public void setMainHost(Host host) throws Exception {
		nodeManageImpl.setMainHost(host);
	}

	@Override
	public void removeHost(Host host) throws Exception {
		List<String> labelList=this.getHostLabel(host);
		
		nodeManageImpl.removeHost(host);
		
		//触发节点加入事件
		Map<String,Object> signalObject=new HashMap<String,Object>(2);
		signalObject.put(HostRemoveSignal.HOST, host);
		signalObject.put(HostRemoveSignal.LABEL_LIST, labelList);
		signalManage.touchSignal(Signal.HOST_REMOVE, signalObject);
	}

	@Override
	public void changeNodeStatus(NodeStatus nodeStatus) throws Exception {
		nodeManageImpl.changeNodeStatus(nodeStatus);
		Host mainHost=nodeManageImpl.getMainHost();
		
		if(nodeStatus == NodeStatus.CONNECT){
			Map<String,Object> signalObject=new HashMap<String,Object>(1);
			signalObject.put(MainDisConnectSignal.HOST, mainHost);
			
			signalManage.touchSignal(Signal.MAIN_CONNECT, signalObject);
		}else if(nodeStatus == NodeStatus.DIS_CONNECT){
			Map<String,Object> signalObject=new HashMap<String,Object>(1);
			signalObject.put(MainDisConnectSignal.HOST, mainHost);
			
			signalManage.touchSignal(Signal.MAIN_DIS_CONNECT, signalObject);
		}else if(nodeStatus == NodeStatus.NO_MAIN){
			signalManage.touchSignal(Signal.NO_MAIN, null);
		}
	}

	@Override
	public List<String> getLabelList(String label) throws Exception {
		return nodeManageImpl.getLabelList(label);
	}

}
