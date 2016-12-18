package rhc.discribute.signal.signalTouch;

import java.util.Collection;
import java.util.Map;

import rhc.discribute.node.host.Host;
import rhc.discribute.signal.SignalTouch;
import rhc.discribute.signal.signalInterface.MainConnectSignal;

public class MainConnectSignalTouch implements SignalTouch<MainConnectSignal>{
	
	@Override
	public void touchSignal(Collection<MainConnectSignal> signalInterfaces, Map<String, Object> signalObject) {
		if(null == signalObject){
			return;
		}
		
		Host host=(Host) signalObject.get(MainConnectSignal.HOST);
		
		for(MainConnectSignal mcs:signalInterfaces){
			mcs.process(host);
		}
		
	}

}
