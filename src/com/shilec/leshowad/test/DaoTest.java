package com.shilec.leshowad.test;

import java.lang.reflect.ParameterizedType;

import com.shilec.leshowad.dao.UserDao;
import com.shilec.leshowad.dao.helper.BaseDatabaseHelper;
import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.ShareMap;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Log;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class DaoTest {

	public static void main(String[] args) {
		
		ConfigUtils.test();
		//IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		IDatabaseHelper<ShareMap> helper = MySqlManager.getInstance().getHelper(ShareMap.class);
		
		ShareMap map = new ShareMap();
		map.setIncome(9999.5f);
		map.setOpen_count(10);
		map.setShared_wx_id("dddd");
		map.setRed_packet_id(121);
		
		//helper.add(map);
		helper.update(map, new String[] {"wx_id"},"id = 1");
		
		System.out.println(helper.loadAll());
		
		IDatabaseHelper<ShareMap> daoHelper = MySqlManager.getInstance().getHelper(ShareMap.class);
		daoHelper.add(map);
		
		System.out.println(daoHelper.loadAll().toString());
	}
	
	private static class Test<T> {
		
		public Test() {
			
		}
	}
	
	private static class Test2 extends Test<String> {
		public Test2() {
			Class<?> clazz = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			System.out.println(clazz.getName());
		}
	}
}
