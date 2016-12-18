package rhc.discribute.node.connecter.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.zookeeper.CreateMode;

import rhc.discribute.SingleInstanceComponent;
import rhc.discribute.common.ConfigConstants;
import rhc.discribute.node.commander.Commander;
import rhc.discribute.node.connecter.BasicConnecter;
import rhc.discribute.node.connecter.impl.zookper.MyWatcher;
import rhc.discribute.node.connecter.impl.zookper.ZookperExecute;
import rhc.discribute.node.connecter.impl.zookper.ZookperOperate;
import rhc.discribute.node.connecter.impl.zookper.watchCallback.MainHostNodeWatchCallback;
import rhc.discribute.node.connecter.impl.zookper.watchCallback.NodeDataWatchCallback;
import rhc.discribute.node.connecter.impl.zookper.watchCallback.NodeJoinWatchCallback;
import rhc.discribute.node.host.Host;
import rhc.discribute.node.nodeManage.InnerNodeManage;
import rhc.discribute.node.nodeManage.NodeManage.NodeStatus;
import rhc.discribute.node.setting.Setting;
import rhc.discribute.util.SystemCloseHookUtil;

/**
 * 通过zookper连接
 * 
 * @author rhc
 *
 */
public class ZookperConnecter extends BasicConnecter implements ZookperExecute{

	private static final String MASTER_HOST_EPHEMERAL_PATH = "/master_host_ephemeral";

	private static final String HOST_PERSISTENT_PATH = "/host_persistent";

	private ZookperOperate zkOperate;
	
	public ZookperConnecter(Commander commander, Setting setting,InnerNodeManage nodeManage) throws Exception {
		super(commander, setting,nodeManage);
	}
	
	@Override
	public void connect() throws Exception{
		connectZK(setting);
		addHostNodeJoinWatch(setting);
		hostChange();
		addMasterHostWatch(setting);
	}

	/**连接zookeeper
	 * @param setting
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void connectZK(Setting setting) throws IOException, InterruptedException {
		String host = (String) setting.get(ConfigConstants.ZK_HOST);
		zkOperate=new ZookperOperate();
		zkOperate.connectZookeeper(host);
		SystemCloseHookUtil.addSystemCloseHook(new Runnable(){

			@Override
			public void run() {
				try {
					zkOperate.closeConnect();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}

	/**
	 * @param setting
	 * @throws Exception
	 */
	private void addHostNodeJoinWatch(Setting setting) throws Exception {
		createHostPersistentPath();
		createHostMasterEphemeralPath(setting);
		addHostPersistentChildWatch();
	}
	
	@Override
	public void addHostPersistentChildWatch() throws Exception{
		MyWatcher watcher = new MyWatcher(SingleInstanceComponent.getInstance(NodeJoinWatchCallback.class), this,true);
		zkOperate.watchPathChild(HOST_PERSISTENT_PATH, watcher);
	}

	/**
	 * 创建master永久节点
	 * 
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private void createHostPersistentPath() throws UnsupportedEncodingException, Exception {
		zkOperate.createPath(HOST_PERSISTENT_PATH, HOST_PERSISTENT_PATH.getBytes("UTF-8"), true, CreateMode.PERSISTENT);
	}

	/**
	 * 创建当前节点master临时节点
	 * 
	 * @throws Exception
	 */
	private void createHostMasterEphemeralPath(Setting setting) throws Exception {
		String localLabel=setting.getProperty(ConfigConstants.LOCAL_LABEL);
		String hostChildPath = getHostEphemeralPath(nodeManage.getCurrentHost());
		zkOperate.createPath(hostChildPath, localLabel.getBytes("UTF-8"), false, CreateMode.EPHEMERAL);
	}

	/**获取临时节点路径
	 * @param host
	 * @return
	 * @throws Exception
	 */
	private String getHostEphemeralPath(Host host) throws Exception {
		return HOST_PERSISTENT_PATH + "/" + host;
	}
	
	/**从生成节点路径得到Host对象
	 * @param path
	 * @return
	 */
	private Host fromHostEphemeralPathGetHost(String path){
		path=path.replaceFirst(".*/", "");
		return Host.fromPathGetHost(path);
	}

	/**设置当前节点数据
	 * @param message
	 * @throws Exception
	 */
	public void setCurrentHostData(String message) throws Exception{
		String path=this.getHostEphemeralPath(nodeManage.getCurrentHost());
		byte[] data=message.getBytes("UTF-8");
		zkOperate.setPathData(path, data);
	}
	
