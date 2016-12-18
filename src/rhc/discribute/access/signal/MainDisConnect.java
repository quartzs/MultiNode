package rhc.discribute.access.signal;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.host.Host;
import rhc.discribute.signal.signalInterface.MainDisConnectSignal;

public class MainDisConnect extends BaseComponent implements MainDisConnectSignal{

	@Override
	public void process(Host host) {
		logger.info("主节点 "+host+" 离开");
	}
	
}
