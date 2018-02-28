package com.shilec.leshowad.test;

import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Log;

public class JsonTest {
	public static void main(String[] args) {
		String string = ConfigUtils.get("user");
		System.out.println(string);
		
		ConfigUtils.set("add", "345");
		
		Log.i("123");
	}
}
