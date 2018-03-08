package com.shilec.leshowad.service;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.RED_PACKET_MODE;
import com.shilec.leshowad.moudle.RedPacket;
import com.shilec.leshowad.moudle.ShareMap;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.Contacts;
import com.shilec.leshowad.utils.Log;
import com.sun.javafx.collections.MappingChange.Map;

import net.sf.json.JSONObject;

/**
 * red_packet_id: ���id
 * location: wx_user_location
 */
@WebServlet("/open_red_packet")
public class OpenRedPacketService extends BaseServlet{

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		if(req.getAttribute("red_packet_id") == null) {
			JSONObject jObj = new JSONObject();
			jObj.put("code",Contacts.RESPONSE_CODE.MISS_PARAM);
			jObj.put("msg", "miss red_packet_id !");
			resp.getWriter().write(jObj.toString());
			return;
		}
		
		if(req.getAttribute("wx_user_location") == null) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.MISS_PARAM);
			jObj.put("msg", "miss wx_user_location");
			resp.getWriter().write(jObj.toString());
			return;
		}
		
		String wx_login_code = req.getAttribute("wx_login_code").toString();
		String wx_red_packet_id = req.getAttribute("red_packet_id").toString();
		String wx_user_location = req.getAttribute("wx_user_location").toString();
		openRedPacket(wx_login_code, wx_red_packet_id, wx_user_location,resp);
	}

	private void openRedPacket(String wx_login_code,String wx_red_packet_id
			,String wx_user_location,HttpServletResponse resp)throws ServletException, IOException {
		IDatabaseHelper<UserInfo> userDao = MySqlManager.getInstance().getHelper(UserInfo.class);
		UserInfo userInfo = userDao.load("wx_login_code = '" + wx_login_code + "'");
		
		IDatabaseHelper<RedPacket> redPacketDao = MySqlManager.getInstance().getHelper(RedPacket.class);
		RedPacket redPacket = redPacketDao.load("id = '" + wx_red_packet_id + "'");
		if(redPacket.isIs_expire() || redPacket.getLeave_count() == 0 || redPacket.getLeave_money() == 0) {
			//��׹��ڣ�����ѷ���
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.RED_PACKET_EXPIER);
			jObj.put("msg", "red packet expire");
			resp.getWriter().write(jObj.toString());
			return;
		}
		
		//�����ǰҪ��ȡ�ĺ���޶����򣬲��ҵ�ǰ�û���λ�ò����޶�������
		if(redPacket.getLocation_limit() != null && !isLocationValidable(wx_user_location)) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.OPEN_LOCATION_UNVALID);
			jObj.put("msg", "location is not include in location limit!");
			resp.getWriter().write(jObj.toString());
			return;
		}
		
		//�����ǰ����޶�ʱ�䣬���ҵ�ǰ����ʱ�䲻��������
		if(redPacket.getTime_limit() != null && !isTimeValid()) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.OPEN_TIME_UNVALID);
			jObj.put("msg", "current time is not include in time limit!");
			resp.getWriter().write(jObj.toString());
			return;
		}
		
		//���һ����ȡ�û���Ϣ
		ShareMap shareMap = new ShareMap();
		shareMap.setOpen_count(0);
		shareMap.setRed_packet_id(redPacket.getId());
		shareMap.setShared_wx_id(userInfo.getWx_id());
		shareMap.setDate(System.currentTimeMillis());
		
		//�̶���� �� ������
		float open_money = 0;
		if(RED_PACKET_MODE.MODE_FIXED == redPacket.getIncome_mode()) {
			open_money = redPacket.getAll_money() / redPacket.getPacket_count();
		} else {
			open_money = (float) (0.01f + Math.random() * redPacket.getLeave_money() / 3);
		}
		DecimalFormat df = new DecimalFormat("#.00");
		shareMap.setIncome(Float.parseFloat(df.format(open_money)));
		IDatabaseHelper<ShareMap> shareMapDao = MySqlManager.getInstance().getHelper(ShareMap.class);
		ShareMap load = shareMapDao.load("red_packet_id = '" + shareMap.getRed_packet_id() + "' "
				+ "and shared_wx_id = '" + shareMap.getShared_wx_id() + "'");
		//�����ȡ�����ҵ�ǰ��ȡ����Ϊ0����������ȡ
		if(load != null && load.getOpen_count() == 0) {
			JSONObject jObj = new JSONObject();
			jObj.put("code", Contacts.RESPONSE_CODE.ALREADY_OPEN);
			jObj.put("msg", "this red packet already opened!");
			resp.getWriter().write(jObj.toString());
			Log.debug("load == " + load);
			return;
		}
		
		if(load == null) {
			shareMap.setAleady_open_count(1);
			shareMapDao.add(shareMap);
		} else {
			//���ٵ�ǰ��ȡ���� 
			shareMap.setAleady_open_count(load.getAleady_open_count() + 1);
			shareMap.setOpen_count(load.getOpen_count() - 1);
			shareMapDao.update(shareMap, new String[] {"income","open_count"},"id = '" + load.getId() + "'");
		}
		
		//���º����Ϣ
		redPacket.setLeave_count(redPacket.getLeave_count() - 1);
		redPacket.setLeave_money(redPacket.getLeave_money() - Float.parseFloat(df.format(open_money)));
		redPacketDao.update(redPacket, new String[] {"leave_count","leave_money"},
				"id = '" + redPacket.getId() + "'");
		sendResult(redPacket,shareMap,resp);
	}
	
	private void sendResult(RedPacket redPacket, ShareMap shareMap, 
			HttpServletResponse resp) throws IOException {
		JSONObject jObj = new JSONObject();
		jObj.put("code", Contacts.RESPONSE_CODE.OK);
		jObj.put("in_come", shareMap.getIncome());
		
		JSONObject redPacketInfo = JSONObject.fromObject(redPacket);
		redPacketInfo.remove("wx_id");
		jObj.put("red_packet_info", redPacketInfo);
		resp.getWriter().write(jObj.toString());
	}

	private boolean isTimeValid() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		return true;
	}
	
	//TODO ����û�λ���Ƿ��ں�����޶�������
	private boolean isLocationValidable(String wx_user_location) {
		return true;
	}
}
