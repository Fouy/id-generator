package com.moguhu.id.creator.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.moguhu.id.creator.dao.AppMapper;
import com.moguhu.id.creator.entity.App;
import com.moguhu.id.creator.service.AppService;

@Service
public class AppServiceImpl implements AppService {
	
	@Resource
	private AppMapper appMapper;

	@Override
	public void insert(App app) {
		appMapper.insert(app);
	}

}
