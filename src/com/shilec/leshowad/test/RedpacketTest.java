package com.shilec.leshowad.test;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.RedPacket;
import com.shilec.leshowad.utils.ConfigUtils;

public class RedpacketTest {

	public static void main(String[] args) {
		ConfigUtils.test();
		
		IDatabaseHelper<RedPacket> helper = MySqlManager.getInstance().getHelper(RedPacket.class);
		RedPacket red = new RedPacket();
		red.setAll_money(1232132.1f);
		red.setAd_id(21);
		red.setIncome_mode(3);
		red.setIs_expire(false);
		red.setPacket_count(1000);
		
		helper.add(red);
	}

}
