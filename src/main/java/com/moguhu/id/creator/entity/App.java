package com.moguhu.id.creator.entity;

import java.io.Serializable;

public class App implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键.
	 */
	private Long id;
	
	public App(){}

	public App(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}