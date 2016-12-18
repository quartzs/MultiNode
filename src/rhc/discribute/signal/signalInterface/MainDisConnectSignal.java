package rhc.discribute.signal.signalInterface;

import java.util.List;

import rhc.discribute.node.host.Host;
import rhc.discribute.signal.SignalInterface;

/**主机断开连接
 * @author rhc
 *
 */
public interface MainDisConnectSignal extends SignalInterface {

	/**
	 * 传递主机
	 */
	public static final String HOST="host";
	
	void process(Host host);
	
}
