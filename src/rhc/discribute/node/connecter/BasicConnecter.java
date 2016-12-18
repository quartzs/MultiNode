package rhc.discribute.node.connecter;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.commander.Commander;
import rhc.discribute.node.nodeManage.InnerNodeManage;
import rhc.discribute.node.setting.Setting;

/**
 * @author rhc
 *
 */
abstract public class BasicConnecter extends BaseComponent implements Connecter{

	protected Commander commander;
	
	protected InnerNodeManage nodeManage;
	
	protected Setting setting;
	
	public BasicConnecter(Commander commander,Setting setting,InnerNodeManage nodeManage){
		this.commander=commander;
		this.nodeManage=nodeManage;
		this.setting=setting;
	}
	
	@Override
	public Commander getCommander(){
		return commander;
	}
}
