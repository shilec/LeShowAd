package com.shilec.leshowad.moudle;

import com.shilec.leshowad.dao.UserDao;
import com.shilec.leshowad.dao.anno.*;

@Table("t_user")
@Dao(UserDao.class)
public class UserInfo {
	
	String wx_id;
	
	String wx_login_code;
	
	String wx_user_name;
	
	String wx_location;
	
	String wx_head_icon;
	
	float balance;
	
	@Id
	int id;

	public String getWx_id() {
		return wx_id;
	}

	public void setWx_id(String wx_id) {
		this.wx_id = wx_id;
	}

	public String getWx_user_name() {
		return wx_user_name;
	}

	public void setWx_user_name(String wx_user_name) {
		this.wx_user_name = wx_user_name;
	}

	public String getWx_location() {
		return wx_location;
	}

	public void setWx_location(String wx_location) {
		this.wx_location = wx_location;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public String getWx_login_code() {
		return wx_login_code;
	}

	public void setWx_login_code(String wx_login_code) {
		this.wx_login_code = wx_login_code;
	}

	public String getWx_head_icon() {
		return wx_head_icon;
	}

	public void setWx_head_icon(String wx_head_icon) {
		this.wx_head_icon = wx_head_icon;
	}

	@Override
	public String toString() {
		return "UserInfo [wx_id=" + wx_id + ", wx_login_code=" + wx_login_code + ", wx_user_name=" + wx_user_name
				+ ", wx_location=" + wx_location + ", wx_head_icon=" + wx_head_icon + ", balance=" + balance + ", id="
				+ id + "]";
	}	
	
	
}
