package com.shilec.leshowad.service.listenner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Log;

/**
 * AppContext 监听器，初始化配置
 *
 */
@WebListener
public class AppServletContextListenner implements ServletContextListener {

    public AppServletContextListenner() {
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	//初始化配置
    	ConfigUtils.init(arg0.getServletContext().getRealPath("/"));
    	//初始化日志
    	Log.init(arg0.getServletContext().getRealPath("/"));
    	//数据库初始化，数据表的创建等
    	MySqlManager.init();
    	Log.i2file("servlet create ===============");
    }
	
}
