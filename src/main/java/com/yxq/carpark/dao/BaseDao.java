package com.yxq.carpark.dao;

import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

@Mapper
public interface BaseDao <M extends Serializable>{
	public void save(M m);
}
