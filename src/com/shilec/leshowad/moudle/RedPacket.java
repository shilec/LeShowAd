package com.shilec.leshowad.moudle;

import java.util.List;

import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.dao.anno.Id;
import com.shilec.leshowad.dao.anno.Table;
import com.shilec.leshowad.dao.helper.RedPacketDao;

@Table("t_red_packet")
@Dao(RedPacketDao.class)
public class RedPacket {
	String locations; //��Χ
	
	float all_money; //�ܽ��
	
	int packet_count; //�ܸ���
	
	int income_mode; //����ģʽ
	
	int leave_count; //ʣ�����
	
	float leave_money; //ʣ����
	
	int ad_id; //���ģ��id
	
	boolean is_expire; //�Ƿ����
	
	@Id
	int id; //id
	
	String wx_id;

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public float getAll_money() {
		return all_money;
	}

	public void setAll_money(float all_money) {
		this.all_money = all_money;
	}

	public int getPacket_count() {
		return packet_count;
	}

	public void setPacket_count(int packet_count) {
		this.packet_count = packet_count;
	}

	public int getIncome_mode() {
		return income_mode;
	}

	public void setIncome_mode(int income_mode) {
		this.income_mode = income_mode;
	}

	public int getLeave_count() {
		return leave_count;
	}

	public void setLeave_count(int leave_count) {
		this.leave_count = leave_count;
	}

	public float getLeave_money() {
		return leave_money;
	}

	public void setLeave_money(float leave_money) {
		this.leave_money = leave_money;
	}

	public int getAd_id() {
		return ad_id;
	}

	public void setAd_id(int ad_id) {
		this.ad_id = ad_id;
	}

	public boolean isIs_expire() {
		return is_expire;
	}

	public void setIs_expire(boolean is_expire) {
		this.is_expire = is_expire;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWx_id() {
		return wx_id;
	}

	public void setWx_id(String wx_id) {
		this.wx_id = wx_id;
	}
	
	
}
