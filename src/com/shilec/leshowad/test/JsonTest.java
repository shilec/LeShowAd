package com.shilec.leshowad.test;

import java.lang.reflect.Field;

import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Log;

public class JsonTest {
	public static void main(String[] args) {
		String string = ConfigUtils.get("user");
		System.out.println(string);
		
		ConfigUtils.set("add", "345");
		
		Log.i2file("123");
		Field field = test2.class.getDeclaredFields()[0];
	}
	
	private static class test2 {
		
		private int i;
	}
}