	/**
	 * 
	 */
	@Override
	public void hostChange() throws Exception{
		List<String> pathList=zkOperate.getChildPathList(HOST_PERSISTENT_PATH);
		
		if(null == pathList || pathList.isEmpty()){
			logger.info("获取到节点为空");
			return;
		}
		
		Collection<Host> hostSet=nodeManage.getAllHost();
		Set<Host> removeHost=null==hostSet?new HashSet<Host>():new HashSet<Host>(hostSet);
		List<Host> addHost=new LinkedList<Host>();
		for(String path:pathList){
			Host host=this.fromHostEphemeralPathGetHost(path);
			if(nodeManage.hostExists(host)){
				removeHost.remove(host);
			}else{
				addHost.add(host);
			}
		}
		if(!removeHost.isEmpty()){
			for(Host host:removeHost){
				nodeManage.removeHost(host);
			}
		}
		if(!addHost.isEmpty()){
			for(Host host:addHost){
				String path=this.getHostEphemeralPath(host);
				byte[] data=zkOperate.getPathData(path);
				if(null == data){
					logger.error(host+" 无法加入，获取不到数据");
					continue;
				}
				String pathData=new String(data,"UTF-8");
				List<String> labelList=nodeManage.getLabelList(pathData);
				nodeManage.addHost(host, labelList);
				this.addHostDataWatch(host);
			}
		}
	}
	
	/**增加一个节点时需要增加对该节点的数据监控
	 * @param host
	 * @throws Exception 
	 */
	private void addHostDataWatch(Host host) throws Exception{
		String hostPath=this.getHostEphemeralPath(host);
		this.addHostDataWatch(hostPath);
	}
	
	@Override
	public void addHostDataWatch(String path) throws Exception{
		MyWatcher watcher=new MyWatcher(SingleInstanceComponent.getInstance(NodeDataWatchCallback.class),this,true);
		byte[] data=zkOperate.watchPathData(path, watcher);
		this.hostDataChange(path, data);
	}
	
	@Override
	public void hostDataChange(String path) throws Exception{
		byte[] data=zkOperate.getPathData(path);
		this.hostDataChange(path, data);
	}
	
	private void hostDataChange(String path,byte[] newData) throws Exception{
		String strData=new String(newData,"UTF-8");
		logger.debug(path+" 更新数据为 "+strData);
		
		
	}
	
	
	/**增加主节点数据监控，当数据更新表示主节点更新
	 * @param setting
	 * @throws Exception 
	 */
	private void addMasterHostWatch(Setting setting) throws Exception{
		this.addMainNodeWatch();
		this.mainNodeDataChange();
	}

	@Override
	public void addMainNodeWatch() throws Exception {
		MyWatcher watcher=new MyWatcher(SingleInstanceComponent.getInstance(MainHostNodeWatchCallback.class),this,true);
		zkOperate.watchPath(MASTER_HOST_EPHEMERAL_PATH, watcher);
	}

	@Override
	public void mainNodeDataChange() throws Exception {
		if(zkOperate.existsPath(MASTER_HOST_EPHEMERAL_PATH)){
			String data=new String(zkOperate.getPathData(MASTER_HOST_EPHEMERAL_PATH),"UTF-8");
			Host host=Host.fromPathGetHost(data);
			nodeManage.setMainHost(host);
			//如果状态不是为已连接则更新为已连接
			if(nodeManage.getNodeStatus() != NodeStatus.CONNECT){
				nodeManage.changeNodeStatus(NodeStatus.CONNECT);
			}
		}else if(nodeManage.canMain()){
			//如果可以成为主节点则尝试
			createMainNode();
		}else{
			//如果已经没有可以成为主节点的节点，则更新状态
			Set<Host> canMainHostSet=nodeManage.getLabelHostSet(ConfigConstants.CAN_MAIN);
			if(null == canMainHostSet || canMainHostSet.isEmpty()){
				nodeManage.changeNodeStatus(NodeStatus.NO_MAIN);
			}
		}
	}
	
	/**
	 * 尝试创建主节点，创建成功则自己成为主节点
	 */
	private void createMainNode(){
		logger.debug("尝试创建主节点");
		try{
			byte[] data=nodeManage.getCurrentHost().toString().getBytes("UTF-8");
			zkOperate.createPath(MASTER_HOST_EPHEMERAL_PATH, data, true, CreateMode.EPHEMERAL);
			logger.debug("创建主节点成功");
		}catch(Exception e){
			logger.debug("创建主节点失败");
		}
	}

	@Override
	public void mainNodeDisConnect() throws Exception {
		nodeManage.changeNodeStatus(NodeStatus.DIS_CONNECT);
	}
	
}
