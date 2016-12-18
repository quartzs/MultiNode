package rhc.discribute.node.connecter;

import rhc.discribute.node.commander.Commander;

/**连接器
 * @author rhc
 *
 */
public interface Connecter {

	Commander getCommander();
	
	void connect() throws Exception;
	
}
