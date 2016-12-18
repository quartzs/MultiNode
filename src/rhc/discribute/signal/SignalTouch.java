package rhc.discribute.signal;

import java.util.Collection;
import java.util.Map;


public interface SignalTouch <T extends SignalInterface> {

	public static final String SPIDER="spider";
	public static final String CRAWLER="crawler";
	public static final String REQUEST="request";
	public static final String RESPONSE="response";
	
	void touchSignal(Collection<T> signalInterfaces,Map<String,Object> signalObject);
}
