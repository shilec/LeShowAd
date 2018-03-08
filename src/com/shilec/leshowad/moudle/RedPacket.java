package com.shilec.leshowad.moudle;

import java.util.List;

import com.shilec.leshowad.dao.RedPacketDao;
import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.dao.anno.Id;
import com.shilec.leshowad.dao.anno.Table;

@Table("t_red_packet")
@Dao(RedPacketDao.class)
public class RedPacket {
	String location_limit; //范围
	
	String location;
	
	String time_limit;
	
	float all_money; //总金额
	
	int packet_count; //总个数
	
	int income_mode; //收入模式
	
	int leave_count; //剩余个数
	
	float leave_money; //剩余金额
	
	boolean is_expire; //是否过期
	
	@Id
	int id; //id
	
	String wx_id;
	
	String title;
	
	long create_date;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAd_desc() {
		return ad_desc;
	}

	public void setAd_desc(String ad_desc) {
		this.ad_desc = ad_desc;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	String ad_desc;
	
	String images;

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

	public long getCreate_date() {
		return create_date;
	}

	public void setCreate_date(long create_date) {
		this.create_date = create_date;
	}

	public String getLocation_limit() {
		return location_limit;
	}

	public void setLocation_limit(String location_limit) {
		this.location_limit = location_limit;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	@Override
	public String toString() {
		return "RedPacket [location_limit=" + location_limit + ", location=" + location + ", time_limit=" + time_limit
				+ ", all_money=" + all_money + ", packet_count=" + packet_count + ", income_mode=" + income_mode
				+ ", leave_count=" + leave_count + ", leave_money=" + leave_money + ", is_expire=" + is_expire + ", id="
				+ id + ", wx_id=" + wx_id + ", title=" + title + ", create_date=" + create_date + ", ad_desc=" + ad_desc
				+ ", images=" + images + "]";
	}
}
