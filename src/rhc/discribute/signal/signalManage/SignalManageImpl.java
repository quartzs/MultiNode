package rhc.discribute.signal.signalManage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import rhc.discribute.signal.Signal;
import rhc.discribute.signal.SignalInterface;
import rhc.discribute.signal.SignalManage;

/**信号管理实现类
 * @author lenovo1
 *
 */
public class SignalManageImpl implements SignalManage{

	private Map<Signal, Set<SignalInterface>> signal_signalInterface=new ConcurrentHashMap<Signal,Set<SignalInterface>>();
	
	@Override
	public void connect(Signal signal,SignalInterface signalObject) {
		if(!signal.connectClass.isInstance(signalObject)){
			return;
		}
		addSignalObject(signal,signalObject);
	}

	@Override
	public void disConnect(Signal signal, SignalInterface signalObject) {
		removeSignalObject(signal,signalObject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void touchSignal(Signal signal,Map<String,Object> signalData) {
		
		if(!signalExists(signal)){
			return;
		}
		
		Collection<SignalInterface> signalObjectList=this.getSignalObjects(signal);
		
		if(null == signalObjectList || signalObjectList.isEmpty()){
			return;
		}
		
		signal.touch.touchSignal(signalObjectList, signalData);
		
		if(null != signalData){
			signalData.clear();
		}
	}
	
	/**增加一个信号连接
	 * @param signal
	 * @param signalObject
	 */
	private void addSignalObject(Signal signal,SignalInterface signalObject){
		
		if(!signalExists(signal)){
			createSignalContainer(signal);
		}
		
		signal_signalInterface.get(signal).add(signalObject);
	}
	
	/**移除一个信号连接
	 * @param signal
	 * @param signalObject
	 */
	private synchronized void removeSignalObject(Signal signal,SignalInterface signalObject){
		
		if(signalExists(signal)){
			Set<SignalInterface> signalContainer=signal_signalInterface.get(signal);
			signalContainer.remove(signalObject);
			//因为signal是全局变量,所以必须从map中删除容器,容器空间才会释放
			if(signalContainer.isEmpty()){
				signal_signalInterface.remove(signal);
			}
		}
	}
	
	/**如果一个信号容器不存在则创建
	 * @param signal
	 */
	private boolean signalExists(Signal signal){
		return signal_signalInterface.containsKey(signal);
	}
	
	/**创建一个信号容器
	 * @param signal
	 */
	private synchronized void createSignalContainer(Signal signal){
		
		if(signal_signalInterface.containsKey(signal)){
			return;
		}
		
		Set<SignalInterface> signalContainer=new HashSet<SignalInterface>();
		signal_signalInterface.put(signal, signalContainer);
	}
	
	/**获取一个信号所属的信号对象
	 * @param signal
	 * @return
	 */
	private List<SignalInterface> getSignalObjects(Signal signal){
		
		if(!signalExists(signal)){
			return null;
		}
		
		Set<SignalInterface> signalContainer=signal_signalInterface.get(signal);
		
		return new ArrayList<SignalInterface>(signalContainer);
	}

}
