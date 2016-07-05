package com.moguhu.id.creator;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.moguhu.id.creator.entity.App;
import com.moguhu.id.creator.service.AppService;
import com.moguhu.id.creator.zk.ZooKeeperOperator;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	
	public static void main(String[] args) throws InterruptedException {
		
		final CountDownLatch countDownLatch = new CountDownLatch(2);
		
		long start = System.currentTimeMillis();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		ZooKeeperOperator zkOperator = applicationContext.getBean(ZooKeeperOperator.class);
		final IdWorker idWorker = zkOperator.generateWorker();
		final AppService appService = applicationContext.getBean(AppService.class);
		int j = 1;
		while(j <= 2) {
			new Thread() {
				@Override
				public void run() {
					int i = 1;
					while(i <= 100000) {
						long id = idWorker.nextId();
						appService.insert(new App(id));
						i++;
					}
					countDownLatch.countDown();
					System.out.println("子线程处理完成");
				}
			}.start();
			j++;
		}
		
		countDownLatch.await();
		long dis = System.currentTimeMillis() - start;
		System.out.println("耗时：" + dis / 1000 + "s");
	}
	
}
