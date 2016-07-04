package com.moguhu.id.creator;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) {
		IdWorker idWorker = new IdWorker(0, 0);
		for(int i = 0; i <= 100; i++) {
			System.out.println(idWorker.nextId());
		}
	}
	
}
