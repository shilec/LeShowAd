package com.shilec.leshowad.utils;

import java.io.File;

import net.sf.json.JSONObject;

public class ConfigUtils {
	
	private final static String CONFIG_PATH = "config" + File.separator + "config.json";
	
	private static String mBasePath;
	
	private static String CONFIG_READL_PATH;
	
	public static void init(String basePath) {
		mBasePath = basePath;
		CONFIG_READL_PATH = mBasePath + "WEB-INF" + File.separator + CONFIG_PATH; 
	}
	
	public static String get(String k) {
		byte[] read = FileUtils.read(CONFIG_READL_PATH);
		String json = new String(read);
		
		JSONObject jObj = JSONObject.fromObject(json);
		String value = jObj.optString(k);
		return value;
	}
	
	public static void set(String k,String v) {
		byte[] read = FileUtils.read(CONFIG_READL_PATH);
		String json = new String(read);
		
		JSONObject jObj = JSONObject.fromObject(json);
		if(jObj.containsKey(k)) {
			jObj.replace(k, v);
		} else {
			jObj.put(k, v);
		}
		json = jObj.toString();
		FileUtils.write(json.getBytes(), CONFIG_READL_PATH);
	}
}
