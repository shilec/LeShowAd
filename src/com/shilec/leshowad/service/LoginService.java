package com.shilec.leshowad.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;
import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.ConfigUtils;

/**
 * Servlet implementation class LoginService
 */
@WebServlet("/LoginService")
public class LoginService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ConfigUtils.init(getServletContext().getRealPath("/"));

		IDatabaseHelper<UserInfo> helper = MySqlManager.getInstance().getHelper(UserInfo.class);
		
		UserInfo info = new UserInfo();
		info.setWx_id("是撒大声地");
		info.setBalance(911111119.112f);
		info.setWx_user_name("十离家阿萨德");
		info.setWx_location("adasda");
		helper.add(info);
		
		List<UserInfo> loadAll = helper.loadAll();
		
		PrintWriter writer = response.getWriter();
		writer.write(loadAll.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
