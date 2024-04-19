package com.yxq.carpark.dto;

import java.io.Serializable;
import java.util.Date;


public class FormData implements Serializable {
	// 停车位ID
	private int id;
	private String judgecarnum;
	// 停车位号
	private Integer parkNum;
	// 卡号
	private String cardNum;
	// 停车车号
	private String carNum;
	// 是否临时停车
	private Integer parkTem;
	// 停车时间
	private Date parkin;
	// 离场时间
	private Date parkout;
	// 标签状态
	private Integer tag;
	// 违规信息
	private String illegalInfo;
	// 支付方式：支付宝，微信，现金，刷卡机收款
	private Integer payid;
	// 支付金额
	private Integer pay_money;
	// 支付类型是否成功
	private Integer pay_type;

	public String getJudgecarnum() {
		return judgecarnum;
	}

	public void setJudgecarnum(String judgecarnum) {
		this.judgecarnum = judgecarnum;
	}

	public int getPayid() {
		return payid;
	}
	public void setPayid(int payid) {
		this.payid = payid;
	}
	public int getPay_money() {
		return pay_money;
	}
	public void setPay_money(int pay_money) {
		this.pay_money = pay_money;
	}
	public int getPay_type() {
		return pay_type;
	}
	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}
	public String getIllegalInfo() {
		return illegalInfo;
	}
	public void setIllegalInfo(String illegalInfo) {
		this.illegalInfo = illegalInfo;
	}
	public Integer getTag() {
		return tag;
	}
	public void setTag(Integer tag) {
		this.tag = tag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParkNum() {
		return parkNum;
	}
	public void setParkNum(int parkNum) {
		this.parkNum = parkNum;
	}
	public Date getParkout() {
		return parkout;
	}
	public void setParkout(Date parkout) {
		this.parkout = parkout;
	}
	public Date getParkin() {
		return parkin;
	}
	public void setParkin(Date parkin) {
		this.parkin = parkin;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public int getParkTem() {
		return parkTem;
	}
	public void setParkTem(int parkTem) {
		this.parkTem = parkTem;
	}

	@Override
	public String toString() {
		return "FormData{" +
				"id=" + id +
				", judgecarnum='" + judgecarnum + '\'' +
				", parkNum=" + parkNum +
				", cardNum='" + cardNum + '\'' +
				", carNum='" + carNum + '\'' +
				", parkTem=" + parkTem +
				", parkin=" + parkin +
				", parkout=" + parkout +
				", tag=" + tag +
				", illegalInfo='" + illegalInfo + '\'' +
				", payid=" + payid +
				", pay_money=" + pay_money +
				", pay_type=" + pay_type +
				'}';
	}
}
