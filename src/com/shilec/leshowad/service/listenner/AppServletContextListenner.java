package com.shilec.leshowad.service.listenner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.shilec.leshowad.dao.helper.MySqlManager;
import com.shilec.leshowad.utils.ConfigUtils;
import com.shilec.leshowad.utils.Log;

/**
 * AppContext ����������ʼ������
 *
 */
@WebListener
public class AppServletContextListenner implements ServletContextListener {

    public AppServletContextListenner() {
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	//��ʼ������
    	ConfigUtils.init(arg0.getServletContext().getRealPath("/"));
    	//��ʼ����־
    	Log.init(arg0.getServletContext().getRealPath("/"));
    	//���ݿ��ʼ�������ݱ�Ĵ�����
    	MySqlManager.init();
    	Log.i2file("servlet create ===============");
    }
	
}
