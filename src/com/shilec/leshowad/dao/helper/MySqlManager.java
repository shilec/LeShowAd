package com.shilec.leshowad.dao.helper;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.exception.DatabaseHelperException;
import com.shilec.leshowad.moudle.RedPacket;
import com.shilec.leshowad.moudle.ShareMap;
import com.shilec.leshowad.moudle.UserInfo;
import com.shilec.leshowad.utils.Log;

public class MySqlManager {

	final StringBuilder T_USER = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append("t_user(")
			.append("wx_id varchar(256),").append("wx_user_name varchar(64),").append("wx_location varchar(256),")
			.append("balance float,").append("id integer not null primary key auto_increment").append(")");

	final StringBuilder T_SHARE_MAP = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append("t_share_map(")
			.append("id integer not null primary key auto_increment,").append("wx_id varchar(256),")
			.append("red_packet_id integer,").append("income float,").append("shared_wx_id varchar(256),")
			.append("open_count int").append(")");

	final StringBuilder T_RED_PACKET = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append("t_red_packet(")
			.append("id integer not null primary key auto_increment,").append("locations varchar(256),")
			.append("all_money float,").append("packet_count int,").append("income_count int,")
			.append("leave_count int,").append("leave_money float,").append("ad_id int,").append("is_expire bool")
			.append(")");

	final StringBuilder T_AD_TEMPLATE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ")
			.append("t_ad_template(").append("id integer not null primary key auto_increment,")
			.append("title varchar(256),").append("ad_desc varchar(1000),").append("images varchar(1024),")
			.append("bk_music varchar(256),").append("html_templete varchar(256)").append(")");

	private static MySqlManager sManager = new MySqlManager();
	
	/***
	 *  获取一个DaoHelper
	 * @param cls moudle类型
	 * @return helper
	 */
	public <T> IDatabaseHelper<T> getHelper(Class<T> cls) {
		Dao annotation = cls.getAnnotation(Dao.class);
		if (annotation == null) {
			return null;
		}
		Class<?> dao = annotation.value();
		if (dao == null) {
			return null;
		}
		try {
			//实例化dao 类
			Object instance = (IDatabaseHelper<T>) dao.newInstance();
			//获取dao类的泛型参数
			Class<?> clazz = (Class<?>) ((ParameterizedType) instance.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
			//如果dao 类的 泛型参数 和 当前要获取的 moudle 的类型不同，则抛出异常
			if (!clazz.getSimpleName().equals(cls.getSimpleName())) {
				throw new DatabaseHelperException("this dao " + dao.getSimpleName() + "" + " support moudle is "
						+ clazz.getSimpleName() + "\n," + "but your moudle type is " + cls.getSimpleName());
			}
			return (IDatabaseHelper<T>) instance;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void init() {

	}

	private MySqlManager() {
		// initTable();
	}

	private void initTable() {
		Connection connc = MySqlConHelper.getInstance().getConnection();
		try {
			Statement statement = connc.createStatement();
			Log.i2file("t_ad_template = " + T_AD_TEMPLATE.toString());

			statement.execute(T_USER.toString());
			statement.execute(T_SHARE_MAP.toString());
			statement.execute(T_RED_PACKET.toString());
			statement.execute(T_AD_TEMPLATE.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static MySqlManager getInstance() {
		return sManager;
	}
}
