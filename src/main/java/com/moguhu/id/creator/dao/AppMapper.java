package com.moguhu.id.creator.dao;

import org.springframework.stereotype.Repository;

import com.moguhu.id.creator.entity.App;

@Repository
public interface AppMapper {
	
    void deleteById(Long id);

    void insert(App record);

}