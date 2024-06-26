package com.yxq.carpark.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yxq.carpark.*;
import com.yxq.carpark.entity.CardType;

@Mapper
public interface CardtypeDao extends BaseDao<CardType>{

	List<CardType> findAllCardType();

	CardType findCardTypeByid(@Param("typeid")int typeid);
	
}
