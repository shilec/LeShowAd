package com.shilec.leshowad.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.Contacts;

import net.sf.json.JSONObject;

@WebServlet({"/user_info"})
public class UserInfoService extends BaseServlet{

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		if(req.getServletPath().equals("/user_info")) {
			getUserInfo(req, resp);
		}
	}
	
	private void getUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance()
				.getHelper(UserInfo.class);
		
		UserInfo sessionUser = (UserInfo) req.getSession().getAttribute(Contacts.SESSION_USER_KEY);
		UserInfo userInfo = helper.load("wx_id='" + sessionUser.getWx_id() + "'");
		JSONObject jObject = new JSONObject();
		jObject.put("info", userInfo);
		jObject.put("code", Contacts.RESPONSE_CODE.OK);
		resp.getWriter().write(jObject.toString());
	}
}
