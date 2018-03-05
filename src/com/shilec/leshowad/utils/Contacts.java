package com.shilec.leshowad.utils;

public class Contacts {
	
	public static final String SALT_KEY = "shilec_leshowad_salt_123";
	
	public static final String SESSION_USER_KEY = "user";
	
	public static final String MINI_PROGRAM_APPID = "appid_";
	
	public static final String MINI_PROGRAM_SECRET = "secret_";
	
	public static final String WX_LOGIN_CODE = "wx_login_code";
	
	
	public static interface RESPONSE_CODE {
		int NOT_LOGIN = 10000;
		int OK = 0;
		int MISS_PARAM = 10001;
		int WX_LOGIN_CODE_INVALID = 10002;
		int UPLOAD_FILE_ERROR = 10003;
	}
}
