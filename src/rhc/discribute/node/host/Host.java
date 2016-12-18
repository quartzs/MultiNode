package rhc.discribute.node.host;

public class Host {

	private String ip;

	private int port;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return ip + ":" + port;
	}

	@Override
	public int hashCode() {
		int hashCode = null == ip || ip.trim().length() == 0 ? 0 : ip.hashCode();
		return hashCode + port;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o || !(o instanceof Host)) {
			return false;
		}

		Host oH = (Host) o;
		String oIp = oH.getIp();
		int oPort = oH.getPort();

		if (ip != oIp) {
			if (null == oIp || null == ip) {
				return false;
			} else if (oIp.equals(ip) && port == oPort) {
				return true;
			} else {
				return false;
			}
		}
		
		return port==oPort;
	}

	/**
	 * 从host toString得到字符串获取Host对象
	 * 
	 * @param path
	 * @return
	 */
	public static Host fromPathGetHost(String path) {
		String[] ip_port = path.split(":");
		Host host = new Host();
		host.setIp(ip_port[0]);
		host.setPort(Integer.parseInt(ip_port[1]));
		return host;
	}
}
