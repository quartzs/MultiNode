package rhc.discribute.signal;

import java.util.Map;

public interface SignalManage {

	/**连接一个信号
	 * @param signal
	 * @param signalObject
	 */
	void connect(Signal signal,SignalInterface signalObject);
	
	/**取消一个信号连接
	 * @param signal
	 * @param signalObject
	 */
	void disConnect(Signal signal,SignalInterface signalObject);
	
	/**触发一个信号
	 * @param signal
	 */
	void touchSignal(Signal signal,Map<String,Object> signalObject);
	
	
}
