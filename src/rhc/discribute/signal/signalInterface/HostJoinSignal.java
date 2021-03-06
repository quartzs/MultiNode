package rhc.discribute.signal.signalInterface;

import java.util.List;

import rhc.discribute.node.host.Host;
import rhc.discribute.signal.SignalInterface;

/**主机加入信号
 * @author rhc
 *
 */
public interface HostJoinSignal extends SignalInterface{
	
	/**
	 * 传递主机
	 */
	public static final String HOST="host";
	
	/**
	 * 传递主机标签
	 */
	public static final String LABEL_LIST="labelList";
	
	/**
	 * @param host
	 * @param labelList
	 */
	void process(Host host,List<String> labelList);
	
}
