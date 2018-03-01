package com.shilec.leshowad.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

	public static String sha(String orgin,String salt) {
		MessageDigest md = null;
        String strDes = null;
        orgin += salt;
        byte[] bt = orgin.getBytes();
        for(int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (bt[i] << 4 >> 4);
        }
        try {
            md = MessageDigest.getInstance("SHA-256");
            strDes = bytes2Hex(md.digest(bt)); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
	}
	
	 public static String bytes2Hex(byte[] bts) {
	        String des = "";
	        String tmp = null;
	        for (int i = 0; i < bts.length; i++) {
	            tmp = (Integer.toHexString(bts[i] & 0xFF));
	            if (tmp.length() == 1) {
	                des += "0";
	            }
	            des += tmp;
	        }
	        return des;
	    }
}
