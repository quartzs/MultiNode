package rhc.discribute.node.commander.impl;

import java.io.IOException;
import java.net.URL;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.commander.Commander;
import rhc.discribute.node.connecter.Connecter;
import rhc.discribute.node.connecter.impl.ZookperConnecter;
import rhc.discribute.node.nodeManage.NodeManage;
import rhc.discribute.node.nodeManage.impl.InnerNodeManageImpl;
import rhc.discribute.node.nodeManage.impl.NodeManageImpl;
import rhc.discribute.node.nodeManage.impl.ProxyNodeManageImpl;
import rhc.discribute.node.setting.Setting;
import rhc.discribute.signal.SignalManage;
import rhc.discribute.signal.signalManage.SignalManageImpl;

public class CommanderImpl extends BaseComponent implements Commander{

	private static String propertiesFile="/config.properties";
	
	private Setting setting;
	
	private Connecter connecter;
	
	/**
	 * 实际进行管理的实例
	 */
	private InnerNodeManageImpl innerNodeManage;
	
	/**
	 * 代理实例，会加入事件触发
	 */
	private ProxyNodeManageImpl proxyNodeManage;
	
	/**
	 * 提供给外部使用的
	 */
	private NodeManage nodeManage;
	
	/**
	 * 信号管理
	 */
	private SignalManage signalManage;
	
	public CommanderImpl() throws Exception{
		this(propertiesFile);
	}
	
	public CommanderImpl(String propertiesFile) throws Exception{
		setting=this.getSetting(propertiesFile);
		innerNodeManage=new InnerNodeManageImpl(setting);
		signalManage=new SignalManageImpl();
		proxyNodeManage=new ProxyNodeManageImpl(innerNodeManage,signalManage);
		nodeManage=new NodeManageImpl(innerNodeManage);
		connecter=new ZookperConnecter(this,setting,proxyNodeManage);
	}
	
	@Override
	public void begin() throws Exception{
		connecter.connect();
	}
	
	@Override
	public NodeManage getNodeManage() {
		return nodeManage;
	}
	
	@Override
	public Connecter getConnecter(){
		return connecter;
	}

	/**获取setting对象
	 * @return
	 * @throws IOException
	 */
	private Setting getSetting(String propertiesFile) throws IOException{
		Setting setting=new Setting();
		
		URL url=CommanderImpl.class.getResource(propertiesFile);
		setting.load(url.openStream());
		
		return setting;
	}

	@Override
	public SignalManage getSignalManage() {
		return signalManage;
	}
}
