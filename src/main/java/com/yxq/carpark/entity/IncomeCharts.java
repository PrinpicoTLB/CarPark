package com.yxq.carpark.entity;

public class IncomeCharts {
	private int weixin;
	private int zhifubao;
	private int cash;
	private int card;
	private int car;

	public int getCar() {
		return car;
	}

	public void setCar(int car) {
		this.car = car;
	}

	public int getWeixin() {
		return weixin;
	}
	public void setWeixin(int weixin) {
		this.weixin = weixin;
	}
	public int getZhifubao() {
		return zhifubao;
	}
	public void setZhifubao(int zhifubao) {
		this.zhifubao = zhifubao;
	}
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}

	@Override
	public String toString() {
		return "IncomeCharts{" +
				"weixin=" + weixin +
				", zhifubao=" + zhifubao +
				", cash=" + cash +
				", card=" + card +
				", car=" + car +
				'}';
	}

	public void setCard(int card) {
        this.card = card;
    }

    public int getCard() {
        return card;
    }
}
