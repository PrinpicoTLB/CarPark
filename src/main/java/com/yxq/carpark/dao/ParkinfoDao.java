package com.yxq.carpark.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yxq.carpark.entity.ParkInfo;

@Mapper
public interface ParkinfoDao extends BaseDao<ParkInfo>{
	//���ͣ��λ��Ϣ
	public void save(ParkInfo parkInfo);
	public ParkInfo findParkinfoByParknum(@Param("parknum")int parknum);
	public void deleteParkinfoByParkNum(@Param("parknum")int parknum);
	public ParkInfo findParkinfoByCardnum2(@Param("cardnum")String cardnum);
	public ParkInfo findParkinfoByCardnum(@Param("cardnum")String cardnum);
	public void updateCardnum(@Param("cardnum")String cardnum, @Param("cardnumNew")String cardnumNew);
}
