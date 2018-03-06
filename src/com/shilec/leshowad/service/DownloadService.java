package com.shilec.leshowad.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Contacts;

import net.sf.json.JSONObject;

@WebServlet("/download")
public class DownloadService extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if(req.getParameter("file_name") == null) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.MISS_PARAM);
			jObj.put("msg","miss file_name !");
			resp.getWriter().write(jObj.toString());
			return;
		}
		
		String file_name = req.getParameter("file_name");
		File file = new File(ConfigUtils.getUploadFileRoot() + File.separator + file_name);
		if(!file.exists()) {
			resp.sendError(404);
			return;
		}
		
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[8 * 1024];
		OutputStream os = resp.getOutputStream();
		int len = 0;
		
		while((len = fis.read(buf)) != -1) {
			os.write(buf,0,len);
			os.flush();
		}
		
		fis.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}
}
