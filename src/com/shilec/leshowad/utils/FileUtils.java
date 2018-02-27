package com.shilec.leshowad.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	public static byte[] read(String path) {
		File file = new File(path);
		if(!file.exists()) {
			return null;
		}
		
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			byte[] buf = new byte[8 * 1024];
			int len = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			while((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			
			baos.flush();
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	public static void write(byte[] buf,String path) {
		File file = new File(path);
		if(!file.exists()) {
			return;
		}
		
		OutputStream os =  null;
		try {
			os = new FileOutputStream(file);
			os.write(buf);
			
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os != null) {
				try {
					os.close();
					os = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
