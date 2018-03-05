package com.shilec.leshowad.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;
import com.shilec.leshowad.utils.TextUtils;

import net.sf.json.JSONObject;

public abstract class BaseServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//�������Ӷ���ҪΪPOST
		resp.sendError(405);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Log.i2file(req.toString());

		WebServlet annotation = getClass().getAnnotation(WebServlet.class);
		//���login/logout/upload ����֤wx_login_code
		Log.debug("ano = " + annotation.value()[0]);
		if (annotation.value()[0].equals("/login") || 
				annotation.value()[0].equals("/upload")) {
			doPost1(req, resp);
			return;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
		String line = reader.readLine();
		String wx_login_code;
		try {
			JSONObject jObject = JSONObject.fromObject(line);
			wx_login_code = jObject.optString("wx_login_code");
		} catch (Exception e) {
			wx_login_code = null;
		}

		// ���˵�¼�ӿڣ������ӿ�û��¼ ���ʷ���NOT_LOGIN
		if (wx_login_code == null) {
			JSONObject jObject = new JSONObject();
			jObject.put("code", Contacts.RESPONSE_CODE.NOT_LOGIN);
			resp.getWriter().write(jObject.toString());
			return;
		} else {
			// ������ǵ�¼���������� wx_login_code ���գ� ���ж����ݿ��� wx_login_code �Ƿ�ע��
			// �Ƿ����wx_login_code ��Ӧ��UserInfo,�� wx_id ��Ϊ��
			boolean isLogin = checkLoginCodeExpire(wx_login_code);
			if (!isLogin) {
				JSONObject jObject = new JSONObject();
				jObject.put("code", Contacts.RESPONSE_CODE.NOT_LOGIN);
				resp.getWriter().write(jObject.toString());
				return;
			}
			req.getSession().setAttribute("wx_login_code", wx_login_code);
		}

		doPost1(req, resp);
	}
	
	/**
	 * @param wx_login_code
	 * @return 	 *  ������ݿ��д���wx_login_code 
	 * ��Ӧ��UserInfo ���� UserInfo �� wx_id ��Ϊ�� ���� true, ���� false
	 */
	protected boolean checkLoginCodeExpire(String wx_login_code) {
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		List<UserInfo> loadSome = helper.loadSome("wx_login_code = '" + wx_login_code + "'");
		
		if(loadSome == null || loadSome.isEmpty()) {
			return false;
		}
		
		UserInfo info = loadSome.get(0);
		if(TextUtils.isEmpty(info.getWx_id())) {
			return false;
		} else {
			return true;
		}
	}	
	
	protected abstract void doPost1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
