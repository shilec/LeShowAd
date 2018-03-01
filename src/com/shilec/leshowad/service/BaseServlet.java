package com.shilec.leshowad.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;

import net.sf.json.JSONObject;

public abstract class BaseServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//所有链接都需要为POST
		resp.sendError(405);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Log.i2file(req.toString());
		
		HttpSession session = req.getSession();
		Object userInfo = session.getAttribute(Contacts.SESSION_USER_KEY);
		
		WebServlet annotation = getClass().getAnnotation(WebServlet.class);
		Log.debug("ano = " + annotation.value()[0]);
		
		if(userInfo == null && !annotation.value()[0].equals("/login")) {
			JSONObject jObject = new JSONObject();
			jObject.put("code", Contacts.RESPONSE_CODE.NOT_LOGIN);
			resp.getWriter().write(jObject.toString());
			return;
		}
		
		doPost1(req, resp);
	}
	
	protected abstract void doPost1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
