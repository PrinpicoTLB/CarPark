package com.yxq.carpark.dao;

import com.yxq.carpark.dto.ChargeData;
import com.yxq.carpark.entity.DepotInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepotInfoDao extends BaseDao<DepotInfo>{
	public void update(ChargeData chargeData);
	public DepotInfo findById(int id);
}
