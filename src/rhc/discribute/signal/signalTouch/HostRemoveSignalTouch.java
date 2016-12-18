package rhc.discribute.signal.signalTouch;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import rhc.discribute.node.host.Host;
import rhc.discribute.signal.SignalTouch;
import rhc.discribute.signal.signalInterface.HostJoinSignal;
import rhc.discribute.signal.signalInterface.HostRemoveSignal;

public class HostRemoveSignalTouch implements SignalTouch<HostRemoveSignal>{

	/**
	 * 传递主机
	 */
	public static final String HOST="host";
	
	/**
	 * 传递主机标签
	 */
	public static final String LABEL_LIST="labelList";
	@Override
	public void touchSignal(Collection<HostRemoveSignal> signalInterfaces, Map<String, Object> signalObject) {

		if(null == signalObject){
			return;
		}
		
		Host host=(Host) signalObject.get(HostRemoveSignal.HOST);
		List<String> labelList=(List<String>) signalObject.get(HostRemoveSignal.LABEL_LIST);
		
		for(HostRemoveSignal hjs:signalInterfaces){
			hjs.process(host, labelList);
		}
		
	}

}
