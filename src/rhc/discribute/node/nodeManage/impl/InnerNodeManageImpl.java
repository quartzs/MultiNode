package rhc.discribute.node.nodeManage.impl;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import rhc.discribute.BaseComponent;
import rhc.discribute.common.ConfigConstants;
import rhc.discribute.node.host.Host;
import rhc.discribute.node.node.Node;
import rhc.discribute.node.nodeManage.InnerNodeManage;
import rhc.discribute.node.setting.Setting;
import rhc.discribute.util.HostUtil;

/**节点管理
 * @author rhc
 *
 */
public class InnerNodeManageImpl extends BaseComponent implements InnerNodeManage{

	private Node node;
	
	/**
	 * 默认端口号
	 */
	private static final String DEFAULT_PORT="8080";
	
	/**
	 * 初始化节点为初始状态
	 */
	private NodeStatus nodeStatus=NodeStatus.INIT;
	
	/**
	 * 是否可以成为主节点，可以表示当主节点消失有竞争权
	 */
	private boolean canMain=false;
	
	/**
	 * 判断本机标签配置里是否
	 */
	private static final String CAN_MAIN_REGEX="^(?:\\w+,)*"+ConfigConstants.CAN_MAIN+"(?:,\\w+)*$";
	
	public InnerNodeManageImpl(Setting setting) throws Exception{
		checkConfig(setting);
		
		Host localHost=this.getLocalHost(setting);
		String localLabel=setting.getProperty(ConfigConstants.LOCAL_LABEL);
		node=new Node();
		List<String> labelList=this.getLabelList(localLabel);
		this.setCurrentHost(localHost, labelList);
	}
	
	@Override
	public Collection<Host> getAllHost() throws Exception {
		return node.getHostSet();
	}

	@Override
	public synchronized void addHost(Host host,List<String> labelList) throws Exception {
		if(null == host || null == labelList){
			throw new NullPointerException();
		}
		if(labelList.isEmpty()){
			throw new IllegalArgumentException("必须至少有一个标签");
		}
		
		Set<Host> hostSet=node.getHostSet();
		if(null == hostSet){
			hostSet=new HashSet<Host>();
			node.setHostSet(hostSet);
		}
		
		hostSet.add(host);
		Map<Host,List<String>> host_labelList=node.getHost_labelList();
		Map<String,Set<Host>> label_hostSet=node.getLabel_hostSet();
		if(null == host_labelList){
			host_labelList=new HashMap<Host,List<String>>();
			node.setHost_labelList(host_labelList);
		}
		if(null == label_hostSet){
			label_hostSet=new HashMap<String,Set<Host>>();
			node.setLabel_hostSet(label_hostSet);
		}
		host_labelList.put(host, labelList);
		
		for(String label:labelList){
			Set<Host> set=label_hostSet.get(label);
			if(null == set){
				set=new HashSet<Host>();
				label_hostSet.put(label, set);
			}
			set.add(host);
		}
		
		logger.info("增加节点 "+host+" "+labelList);
	}

	@Override
	public Host getMainHost() throws Exception {
		return node.getMainHost();
	}

	@Override
	public Host getCurrentHost() throws Exception {
		return node.getCurrentHost();
	}

	@Override
	public boolean hostExists(Host host) throws Exception {
		Set<Host> hostSet=node.getHostSet();
		return hostSet.contains(host);
	}

	@Override
	public boolean hostIsMaster(Host host) throws Exception {
		Host mainHost=node.getMainHost();
		return null != mainHost && mainHost.equals(host);
	}

	@Override
	public void setCurrentHost(Host host,List<String> labelList) throws Exception {
		node.setCurrentHost(host);
		//加入保留标签
		if(this.canMain){
			labelList.add(ConfigConstants.CAN_MAIN);
		}
		
		this.addHost(host, labelList);
		
		logger.info("设置本机节点"+host+" "+labelList);
	}

	@Override
	public void setMainHost(Host host) throws Exception {
		node.setMainHost(host);
		logger.info("设置主节点为 "+host);
	}

