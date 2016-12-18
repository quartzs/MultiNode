package rhc.discribute.access.signal;

import java.util.List;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.host.Host;
import rhc.discribute.signal.signalInterface.HostJoinSignal;

public class HostJoin extends BaseComponent implements HostJoinSignal{

	@Override
	public void process(Host host, List<String> labelList) {
		
		logger.info(host+" "+labelList+" 加入");
		
	}

}
