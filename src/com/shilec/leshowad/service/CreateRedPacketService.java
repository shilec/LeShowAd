package com.shilec.leshowad.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.utils.Contacts;

/**
 * 创建红包接口
 */
@WebServlet("/create_red_packet")
public class CreateRedPacketService extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost1(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		resp.getWriter().write("login:" + req.getSession().
				getAttribute(Contacts.SESSION_USER_KEY).toString());
	}
}
