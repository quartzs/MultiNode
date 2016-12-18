package rhc.discribute.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import rhc.discribute.node.host.Host;

/**ip辅助工具类
 * @author rhc
 *
 */
public class HostUtil {

	/**获取本机ip
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getCurrentIP() throws UnknownHostException{
		InetAddress addr = InetAddress.getLocalHost();
		String ip=addr.getHostAddress().toString();
		return ip;
	}
	
	public static Host getHost(String path){
		return null;
	}
	
}
