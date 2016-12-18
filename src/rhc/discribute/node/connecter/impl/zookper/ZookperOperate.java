package rhc.discribute.node.connecter.impl.zookper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import rhc.discribute.BaseComponent;

/**封装zookeeper操作的类
 * @author rhc
 *
 */
public class ZookperOperate extends BaseComponent implements Watcher {

	private ZooKeeper zooKeeper;

	private static final int SESSION_TIME_OUT = 3000;

	private CountDownLatch countDownLatch = new CountDownLatch(1);

	/**
	 * 连接zookeeper
	 *
	 * @param host
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void connectZookeeper(String host) throws IOException, InterruptedException {
		zooKeeper = new ZooKeeper(host, SESSION_TIME_OUT, this);
		countDownLatch.await();
		logger.info("zookeeper connect ok");
	}

	/**
	 * 实现watcher的接口方法，当连接zookeeper成功后，zookeeper会通过此方法通知watcher
	 * 此处为如果接受到连接成功的event，则countDown，让当前线程继续其他事情。
	 */
	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == Event.KeeperState.SyncConnected) {
			countDownLatch.countDown();
		}
	}

	/**
	 * 根据路径创建节点，并且设置节点数据
	 *
	 * @param path
	 * @param data
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public String createNode(String path, byte[] data, CreateMode createNode) throws KeeperException, InterruptedException {
		return this.zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createNode);
	}

	/**
	 * 根据路径获取所有孩子节点
	 *
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public List<String> getChildren(String path) throws KeeperException, InterruptedException {
		return this.zooKeeper.getChildren(path, false);
	}

	public Stat setData(String path, byte[] data, int version) throws KeeperException, InterruptedException {
		return this.zooKeeper.setData(path, data, version);
	}

	/**
	 * 根据路径获取节点数据
	 *
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public byte[] getData(String path) throws KeeperException, InterruptedException {
		return this.zooKeeper.getData(path, false, null);
	}

	/**
	 * 删除节点
	 *
	 * @param path
	 * @param version
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void deletNode(String path, int version) throws InterruptedException, KeeperException {
		this.zooKeeper.delete(path, version);
	}

	/**
	 * 关闭zookeeper连接
	 *
	 * @throws InterruptedException
	 */
	public void closeConnect() throws InterruptedException {
		if (null != zooKeeper) {
			zooKeeper.close();
		}
	}
	
	public boolean existsPath(String path) throws KeeperException, InterruptedException{
		return null != this.zooKeeper.exists(path, false);
	}
	
	public boolean watchPath(String path,Watcher watcher) throws KeeperException, InterruptedException{
		return null != this.zooKeeper.exists(path, watcher);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 创建节点
	 * 
	 * @param path
	 * @param data
	 * @param allowExists
	 * @throws Exception
	 */
	public void createPath(String path, byte[] data, boolean allowExists, CreateMode createMode) throws Exception {
		if (existsPath(path)) {
			if (!allowExists) {
				throw new Exception(path + "已存在");
			} else {
				logger.debug("节点 " + path + " 已存在");
				return;
			}
		} else {
			String p=zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
			logger.debug("创建节点 " + path);
		}
	}

	/**
	 * 开始监控一个节点的子节点
	 * 
	 * @param path
	 * @param watcher
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void watchPathChild(String path, Watcher watcher) throws KeeperException, InterruptedException {
		zooKeeper.getChildren(path, watcher);
	}
	
	/**
	 * @param path
	 * @param watcher
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public byte[] watchPathData(String path,Watcher watcher) throws KeeperException, InterruptedException{
		return zooKeeper.getData(path, watcher, null);
	}
	
	/**获取节点数据
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public byte[] getPathData(String path) throws KeeperException, InterruptedException{
		return zooKeeper.getData(path, false, null);
	}

	
	/**设置节点数据
	 * @param path
	 * @param data
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public void setPathData(String path,byte[] data) throws KeeperException, InterruptedException{
		zooKeeper.setData(path, data, -1);
	}
	
	/**获取子节点列表
	 * @param path
	 * @return
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public List<String> getChildPathList(String path) throws KeeperException, InterruptedException {
		return zooKeeper.getChildren(path, false);
	}
}
