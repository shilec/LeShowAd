package com.shilec.leshowad.moudle;

import com.shilec.leshowad.dao.AdTemplateDao;
import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.dao.anno.Id;
import com.shilec.leshowad.dao.anno.Table;

@Dao(AdTemplateDao.class)
@Table("t_ad_template")
public class AdTemplate {
	
	@Id
	int id;
	
	String title;
	
	String ad_desc;
	
	String images;
	
	String bk_music;
	
	String html_templete;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getBk_music() {
		return bk_music;
	}

	public void setBk_music(String bk_music) {
		this.bk_music = bk_music;
	}

	public String getHtml_templete() {
		return html_templete;
	}

	public void setHtml_templete(String html_templete) {
		this.html_templete = html_templete;
	}

	@Override
	public String toString() {
		return "AdTemplate [id=" + id + ", title=" + title + ", desc=" + ad_desc + ", images=" + images + ", bk_music="
				+ bk_music + ", html_templete=" + html_templete + "]";
	}

	public String getAd_desc() {
		return ad_desc;
	}

	public void setAd_desc(String ad_desc) {
		this.ad_desc = ad_desc;
	}
	
}
