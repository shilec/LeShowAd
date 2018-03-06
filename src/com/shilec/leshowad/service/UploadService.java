package com.shilec.leshowad.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;
import com.shilec.leshowad.utils.TextUtils;
import com.sun.glass.ui.CommonDialogs.Type;

import net.sf.json.JSONObject;

@WebServlet("/upload")
public class UploadService extends BaseServlet {

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String wx_login_code = req.getParameter("wx_login_code");
		String file_name = req.getParameter("file_name");
		boolean isLogin = checkLoginCodeExpire(wx_login_code);

		if (!isLogin) {
			JSONObject jObject = new JSONObject();
			jObject.put("code", Contacts.RESPONSE_CODE.NOT_LOGIN);
			return;
		}

		// ªÒ»°bundary
		Log.debug("content-type == " + req.getHeader("Content-Type"));
		String content_type = req.getHeader("Content-Type");
		String bunaray = content_type.split(";")[1].split("=")[1].trim();
		Log.debug("bundaray = " + bunaray);

		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		UserInfo userInfo = helper.load("wx_login_code = '" + wx_login_code + "'");

		String saveImagFile;
		try {
			saveImagFile = saveImagFile(file_name, bunaray, userInfo.getWx_id(), req);
		} catch (Exception e) {
			saveImagFile = null;
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(saveImagFile)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", Contacts.RESPONSE_CODE.OK);
			jsonObject.put("file_name", saveImagFile);
			resp.getWriter().write(jsonObject.toString());
		} else {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", Contacts.RESPONSE_CODE.UPLOAD_FILE_ERROR);
			resp.getWriter().write(jsonObject.toString());
		}
	}

	private String saveImagFile(String fileName, String bundary, String wx_id, HttpServletRequest req)
			throws Exception {
	
		File file = new File(ConfigUtils.getUploadFileRoot());
		if (!file.exists()) {
			file.mkdirs();
		}
		//
		file = new File(file.getAbsolutePath() + File.separator + wx_id + "_" + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		Map<String, List<FileItem>> map = fileUpload.parseParameterMap(req);
		List<FileItem> list = map.get("img");
		FileItem fileItem = list.get(0);

		byte[] buf = new byte[8 * 1024];
		int len = 0;
		InputStream is = fileItem.getInputStream();

		while ((len = is.read(buf)) != -1) {
			fos.write(buf, 0, len);
		}

		fos.close();
		return file.getName();
	}

}
