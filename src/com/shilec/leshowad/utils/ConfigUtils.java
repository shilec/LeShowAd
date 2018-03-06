package com.shilec.leshowad.utils;

import java.io.File;

import net.sf.json.JSONObject;

public class ConfigUtils {
	
	private final static String CONFIG_PATH = "config" + File.separator + "config.json";
	
	private static String mBasePath;
	
	
	private static String CONFIG_REAL_PATH;
	
	public static void test() {
		CONFIG_REAL_PATH = CONFIG_PATH;
	}
	
	public static void init(String basePath) {
		mBasePath = basePath;
		CONFIG_REAL_PATH = mBasePath + "WEB-INF" + File.separator + CONFIG_PATH;
	}
	
	public static String get(String k) {
		byte[] read = FileUtils.read(CONFIG_REAL_PATH);
		String json = new String(read);
		
		JSONObject jObj = JSONObject.fromObject(json);
		String value = jObj.optString(k);
		return value;
	}
	
	public static void set(String k,String v) {
		byte[] read = FileUtils.read(CONFIG_REAL_PATH);
		String json = new String(read);
		
		JSONObject jObj = JSONObject.fromObject(json);
		if(jObj.containsKey(k)) {
			jObj.replace(k, v);
		} else {
			jObj.put(k, v);
		}
		json = jObj.toString();
		FileUtils.write(json.getBytes(), CONFIG_REAL_PATH);
	}

	public static String getRootPath() {
		// TODO Auto-generated method stub
		return mBasePath;
	}
	
	public static String getUploadFileRoot() {
		String uploadRootPath = new File(mBasePath).getParent() + File.separator + "upload";
		File file = new File(uploadRootPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		return uploadRootPath;
	}
}
