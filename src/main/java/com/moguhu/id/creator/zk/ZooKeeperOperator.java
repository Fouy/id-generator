package com.moguhu.id.creator.zk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author xuefeihu
 *
 */
public class ZooKeeperOperator extends AbstractZooKeeper {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperOperator.class);
	
	/**
	 * 创建持久态的znode,比支持多层创建.比如在创建/parent/child的情况下,无/parent.无法通过
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void create(String path, byte[] data, CreateMode createMode)throws KeeperException, InterruptedException{
		/**
		 * 此处采用的是CreateMode是PERSISTENT  表示The znode will not be automatically deleted upon client's disconnect.
		 * EPHEMERAL 表示The znode will be deleted upon the client's disconnect.
		 */ 
		this.zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, createMode);
	}
	
	/**
	 * 获取节点信息
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public List<String> getChild(String path) throws KeeperException, InterruptedException {
		List<String> list = new ArrayList<String>();
		try {
			list = this.zooKeeper.getChildren(path, false);
		} catch (KeeperException.NoNodeException e) {
			throw e;
		}
		return list;
	}
	
	public byte[] getData(String path) throws KeeperException, InterruptedException {   
        return  this.zooKeeper.getData(path, false,null);   
    }  
	
	 public static void main(String[] args) {
		try {
			ZooKeeperOperator zkoperator = new ZooKeeperOperator();
			zkoperator.connect("127.0.0.1");

			byte[] data = "payengine".getBytes();

			try {
				zkoperator.create("/payengine", data, CreateMode.PERSISTENT);
			} catch (NodeExistsException e) {
				// do nothing
			}
			LOGGER.info(Arrays.toString(zkoperator.getData("/payengine")));
			
			List<String> children = zkoperator.getChild("/payengine");
			if(CollectionUtils.isNotEmpty(children)) {
				int i = 1;
				while(i <= Integer.MAX_VALUE) {
					if(!children.contains(String.valueOf(i))) {
						zkoperator.create("/payengine/" + i, String.valueOf(i).getBytes(), CreateMode.EPHEMERAL);
						break;
					}
					i++;
				}
			} else {
				zkoperator.create("/payengine/" + 1, String.valueOf(1).getBytes(), CreateMode.EPHEMERAL);
			}

			// zkoperator.create("/root/child1",data);
			// System.out.println(Arrays.toString(zkoperator.getData("/root/child1")));
			//
			// zkoperator.create("/root/child2",data);
			// System.out.println(Arrays.toString(zkoperator.getData("/root/child2")));

			// String zktest = "ZooKeeper的Java API测试";
			// zkoperator.create("/root/child3", zktest.getBytes());

			// LOGGER.debug("获取设置的信息：" + new
			// String(zkoperator.getData("/root/child3")));

			zkoperator.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
