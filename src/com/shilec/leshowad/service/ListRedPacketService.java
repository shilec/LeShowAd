package com.shilec.leshowad.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.RedPacket;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.TextUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * order_by: 默认为 0 0: money 1: count 2: date
 * 
 * location_limit: 默认为 null null 不限制
 * 
 * count: 0 全部，其他具体条数
 * 
 * user_type: 0 all, 1 user
 */
@WebServlet("/list_red_packet")
public class ListRedPacketService extends BaseServlet {

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Object object = req.getAttribute("location_limit");
		String location_limit = null;
		if (object != null) {
			location_limit = object.toString();
		}

		if (req.getAttribute("order_by") == null) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.MISS_PARAM);
			jObj.put("msg", "miss order_by param!");
			resp.getWriter().write(jObj.toString());
			return;
		}

		if (req.getAttribute("user_type") == null) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.MISS_PARAM);
			jObj.put("msg", "miss type param!");
			resp.getWriter().write(jObj.toString());
			return;
		}

		int order_by = Integer.parseInt(req.getAttribute("order_by").toString());
		int type = Integer.parseInt(req.getAttribute("user_type").toString());

		// 获取wx_id
		String wx_login_code = req.getAttribute("wx_login_code").toString();
		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		UserInfo userInfo = helper.load("wx_login_code = '" + wx_login_code + "'");
		String wx_id = userInfo.getWx_id();

		IDatabaseHelper<RedPacket> redPacketDao = MySqlManager.getInstance().getHelper(RedPacket.class);
		
		String where = null;
		//只取当前的红包
		if (type == 1) {
			where = "wx_id = '" + wx_id + "'";
		}
		if (!TextUtils.isEmpty(location_limit)) {
			if(where != null) {
				where += " and ";
			}
			where += "locations = '" + location_limit + "'";
		}

		String order = "";
		switch (order_by) {
		case 0:
			order = "by all_money desc";
			break;
		case 1:
			order = "by packet_count desc";
			break;
		case 2:
			order = "by create_date desc";
			break;
		default:
			order = null;
		}
		List<RedPacket> loadSome = redPacketDao.loadSome(where, order);
		resp.getWriter().write(createResJson(loadSome));
	}

	private String createResJson(List<RedPacket> list) {
		JSONArray jsonArray = new JSONArray();
		if(list != null && !list.isEmpty())
		for(RedPacket redPacket : list) {
			JSONObject jObj = JSONObject.fromObject(redPacket);
			jObj.remove("wx_id");
			jsonArray.add(jObj);
		}
		
		return jsonArray.toString();
	}
}
