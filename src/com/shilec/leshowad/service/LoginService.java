package com.shilec.leshowad.service;

import java.awt.JobAttributes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
import com.shilec.leshowad.utils.TextUtils;

import net.sf.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 登录接口
 */
@WebServlet({"/login","/logout"})
public class LoginService extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		//读取POST 参数
		ServletInputStream inputStream = req.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		String line = reader.readLine();
		Log.i2file("POST PARAM === " + line);
		
		//读取参数
		JSONObject jObject = JSONObject.fromObject(line);
		String wx_login_code = jObject.optString("wx_login_code");
		String wx_user_name = jObject.optString("wx_user_name");
		String wx_location = jObject.optString("wx_location");
		String wx_head_icon = jObject.optString("wx_head_icon");
		
		//检查
		if(TextUtils.isEmpty(wx_login_code) || TextUtils.isEmpty(wx_user_name) 
				|| TextUtils.isEmpty(wx_location) || TextUtils.isEmpty(wx_head_icon)) {
			
			String msg = TextUtils.isEmpty(wx_login_code) ? "miss wx_id" :
				TextUtils.isEmpty(wx_location) ? "miss wx_location" :
					TextUtils.isEmpty(wx_user_name) ? "miss wx_user_name" : "miss wx_head_icon";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", Contacts.RESPONSE_CODE.MISS_PARAM);
			jsonObject.put("msg", msg);
			resp.getWriter().write(jsonObject.toString());
			return;
		}
 		
		UserInfo info = new UserInfo();
		info.setWx_location(wx_location);
		info.setWx_user_name(wx_user_name);
		info.setWx_login_code(wx_login_code);
		info.setWx_head_icon(wx_head_icon);
		
		Log.debug("info = " + info);
		Log.debug("req = " + req.getServletPath());
		if(req.getServletPath().equals("/login")) {
			login(info, wx_login_code,resp, req);
		} else {
			logout(req, resp);
		}
	}
	
	//登录
	private void login(UserInfo info,String wx_login_code,HttpServletResponse resp,HttpServletRequest req) 
			throws IOException {
		String openId = getWxOpenId(wx_login_code);
		if(!TextUtils.isEmpty(openId)) {
			info.setWx_id(openId);
			saveInfo(info, resp, req);
		} else {
			JSONObject object = new JSONObject();
			object.put("code",Contacts.RESPONSE_CODE.WX_LOGIN_CODE_INVALID);
			object.put("msg", "wx_login_code_invalid!");
			resp.getWriter().write(object.toString());
		}
	}
	
	//保存用户信息到数据库和session
	private void saveInfo(UserInfo info,HttpServletResponse resp,
			HttpServletRequest req) throws IOException {
		//req.getSession().setAttribute(Contacts.SESSION_USER_KEY, info);
		//首次登录，保存到数据库
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		UserInfo load = helper.load("wx_id='" + info.getWx_id() + "'");
		Log.debug("exist user = " + load);
		if(TextUtils.isEmpty(load.getWx_id())) {
			helper.add(info);
		} else if(!info.getWx_login_code().equals(load.getWx_login_code())){
			helper.update(info, new String[] {"wx_login_code"}, "wx_id = '" + info.getWx_id() + "'");
		}
		
		//返回成功
		JSONObject jObject = new JSONObject();
		jObject.put("code", Contacts.RESPONSE_CODE.OK);
		resp.getWriter().write(jObject.toString());
		
		Log.i2file("login ==== \n" + info.toString());
	}
	
	//通过login_code 获取 openid
	private String getWxOpenId(String wx_login_code) throws IOException {
		
		String appid = ConfigUtils.get(Contacts.MINI_PROGRAM_APPID);
		String appsecret = ConfigUtils.get(Contacts.MINI_PROGRAM_SECRET);
		
		OkHttpClient client = new OkHttpClient();
		//'https://api.weixin.qq.com/sns/jscode2session?appid=' + appid +
		//'&secret=' + appsecret + '&grant_type=authorization_code&js_code=' + res.code,
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + 
				"&secret=" + appsecret + "&grant_type=authorization_code&js_code=" + wx_login_code;
		Log.debug("request url = " + url);
		Request request = new Request.Builder()
				.addHeader("content-type", "application/json")
				.url(url)
				.build();
		Call newCall = client.newCall(request);
		Response response = newCall.execute();
		
		if(response.isSuccessful()) {
			JSONObject jObject = JSONObject.fromObject(response.body().string());
			return jObject.optString("openid");
		} else {
			return null;
		}
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
