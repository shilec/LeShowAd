package com.shilec.leshowad.utils;

public class TextUtils {

	
	public static boolean isEmpty(String str) {
		if(str == null || str.equals("")) {
			return true;
		}
		return false;
	}
	
	public static boolean equals(String s1,String s2) {
		if(s1 == s2) {
			return true;
		}
		
		if(s1 != null && s1.equals(s2)) {
			return true;
		}
		
		return false;
	}
}
