package rhc.discribute.node.connecter.impl.zookper;

public interface ZookperExecute {

	/**每一个加入的节点都会在该永久节点下创建一个临时节点，当节点丢失则其它还在线的节点都会感知到
	 * 每一次节点变更都需要重新调用该方法，因为对子节点变化只能监听一次
	 * @throws Exception
	 */
	void addHostPersistentChildWatch() throws Exception;
	
	/**节点发生了变化，通知connecter
	 * @throws Exception
	 */
	void hostChange() throws Exception;
	
	/**增加节点数据监控
	 * @param path
	 * @throws Exception
	 */
	void addHostDataWatch(String path) throws Exception;
	
	/**节点数据发生了变化
	 * @param path
	 * @throws Exception
	 */
	void hostDataChange(String path) throws Exception;
	
	/**增加主节点数据监控
	 * @throws Exception
	 */
	void addMainNodeWatch() throws Exception;
	
	/**主节点数据变化
	 * @throws Exception
	 */
	void mainNodeDataChange() throws Exception;
	
	/**主节点断开连接
	 * @throws Exception
	 */
	void mainNodeDisConnect() throws Exception;
}
