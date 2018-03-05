package com.shilec.leshowad.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;
import com.sun.glass.ui.CommonDialogs.Type;


import net.sf.json.JSONObject;

@WebServlet("/upload")
public class UploadService extends BaseServlet{

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String wx_login_code = req.getParameter("wx_login_code");
		String file_name = req.getParameter("file_name");
		boolean isLogin = checkLoginCodeExpire(wx_login_code);
		
		if(!isLogin) {
			JSONObject jObject = new JSONObject();
			jObject.put("code", Contacts.RESPONSE_CODE.NOT_LOGIN);
			return;
		}
		
		//ªÒ»°bundary
		Log.debug("content-type == " + req.getHeader("Content-Type"));
		String content_type = req.getHeader("Content-Type");
		String bunaray = content_type.split(";")[1].split("=")[1].trim();
		Log.debug("bundaray = " + bunaray);
		
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		UserInfo userInfo = helper.load("wx_login_code = '" + wx_login_code + "'");
		
		String saveImagFile = saveImagFile(file_name, bunaray, userInfo.getWx_id(), req);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", Contacts.RESPONSE_CODE.OK);
		jsonObject.put("file_name", saveImagFile);
		resp.getWriter().write(jsonObject.toString());
	}
	
	private String saveImagFile(String fileName,String bundary,String wx_id,HttpServletRequest req) throws UnsupportedEncodingException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(req
				.getInputStream(), "utf-8"));
		String line = "";
		
		File file = new File(ConfigUtils.getRootPath() + fileName + "_" + wx_id);
		BufferedWriter writer = new BufferedWriter(new 
				OutputStreamWriter(new FileOutputStream(file)));
		
		while((line = reader.readLine()) != null) {
			if(line.contains(bundary)) {
				continue;
			}
			writer.write(line);
		}
		
		writer.close();
		reader.close();
		return file.getName();
	}

}
