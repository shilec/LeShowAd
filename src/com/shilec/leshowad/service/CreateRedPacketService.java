package com.shilec.leshowad.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.AdTemplate;
import com.shilec.leshowad.moudle.RedPacket;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;

import net.sf.json.JSONObject;

/**
 * 创建红包接口
 */
@WebServlet("/create_red_packet")
public class CreateRedPacketService extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Enumeration<String> names = req.getAttributeNames();
		while(names.hasMoreElements()) {
			String key = names.nextElement();
			Log.debug(key + "=" + req.getAttribute(key));
		}
		
		//取出当前的wx-id
		IDatabaseHelper<UserInfo> userDao = MySqlManager.getInstance().getHelper(UserInfo.class);
		String wx_login_code = req.getAttribute("wx_login_code").toString();
		UserInfo userInfo = userDao.load("wx_login_code = '" + wx_login_code + "'");
		
		//保存红包数据
		IDatabaseHelper<RedPacket> helper = MySqlManager.getInstance().getHelper(RedPacket.class);
		RedPacket redPacket = new RedPacket();
		redPacket.setAll_money(Integer.parseInt(req.getAttribute("money").toString()));
		redPacket.setIncome_mode(Integer.parseInt(req.getAttribute("red_packet_mode").toString()));
		redPacket.setIs_expire(false);
		redPacket.setWx_id(userInfo.getWx_id());
		redPacket.setPacket_count(Integer.parseInt(req.getAttribute("count").toString()));
		redPacket.setLeave_count(redPacket.getPacket_count());
		redPacket.setLeave_money(redPacket.getAll_money());
		if(req.getAttribute("location_limit") != null) {
			redPacket.setLocation_limit(req.getAttribute("location_limit").toString());
		}
		
		if(req.getAttribute("location") != null) {
			redPacket.setLocation(req.getAttribute("location").toString());
		}
		
		if(req.getAttribute("red_packet_desc") != null) {
			redPacket.setAd_desc(req.getAttribute("red_packet_desc").toString());
		}
		
		if(req.getAttribute("time_limit") != null) {
			redPacket.setTime_limit(req.getAttribute("time_limit").toString());
		}
		redPacket.setTitle(req.getAttribute("title").toString());
		redPacket.setImages(req.getAttribute("image_path").toString());
		redPacket.setCreate_date(Long.parseLong(req.getAttribute("create_date").toString()));
		Log.debug("redPakcetInfo == " + redPacket);
		helper.add(redPacket);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", Contacts.RESPONSE_CODE.OK);
		resp.getWriter().write(jsonObject.toString());
	}
}
