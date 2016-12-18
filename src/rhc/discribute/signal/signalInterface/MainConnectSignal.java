package rhc.discribute.signal.signalInterface;

import rhc.discribute.node.host.Host;
import rhc.discribute.signal.SignalInterface;

/**主机连接上
 * @author rhc
 *
 */
public interface MainConnectSignal extends SignalInterface {

	/**
	 * 传递主机
	 */
	public static final String HOST="host";
	
	void process(Host host);
	
}
