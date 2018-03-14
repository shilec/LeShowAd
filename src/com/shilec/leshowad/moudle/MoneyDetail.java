package com.shilec.leshowad.moudle;

import com.shilec.leshowad.dao.MoneyDetailDao;
import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.dao.anno.Id;
import com.shilec.leshowad.dao.anno.Table;

@Dao(MoneyDetailDao.class)
@Table("t_money_detail")
public class MoneyDetail {
	
	@Id
	private int id;
	
	private float income; //收入
	
	private float expenditure; //支出
	
	private float withdrawals; //提现的金额
	
	private String wx_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getIncome() {
		return income;
	}

	public void setIncome(float income) {
		this.income = income;
	}

	public float getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(float expenditure) {
		this.expenditure = expenditure;
	}

	public float getWithdrawals() {
		return withdrawals;
	}

	public void setWithdrawals(float withdrawals) {
		this.withdrawals = withdrawals;
	}

	public String getWx_id() {
		return wx_id;
	}

	public void setWx_id(String wx_id) {
		this.wx_id = wx_id;
	}

	@Override
	public String toString() {
		return "MoneyDetail [id=" + id + ", income=" + income + ", expenditure=" + expenditure + ", withdrawals="
				+ withdrawals + ", wx_id=" + wx_id + "]";
	}
	
	
}
