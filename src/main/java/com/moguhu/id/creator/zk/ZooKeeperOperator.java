package com.moguhu.id.creator.zk;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.moguhu.id.creator.IdWorker;


/**
 * 
 * @author xuefeihu
 *
 */
@Component
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
	
	/**
	 * get IdWorker
	 * @return
	 */
	public IdWorker generateWorker() {
		
		IdWorker worker = null;
		try {
			ZooKeeperOperator zkoperator = new ZooKeeperOperator();
			zkoperator.connect("127.0.0.1");

			try {
				zkoperator.create("/payengine", null, CreateMode.PERSISTENT);
			} catch (NodeExistsException e) {
				LOGGER.info(" payengine node has exists. ");
				// do nothing
			}
			
			List<String> children = zkoperator.getChild("/payengine");
			int i = 1;
			if(CollectionUtils.isNotEmpty(children)) {
				while(i <= Integer.MAX_VALUE) {
					if(!children.contains(String.valueOf(i))) {
						zkoperator.create("/payengine/" + i, String.valueOf(i).getBytes(), CreateMode.EPHEMERAL);
						break;
					}
					i++;
				}
			} else {
				zkoperator.create("/payengine/" + i, String.valueOf(i).getBytes(), CreateMode.EPHEMERAL);
			}
			
			worker = new IdWorker(i, 1L);
			worker.setKeeperOperator(zkoperator);
//			zkoperator.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return worker;
	}
	
}
