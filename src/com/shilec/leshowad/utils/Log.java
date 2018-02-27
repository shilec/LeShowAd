package com.shilec.leshowad.utils;

import java.util.logging.Logger;

public class Log {
	
	private final static Logger sLogger = Logger.getLogger("LeShowAd");
	
	private static final boolean DEBUG = true;
	
	private static String getMsg(Throwable throwable,String msg) {
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		StringBuilder stringBuilder = new StringBuilder()
				.append("[")
				.append(stackTrace[2].getClassName())
				.append(".")
				.append(stackTrace[2].getMethodName())
				.append(" : ")
				.append(stackTrace[2].getLineNumber())
				.append("]\n")
				.append(msg);
		return stringBuilder.toString();
	}
	
	public static void i(String msg) {
		if(DEBUG) 
		sLogger.info(getMsg(new Throwable(), msg));
	}
	
}
