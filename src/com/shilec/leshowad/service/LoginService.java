package com.shilec.leshowad.service;

import java.awt.JobAttributes;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;

import net.sf.json.JSONObject;

/**
 * 登录接口
 */
@WebServlet({"/login","/logout"})
public class LoginService extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		
		String wx_id = req.getParameter("wx_id");
		String wx_user_name = req.getParameter("wx_user_name");
		String wx_location = req.getParameter("wx_location");
		float balance = 0.0f;
 		
		UserInfo info = new UserInfo();
		info.setBalance(balance);
		info.setWx_id(wx_id);
		info.setWx_location(wx_location);
		info.setWx_user_name(wx_user_name);
		
		Log.debug("req = " + req.getServletPath());
		if(req.getServletPath().equals("/login")) {
			login(info, resp, req);
		} else {
			logout(req, resp);
		}
	}
	
	//登录
	private void login(UserInfo info,HttpServletResponse resp,HttpServletRequest req) 
			throws IOException {
		
		req.getSession().setAttribute(Contacts.SESSION_USER_KEY, info);
		
		//首次登录，保存到数据库
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		UserInfo load = helper.load("wx_id='" + info.getWx_id() + "'");
		if(load == null) {
			helper.add(info);
		}
		
		JSONObject jObject = new JSONObject();
		jObject.put("code", Contacts.RESPONSE_CODE.OK);
		resp.getWriter().write(jObject.toString());
		
		Log.i2file("login ==== \n" + info.toString());
	}
	
	//登出
	private void logout(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException{
		HttpSession session = req.getSession();
		UserInfo sessionUser = (UserInfo) session.getAttribute(Contacts.SESSION_USER_KEY);
		if(sessionUser != null) {
			 session.removeAttribute(Contacts.SESSION_USER_KEY);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", Contacts.RESPONSE_CODE.OK);
		resp.getWriter().write(jsonObject.toString());
	}
}
