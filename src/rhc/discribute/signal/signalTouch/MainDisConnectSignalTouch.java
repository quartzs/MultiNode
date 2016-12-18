package rhc.discribute.signal.signalTouch;

import java.util.Collection;
import java.util.Map;

import rhc.discribute.node.host.Host;
import rhc.discribute.signal.SignalTouch;
import rhc.discribute.signal.signalInterface.MainDisConnectSignal;

public class MainDisConnectSignalTouch implements SignalTouch<MainDisConnectSignal>{

	@Override
	public void touchSignal(Collection<MainDisConnectSignal> signalInterfaces, Map<String, Object> signalObject) {
		
		if(null == signalObject){
			return;
		}
		
		Host host=(Host) signalObject.get(MainDisConnectSignal.HOST);
		
		for(MainDisConnectSignal mds:signalInterfaces){
			mds.process(host);
		}
		
	}

}
