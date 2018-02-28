package com.shilec.leshowad.test;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateTest {

	public static void main(String[] args) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh");
		System.out.println(format.format(date));
	}
}
