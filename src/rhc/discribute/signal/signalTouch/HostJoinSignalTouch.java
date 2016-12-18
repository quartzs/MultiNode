package rhc.discribute.signal.signalTouch;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import rhc.discribute.BaseComponent;
import rhc.discribute.node.host.Host;
import rhc.discribute.signal.SignalTouch;
import rhc.discribute.signal.signalInterface.HostJoinSignal;

/**
 * @author rhc
 *
 */
public class HostJoinSignalTouch extends BaseComponent implements SignalTouch<HostJoinSignal>{

	@Override
	public void touchSignal(Collection<HostJoinSignal> signalInterfaces, Map<String, Object> signalObject) {
		
		if(null == signalObject){
			return;
		}
		
		Host host=(Host) signalObject.get(HostJoinSignal.HOST);
		List<String> labelList=(List<String>) signalObject.get(HostJoinSignal.LABEL_LIST);
		
		for(HostJoinSignal hjs:signalInterfaces){
			hjs.process(host, labelList);
		}
		
	}

}
