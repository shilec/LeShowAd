package com.shilec.leshowad.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.MoneyDetail;
import com.shilec.leshowad.moudle.RedPacket;
import com.shilec.leshowad.moudle.ShareMap;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.Contacts;

import net.sf.json.JSONObject;

@WebServlet({"/user_info"})
public class UserInfoService extends BaseServlet{

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance()
				.getHelper(UserInfo.class);
		
		String wx_login_code = req.getAttribute("wx_login_code").toString();
		UserInfo userInfo = helper.load("wx_login_code='" + wx_login_code + "'");
		JSONObject jObject = new JSONObject();
		jObject.put("info", userInfo);
		jObject.put("code", Contacts.RESPONSE_CODE.OK);
		
		IDatabaseHelper<MoneyDetail> moneyDao = MySqlManager.getInstance()
				.getHelper(MoneyDetail.class);
		MoneyDetail moneyDetail = moneyDao.load("wx_id='" + userInfo.getWx_id() + "'");
		if(moneyDetail == null) {
			moneyDetail = new MoneyDetail();
			moneyDetail.setWx_id(userInfo.getWx_id());
			moneyDao.add(moneyDetail);
		}
		jObject.put("money_info", moneyDetail);
		resp.getWriter().write(jObject.toString());
	}

}
