package com.shilec.leshowad.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.ConfigUtils;

public class MySqlTest {

	public static void main(String[] args) {
		
//		FieldTest test = new FieldTest();
//		Class<FieldTest> class1 = (Class<FieldTest>) test.getClass();
//		for(Field f:class1.getDeclaredFields()) {
//			System.out.println("name = " + f.getName() + ",type = " + f.getType().getSimpleName());
//		}
		
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		
		UserInfo info = new UserInfo();
		info.setWx_id("是撒大声地");
		info.setBalance(911111119.112f);
		info.setWx_user_name("十离家阿萨德");
		info.setWx_location("adasda");
	
//		List<UserInfo> infos = new ArrayList<>();
//		for(int i = 0; i < 3; i++) {
//			info = new UserInfo();
//			info.setWx_id("21sasasasasas");
//			info.setBalance(99.112f);
//			info.setWx_user_name("shilec");
//			info.setWx_location("6788,990");
//			infos.add(info);
//		}
		
		helper.update(info, new String[] {"wx_user_name","balance"},"id = 403");
		
		List<UserInfo> infos = helper.loadAll();
		System.out.println(infos);
		//helper.delete("id = 1");
		//helper.deleteAll();
	}

}


final class FieldTest {
	String string;
	int n;
	boolean b;
	Integer integer;
	float f;
}