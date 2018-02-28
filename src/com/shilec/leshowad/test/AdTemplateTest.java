package com.shilec.leshowad.test;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.AdTemplate;
import com.shilec.leshowad.utils.ConfigUtils;

public class AdTemplateTest {
	public static void main(String[] args) {
		
		ConfigUtils.test();
		
		AdTemplate adTemplate = new AdTemplate();
		adTemplate.setBk_music("http://localhost/music/1.mp3");
		adTemplate.setAd_desc("这是一个广告描述");
		adTemplate.setHtml_templete("http://localhost/html/32424.html");
		adTemplate.setImages("http://localhost/images/1.jpg,http://localhost/images/1.jpg");
		adTemplate.setTitle("这是广告");
		
		IDatabaseHelper<AdTemplate> helper = MySqlManager.getInstance().getHelper(AdTemplate.class);
		helper.add(adTemplate);
		
		System.out.println(helper.loadAll().toString());
	}
}
