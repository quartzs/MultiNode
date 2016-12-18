package rhc.discribute.node.commander;

import rhc.discribute.node.connecter.Connecter;
import rhc.discribute.node.nodeManage.NodeManage;
import rhc.discribute.signal.SignalManage;

public interface Commander {

	/**获取节点管理
	 * @return
	 */
	NodeManage getNodeManage();
	
	/**获取连接器
	 * @return
	 */
	Connecter getConnecter();
	
	/**获取信号管理
	 * @return
	 */
	SignalManage getSignalManage();
	
	/**打开连接，开始获取存在的其它节点
	 * @throws Exception
	 */
	void begin() throws Exception;
}
