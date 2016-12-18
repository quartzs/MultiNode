package rhc.discribute.signal.signalTouch;

import java.util.Collection;
import java.util.Map;

import rhc.discribute.signal.SignalTouch;
import rhc.discribute.signal.signalInterface.NoMainSignal;

public class NoMainSignalTouch implements SignalTouch<NoMainSignal> {

	@Override
	public void touchSignal(Collection<NoMainSignal> signalInterfaces, Map<String, Object> signalObject) {
		
		for(NoMainSignal nms:signalInterfaces){
			nms.process();
		}
		
	}

}
