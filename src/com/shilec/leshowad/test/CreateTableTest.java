package com.shilec.leshowad.test;

import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.dao.anno.Id;
import com.shilec.leshowad.dao.anno.Table;
import com.shilec.leshowad.dao.helper.BaseDatabaseHelper;
import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.utils.ConfigUtils;

public class CreateTableTest {

	public static void main(String[] args) {
		ConfigUtils.test();
		IDatabaseHelper<Bean> helper = MySqlManager.getInstance().getHelper(Bean.class);
	
		Bean bean = new Bean();
		bean.name = "shijiale";
		helper.add(bean);
	}
	
	@Table("t_bean")
	@Dao(BeanDao.class)
	public static class Bean {
		String name;
		
		@Id
		int id;
		
		int age;
		
		boolean sex;
		
		float balance;
		
		long size;
	}
	

	public static class BeanDao extends BaseDatabaseHelper<Bean2>{
		
	}
	
	@Table("t_bean")
	public static class Bean2 {
		@Id
		int id;
	}
}