	@Override
	public void removeHost(Host host) throws Exception {
		Set<Host> hostSet=node.getHostSet();
		Map<Host,List<String>> host_labelList=node.getHost_labelList();
		Map<String,Set<Host>> label_hostSet=node.getLabel_hostSet();
		
		List<String> labelList=null;
		if(null != hostSet && hostSet.contains(host)){
			hostSet.remove(host);
			
			labelList=host_labelList.remove(host);
			if(null != labelList){
				for(String label:labelList){
					Set<Host> hSet=label_hostSet.get(label);
					if(null != hSet){
						hSet.remove(host);
					}
				}
			}
		}
		
		logger.info("移除节点 "+host+" "+labelList);
	}
	
	/**获取本机Host
	 * @param properties
	 * @return
	 * @throws UnknownHostException 
	 */
	private Host getLocalHost(Properties properties) throws UnknownHostException{
		
		String localIp=null==properties?null:properties.getProperty(ConfigConstants.LOCAL_IP);
		if(null == localIp || localIp.trim().length() == 0){
			try {
				localIp=HostUtil.getCurrentIP();
			} catch (UnknownHostException e) {
				logger.error("获取本机ip失败");
				throw e;
			}
		}
		String localPort=null==properties?null:properties.getProperty(ConfigConstants.LOCAL_PORT);
		if(null == localPort || localPort.trim().length() == 0){
			localPort=DEFAULT_PORT;
		}
		
		Host host=new Host();
		host.setIp(localIp);
		host.setPort(Integer.parseInt(localPort));
		
		return host;
	}
	
	/**获取本机标签
	 * @param setting
	 * @return
	 * @throws Exception 
	 */
	public List<String> getLabelList(String labelStr) throws Exception{
		if(null == labelStr || !labelStr.matches("(?:\\w+,)*\\w+")){
			logger.error("请配置 "+ConfigConstants.LOCAL_LABEL);
			throw new Exception();
		}
		if(!labelStr.matches("(?:\\w+,)*\\w+")){
			logger.error("请将本机标签用英文逗号分隔");
			throw new Exception();
		}
		String[] labelArr=labelStr.split(",");
		
		List<String> labelList=new LinkedList<String>();
		
		for(String label:labelArr){
			labelList.add(label);
		}
		
		return labelList;
	}

	@Override
	public void changeNodeStatus(NodeStatus nodeStatus) throws Exception {
		this.nodeStatus=nodeStatus;
		logger.info(node.getCurrentHost()+" 更新节点状态为 "+nodeStatus.getDesc());
	}

	@Override
	public NodeStatus getNodeStatus() throws Exception {
		return nodeStatus;
	}

	@Override
	public List<String> getHostLabel(Host host) throws Exception {
		if(null == host){
			throw new NullPointerException();
		}
		
		if(!this.hostExists(host)){
			throw new IllegalArgumentException("节点 "+host+" 不存在");
		}
		
		List<String> labelList=node.getHost_labelList().get(host);
		
		return labelList;
	}

	@Override
	public Set<Host> getLabelHostSet(String label) throws Exception {
		if(null == label || label.trim().length() == 0){
			throw new NullPointerException();
		}
		Set<Host> hostSet=node.getLabel_hostSet().get(label);
		return hostSet;
	}

	/**检查配置
	 * @param setting
	 */
	private void checkConfig(Setting setting){
		String labelConfig=setting.getProperty(ConfigConstants.LOCAL_LABEL);
		if(null == labelConfig || labelConfig.trim().length() == 0){
			throw new IllegalArgumentException("必须配置 "+ConfigConstants.LOCAL_LABEL);
		}
		
		if(labelConfig.matches(CAN_MAIN_REGEX)){
			throw new IllegalArgumentException(ConfigConstants.CAN_MAIN+" 为保留标签，请配置为其它标签");
		}
		
		String canMain=setting.getProperty(ConfigConstants.CAN_MAIN);
		this.canMain=(null != canMain && canMain.equals("1"));
		
		if(this.canMain){
			labelConfig=labelConfig+","+ConfigConstants.CAN_MAIN;
			setting.put(ConfigConstants.LOCAL_LABEL, labelConfig);
		}
	}

	@Override
	public boolean canMain() {
		return canMain;
	}
	
}
