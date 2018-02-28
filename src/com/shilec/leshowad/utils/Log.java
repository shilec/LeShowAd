package com.shilec.leshowad.utils;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;


public class Log {
	
	private static ExecutorService mThreadPool = Executors.newSingleThreadExecutor();
	
	private static String LOG_FILE_PATH = "/WEB-INF/logs" ;
	
	private final static Logger sLogger = Logger.getLogger("LeShowAd");
	
	private static final boolean DEBUG = true;
	
	private static String mBasePath;
	
	public static void init(String basePath) {
		mBasePath = basePath;
		File file = new File(mBasePath + LOG_FILE_PATH);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	private static String getMsg(Throwable throwable,String msg,int index) {
		StackTraceElement[] stackTrace = throwable.getStackTrace();
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		StringBuilder stringBuilder = new StringBuilder()
				.append("\n[" + format.format(date) + "]")
				.append("[")
				.append(stackTrace[index].getClassName())
				.append(".")
				.append(stackTrace[index].getMethodName())
				.append(" : ")
				.append(stackTrace[index].getLineNumber())
				.append("]\n")
				.append(msg + "\n\n");
		return stringBuilder.toString();
	}
	
	public static void i(String msg) {
		if(DEBUG) {
			Throwable throwable = new Throwable();
			sLogger.info(getMsg(throwable, msg,1));
			mThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					log2file(getMsg(throwable, msg,1));
				}
			});
		}
	}
	
	private static void log2file(String msg) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh");
		File file = new File(mBasePath + LOG_FILE_PATH + File.separator + format.format(date).toString() + ".log");
		sLogger.info("save logPath = " + file.getAbsolutePath());
		
		FileUtils.write(msg.getBytes(), file.getAbsolutePath(), true);
	}
}
