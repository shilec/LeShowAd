//package com.shilec.leshowad.service.listenner;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//
//import com.shilec.leshowad.dao.helper.MySqlManager;
//import com.shilec.leshowad.utils.ConfigUtils;
//import com.shilec.leshowad.utils.Log;
//
///**
// * Application Lifecycle Listener implementation class AppServletContextListenner
// *
// */
//@WebListener
//public class AppServletContextListenner implements ServletContextListener {
//
//    /**
//     * Default constructor. 
//     */
//    public AppServletContextListenner() {
//    }
//
//	/**
//     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
//     */
//    public void contextDestroyed(ServletContextEvent arg0)  { 
//    }
//
//	/**
//     * @see ServletContextListener#contextInitialized(ServletContextEvent)
//     */
//    public void contextInitialized(ServletContextEvent arg0)  { 
//    	ConfigUtils.init(arg0.getServletContext().getRealPath("/"));
//    	Log.init(arg0.getServletContext().getRealPath("/"));
//    	MySqlManager.init();
//    	Log.i("servlet create ===============");
//    }
//	
//}
