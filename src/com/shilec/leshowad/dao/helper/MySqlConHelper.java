package com.shilec.leshowad.dao.helper;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Log;

public class MySqlConHelper {
	
	private String mUserName;
	
	private String mPsw;
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	
	private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	
	private static final String DB_NAME = "db_program";
	
	private Connection mConn;
	
	private MySqlConHelper() {
		initConfig();
	}
	
	private void initConnection() {
		try {
			Class.forName(DRIVER_NAME);
			mConn = DriverManager.getConnection(DB_URL + DB_NAME, mUserName,mPsw);
			if(mConn.isClosed()) {
				Log.i("db connection create error!");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	Connection getConnection() {
		initConnection();
		return mConn;
	}

	private void initConfig() {
		mUserName = ConfigUtils.get("user");
		mPsw = ConfigUtils.get("passwd");
		
		if(mUserName == null || mPsw == null) {
			throw new RuntimeException("can not load user and passwd from config.json!");
		} 
	}

	private static MySqlConHelper sInstance = new MySqlConHelper();
	
	static MySqlConHelper getInstance() {
		return sInstance;
	}
}
