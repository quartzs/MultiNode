package rhc.discribute.access.signal;

import rhc.discribute.BaseComponent;
import rhc.discribute.signal.signalInterface.NoMainSignal;

public class NoMain extends BaseComponent implements NoMainSignal{

	@Override
	public void process() {
		
		logger.info("暂时没有主节点");
		
	}

}
