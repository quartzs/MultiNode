package rhc.discribute.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**辅助系统关闭时执行操作的工具类
 * @author rhc
 *
 */
public class SystemCloseHookUtil {

	private static final List<Runnable> CLOSE_HOOK=Collections.synchronizedList(new LinkedList<Runnable>());
	
	private static final Logger logger = LoggerFactory.getLogger(SystemCloseHookUtil.class);
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				if(CLOSE_HOOK.size() == 0){
					return;
				}
				for(Runnable r:CLOSE_HOOK){
					try{
						r.run();
					}catch(Exception e){
						logger.error("执行系统关闭钩子时出错：",e.getMessage());
					}
				}
			}
		});
	}
	
	/**增加一个系统关闭钩子
	 * @param r
	 */
	public static void addSystemCloseHook(Runnable r){
		if(null == r){
			return;
		}
		CLOSE_HOOK.add(r);
	}
}
