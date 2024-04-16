package com.yxq.carpark.service;

import com.yxq.carpark.dto.FormData;
import com.yxq.carpark.entity.ParkInfo;


public interface ParkinfoService {
	public void saveParkinfo(FormData data);
	public ParkInfo findParkinfoByParknum(int parknum);
	public void deleteParkinfoByParkNum(int parkNum);
	public ParkInfo findParkinfoByCardnum2(String cardnum);
	public ParkInfo findParkinfoByCardnum(String cardnum);
	public void updateCardnum(String cardnum, String cardnumNew);
}
