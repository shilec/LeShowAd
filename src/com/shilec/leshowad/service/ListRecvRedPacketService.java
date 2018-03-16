package com.shilec.leshowad.service;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils.Null;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.RedPacket;
import com.shilec.leshowad.moudle.ShareMap;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;
import com.shilec.leshowad.utils.TextUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 获取收到的红包列表
 * 
 * wx_login_code: wx user login code 
 * user_type: 0 all, 1 current user
 * red_packet_type: 0 all, 1 
 * red_packet_id 对应的 红包 red_packet_id: null all,
 * target red_packet
 * order_type: 0:money 1:count 2:date
 */
@WebServlet("/list_recv_redpacket")
public class ListRecvRedPacketService extends BaseServlet {

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		JSONObject error = new JSONObject();
		error.put("code", Contacts.RESPONSE_CODE.MISS_PARAM);
		if (req.getAttribute("user_type") == null) {
			error.put("msg", "miss user_type param!");
			resp.getWriter().write(error.toString());
			return;
		}

		if (req.getAttribute("red_packet_type") == null) {
			error.put("msg", "miss red_packet_type param!");
			resp.getWriter().write(error.toString());
			return;
		}
		
		if (req.getAttribute("order_by") == null) {
			error.put("msg", "miss order_by param!");
			resp.getWriter().write(error.toString());
			return;
		}

		// 如果当前 red_packet_type == 1 那就必选指定 red_packet_id
		if (req.getAttribute("red_packet_type") != null
				&& 1 == Integer.parseInt(req.getAttribute("red_packet_type").toString())
				&& req.getAttribute("red_packet_id") == null) {
			error.put("msg", "miss red_packet_id param!");
			resp.getWriter().write(error.toString());
			return;
		}
		

		int user_type = Integer.parseInt(req.getAttribute("user_type").toString());
		int red_packet_type = Integer.parseInt(req.getAttribute("red_packet_type").toString());
		String red_packet_id = req.getAttribute("red_packet_id") == null ? ""
				: req.getAttribute("red_packet_id").toString();
		String wx_login_code = req.getAttribute("wx_login_code").toString();
		int order_by = Integer.parseInt(req.getAttribute("order_by").toString());
		
		listShareMaps(wx_login_code,user_type, red_packet_type,red_packet_id,order_by,resp);
	}

	private void listShareMaps(String wx_login_code,int user_type, int red_packet_type,
			String red_packet_id,int order_by,HttpServletResponse resp) throws IOException {
		JSONObject jObjet = new JSONObject();
		jObjet.put("code", Contacts.RESPONSE_CODE.OK);
		
		IDatabaseHelper<ShareMap> shareMapDao = MySqlManager.getInstance().getHelper(ShareMap.class);
		
		IDatabaseHelper<UserInfo> userDao = MySqlManager.getInstance().getHelper(UserInfo.class);
		UserInfo userInfo = userDao.load("wx_login_code = '" + wx_login_code + "'");
		
		String where = null;
		//all all
		if(user_type == 0 && red_packet_type == 0) {
			where = "";
		} else if(user_type == 0 && red_packet_type != 0) {
			where = "red_packet_id = '" + red_packet_id + "'";
		} else if(user_type == 1 && red_packet_type == 0) {
			where = "shared_wx_id = '" + userInfo.getWx_id() + "'";
		} else if(user_type == 1 && red_packet_type == 1) {
			where = "shared_wx_id = '" + userInfo.getWx_id() + "' and red_packet_id = '" + red_packet_id + "'";
		}
		//加载红包记录表
		List<ShareMap> shareMaps = shareMapDao.loadSome(where, null);
		
		//加载红包记录表对应的用户
		List<UserInfo> listUserInfos = listUserInfo(userDao,shareMaps,order_by);
		List<Map<String, Object>> res = new ArrayList<>();
		Log.debug("user info size = " + listUserInfos.size());
		for(UserInfo info : listUserInfos) {
			Map<String, Object> item = null;
			for(int i = 0; i < res.size(); i++) {
				Map<String, Object> map = res.get(i);
				if(map.get("user_info").equals(info)) {
					item = map;
					break;
				}
			}
			
			if(item == null) {
				item = new HashMap<>();
				item.put("user_info", info);
				item.put("history", new ArrayList<>());
				res.add(item);
			}
			
			List<Object> history = (List<Object>) item.get("history");
			Log.debug("share map size =======" + shareMaps.size());
			for(int i = 0; i < shareMaps.size(); i++) {
				if(TextUtils.equals(info.getWx_id(), shareMaps.get(i).getShared_wx_id())) {
					history.add(shareMaps.get(i));
				}
			}
		}
		
		JSONArray jsonArray = JSONArray.fromObject(res);
		Log.debug("ret ===== " + res.toString());
		jObjet.put("list", jsonArray);
		resp.getWriter().write(jObjet.toString());
	}
	
	private List<UserInfo> listUserInfo(IDatabaseHelper<UserInfo> userDao,List<ShareMap> shareMaps,int order_by) {
		List<UserInfo> userInfos = new ArrayList<>();
		for(ShareMap map : shareMaps) {
			UserInfo userInfo = userDao.load("wx_id = '" + map.getShared_wx_id() + "'");
			if(!userInfos.contains(userInfo)) {
				userInfos.add(userInfo);
			}
		}
		return userInfos;
	}
	
}
