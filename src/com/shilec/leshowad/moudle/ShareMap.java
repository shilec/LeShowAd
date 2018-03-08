package com.shilec.leshowad.moudle;

import java.util.List;

import com.shilec.leshowad.dao.ShareMapDao;
import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.dao.anno.Id;
import com.shilec.leshowad.dao.anno.Table;

/**
 * 分享一次可领取一个红包
 */
@Table("t_share_map")
@Dao(ShareMapDao.class)
public class ShareMap {
	
	String  shared_wx_id;
	
	@Id
	int id;
	
	int red_packet_id; //红包id
	
	float income; //收入
	
	int open_count;
	
	int aleady_open_count;
	
	long date;

	public String getShared_wx_id() {
		return shared_wx_id;
	}

	public void setShared_wx_id(String shared_wx_id) {
		this.shared_wx_id = shared_wx_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRed_packet_id() {
		return red_packet_id;
	}

	public void setRed_packet_id(int red_packet_id) {
		this.red_packet_id = red_packet_id;
	}

	public float getIncome() {
		return income;
	}

	public void setIncome(float income) {
		this.income = income;
	}

	public int getOpen_count() {
		return open_count;
	}

	public void setOpen_count(int open_count) {
		this.open_count = open_count;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getAleady_open_count() {
		return aleady_open_count;
	}

	public void setAleady_open_count(int aleady_open_count) {
		this.aleady_open_count = aleady_open_count;
	}

	@Override
	public String toString() {
		return "ShareMap [shared_wx_id=" + shared_wx_id + ", id=" + id + ", red_packet_id=" + red_packet_id
				+ ", income=" + income + ", open_count=" + open_count + ", aleady_open_count=" + aleady_open_count
				+ ", date=" + date + "]";
	}
	
	
}
