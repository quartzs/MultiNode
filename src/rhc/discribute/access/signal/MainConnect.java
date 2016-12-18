package rhc.discribute.access.signal;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.host.Host;
import rhc.discribute.signal.signalInterface.MainConnectSignal;

public class MainConnect extends BaseComponent implements MainConnectSignal{

	@Override
	public void process(Host host) {
		logger.info("主节点 "+host+" 加入");
	}

}
