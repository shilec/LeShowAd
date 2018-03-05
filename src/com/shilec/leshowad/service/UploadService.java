package com.shilec.leshowad.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;

import net.sf.json.JSONObject;

@WebServlet("/upload")
public class UploadService extends BaseServlet{

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String wx_login_code = req.getParameter("wx_login_code");
		boolean isLogin = checkLoginCodeExpire(wx_login_code);
		
		if(!isLogin) {
			JSONObject jObject = new JSONObject();
			jObject.put("code", Contacts.RESPONSE_CODE.NOT_LOGIN);
			return;
		}
		
		Log.debug("upload wx_login_code = " + wx_login_code);
	}

}
