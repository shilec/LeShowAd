package com.shilec.leshowad.dao.helper;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;

import com.shilec.leshowad.dao.anno.Dao;
import com.shilec.leshowad.dao.anno.Id;
import com.shilec.leshowad.dao.anno.Ignore;
import com.shilec.leshowad.dao.anno.Table;
import com.shilec.leshowad.exception.MissAnnotionException;
import com.shilec.leshowad.exception.MoudleNotHaveMainKeyException;
import com.shilec.leshowad.utils.Log;


/***
 * orm 框架
 * 
 * @author shijiale
 *
 * @param <T>
 */
public abstract class BaseDatabaseHelper<T> implements IDatabaseHelper<T>{
	
	private String mTableName; //表名
	private Class<T> mClass;   //moudle 的class
	
	public String getTableName() {
		return mTableName;
	}
	
	//根据Table 注解创建数据表
	private void autoCreateTable() {
		StringBuilder sqlBuilder = new StringBuilder()
				.append("CREATE TABLE IF NOT EXISTS ")
				.append(mTableName + "(")
				.append("id integer not null primary key auto_increment,");
		Field[] fields = mClass.getDeclaredFields();
		for(Field field : fields) {
			if(field.getAnnotation(Ignore.class) != null) {
				continue;
			}
			
			if(field.getAnnotation(Id.class) != null) {
				continue;
			}
			
			String javaType = field.getType().getSimpleName();
			String key = field.getName();
			//如果有Field 注解 ，则使用Field 的value 作为数据库字段名
			if(field.getAnnotation(com.shilec.leshowad.dao.anno.Field.class) != null) {
				key = field.getAnnotation(com.shilec.leshowad.dao.anno.Field.class).value();
			}
			sqlBuilder.append(key + " " + getSqlTypeByJavaType(javaType) + ",");
		}
		String sql = sqlBuilder.toString().substring(0,
				sqlBuilder.toString().length() - 1);
		sql += ")";
		Log.debug("sql = " + sql);
		
		createTable(sql);
	}
	
	private void createTable(String sql) {
		Connection connc = MySqlConHelper.getInstance().getConnection();
		try {
			Statement statement = connc.createStatement();
			statement.execute(sql);
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
	
	//java 中的类型和mysql中的数据类型 映射
	private String getSqlTypeByJavaType(String javaType) {
		switch(javaType) {
		case  "String":
			return "text";
		case "Integer":
		case "int":
			return "int";
		case "Float":
		case "float":
			return "float";
		case "Long":
		case "long":
			return "long";
		case "Boolean":
		case "boolean":
			return "bool";
		default:
			return null;
		}
	}
	
	public BaseDatabaseHelper() {
		//获取泛型实际类型
		Class<?> clazz = (Class<?>) ((ParameterizedType) getClass().
				getGenericSuperclass()).getActualTypeArguments()[0];
		mClass = (Class<T>) clazz;
		Table annotation = clazz.getAnnotation(Table.class);
		Dao dao = clazz.getAnnotation(Dao.class);
		
		if(dao == null) {
 
		}
		
		if(annotation == null) {
			throw new MissAnnotionException("missing Table Annotion!");
		}
		
		//是否有主键
		Field[] fields = clazz.getDeclaredFields();
		boolean isHaveMainKey = false;
		for(Field field : fields) {
			if(field.getAnnotation(Id.class) != null) {
				isHaveMainKey = true;
				break;
			}
		}
		
		if(!isHaveMainKey) {
			throw new MoudleNotHaveMainKeyException("miss main key!");
		}
		
		if(annotation != null && annotation.value() != null && annotation.value() != "") {
			mTableName = annotation.value();
		}
		autoCreateTable();
	}
	
	private String createTableString(T t) {
		StringBuilder builder = new StringBuilder()
				.append(mTableName)
				.append("(");
		Class<T> tCls = (Class<T>) t.getClass();
		Field[] declaredFields = tCls.getDeclaredFields();
		
		List<String> fieldNames = new ArrayList<>();
		for(Field field : declaredFields) {
			//标识Ignore 不保存
			Ignore ignore = field.getAnnotation(Ignore.class);
			if(ignore != null) {
				continue;
			}
			
			//id 自增，不保存
			Id id = field.getAnnotation(Id.class);
			if(id != null) {
				continue;
			}
			
			com.shilec.leshowad.dao.anno.Field annotation = field.getAnnotation(
					com.shilec.leshowad.dao.anno.Field.class);
			//注解 Field 的 value作为键值
			if(annotation != null) {
				fieldNames.add(annotation.value());
			} else {
				fieldNames.add(field.getName());
			}
		}
		
		//拼接字段sql
		for(int i = 0; i < fieldNames.size(); i++) {
			if(i != fieldNames.size() - 1) {
				builder.append(fieldNames.get(i) + ",");
			} else {
				builder.append(fieldNames.get(i));
			}
		}
		
		//拼接参数sql
		builder.append(") value(");
		for(int i = 0; i < fieldNames.size(); i++) {
			if(i != fieldNames.size() - 1) {
				builder.append("?,");
			} else {
				builder.append("?");
			}
		}
		builder.append(")");
		
		return builder.toString();
	}
	
	/**
	 * 把moudle中的值和字段 添加到 PreparedStatement
	 * @param t
	 * @param statement
	 */
	private void setData(T t,PreparedStatement statement) {
		Class<?> tCls = t.getClass();
		Field[] declaredFields = tCls.getDeclaredFields();
		
		int index = 1; //列序号
		for(Field field : declaredFields) {
			//ignore 跳过
			Ignore ignore = field.getAnnotation(Ignore.class);
			if(ignore != null) {
				continue;
			}
			//id 跳过
			Id id = field.getAnnotation(Id.class);
			if(id != null) {
				continue;
			}
			
			try {
				field.setAccessible(true);
				Object value = field.get(t);
				String typeName = field.getType().getSimpleName();
				setValue(index++, typeName, value, statement);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将对应类型的值存库
	 * @param index
	 * @param typeName
	 * @param value
	 * @param statement
	 * @throws SQLException
	 */
	private void setValue(int index,String typeName,Object value,PreparedStatement statement) throws SQLException {
		switch(typeName) {
		case "String":
			statement.setString(index, value == null ? "" : value.toString());
			break;
		case "boolean":
		case "Boolean":
			statement.setBoolean(index, Boolean.parseBoolean(value.toString()));
			break;
		case "Integer":
		case "int":
			statement.setInt(index, Integer.parseInt(value.toString()));
			break;
		case "Float":
		case "float":
			statement.setFloat(index, Float.parseFloat(value.toString()));
			break;
		case "Long":
		case "long":
			statement.setLong(index, Long.parseLong(value.toString()));
			break;
		}
	}
	
	/**
	 * 从cursor 中读出的值，保存到moudle
	 * @param resultSet
	 * @return moudle
	 */
	private T getData(ResultSet resultSet) {
		Field[] fields = mClass.getDeclaredFields();
		T t = null;
		try {
			t = mClass.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		for(Field field : fields) {
			if(field.getAnnotation(Ignore.class) != null) {
				continue;
			}
			String key = field.getName();
			com.shilec.leshowad.dao.anno.Field field2 = field.getAnnotation(com.shilec.leshowad.dao.anno.Field.class);
			if(field2 != null) {
				key = field2.value();
			}
			
			try {
				String typeName = field.getType().getSimpleName();
				Object value = null;
				switch(typeName) {
				case "String":
					value = resultSet.getString(resultSet.findColumn(key));
					break;
				case "Boolean":
				case "boolean":
					value = resultSet.getBoolean(resultSet.findColumn(key));
					break;
				case "Integer":
				case "int":
					value = resultSet.getInt(resultSet.findColumn(key));
					break;
				case "Float":
				case "float":
					value = resultSet.getFloat(resultSet.findColumn(key));
					break;
				case "Long":
				case "long":
					value = resultSet.getLong(resultSet.findColumn(key));
					break;
				}
				
				if(value != null) {
					field.setAccessible(true);
					field.set(t,value);
					//Log.i("value = " + value + ",key = " + key);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return t;
	}
	

	@Override
	public void add(T t) {
		Connection con = MySqlConHelper.getInstance().getConnection();
		try {
			String sql = "insert into " + createTableString(t);
			Log.i2file(sql);
			PreparedStatement cmd = con.prepareStatement(sql);
			setData(t, cmd);
			cmd.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void add(List<T> ts) {
		Connection con = MySqlConHelper.getInstance().getConnection();
		try {
			con.setAutoCommit(false);
			String sql = "insert into " + createTableString(ts.get(0));
			PreparedStatement cmd = con.prepareStatement(sql);
			for(T t : ts) {
				setData(t, cmd);
				cmd.addBatch();
			}
			
			cmd.executeBatch();
			con.commit();
			
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}



	@Override
	public void deleteAll() {
		delete("");
	}


	@Override
	public List<T> loadAll() {
		return loadSome("");
	}

	@Override
	public T load(String where) {
		Connection connection = MySqlConHelper.getInstance().getConnection();
		try {
			Statement cmd = connection.createStatement();
			String sql = "select * from " + mTableName + " where " + where;
			ResultSet resultSet = cmd.executeQuery(sql);
			if(resultSet == null) {
				return null;
			}
			resultSet.next();
			T t = getData(resultSet);
			cmd.close();
			
			return t;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public List<T> loadSome(String where) {
		return loadSome(where,null);
	}


	@Override
	public void delete(String where) {
		Connection connection = MySqlConHelper.getInstance().getConnection();
		try {
			Statement cmd = connection.createStatement();
			String sql = "delete from " + mTableName;
			if(where != null && where != "") {
				sql += " where " + where;
			}
			cmd.execute(sql);
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void update(T t, String where) {
		Field[] fields = mClass.getDeclaredFields();
		List<String> fieldNames = new ArrayList<>();
		List<Object> values = new ArrayList<>();
		
		for(Field field : fields) {
			if(field.getAnnotation(Ignore.class) != null) {
				continue;
			}
			
			if(field.getAnnotation(Id.class) != null) {
				continue;
			}
			
			String key = field.getName();
			com.shilec.leshowad.dao.anno.Field field2 = field.getAnnotation(com.shilec.leshowad.dao.anno.Field.class);
			if(field2 != null) {
				key = field2.value();
			}
			
			field.setAccessible(true);
			try {
				Object object = field.get(t);
				//空值不更新
				if(object == null) {
					continue;
				}
				values.add(object);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			fieldNames.add(key);
		}
		
		StringBuilder builder = new StringBuilder()
				.append("update " + mTableName + " set ");
		for(int i = 0; i < fieldNames.size(); i++) {
			if(i != fieldNames.size() - 1) {
				builder.append(fieldNames.get(i) + "=?,");
			} else {
				builder.append(fieldNames.get(i) + "=?");
			}
		}
		
		String sql = builder.toString();
		if(where != null && where != "") {
			sql += " where " + where;
		}
		//Log.i(sql);
		//Log.i(values.toString());
		//Log.i(fieldNames.toString());
		Connection connection = MySqlConHelper.getInstance().getConnection();
		try {
			PreparedStatement cmd = connection.prepareStatement(sql);
			for(int i = 0; i < fieldNames.size(); i++) {
				//Log.i(values.get(i).getClass().getSimpleName());
				setValue(i+1, values.get(i).getClass().getSimpleName(), values.get(i), cmd);
			}
			
			cmd.execute();
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void update(T t, String[] fields, String where) {
		//拼接 update t_user set x=?
		StringBuilder builder = new StringBuilder()
				.append("update " + mTableName + " set ");
		for(int i = 0; i < fields.length; i++) {
			if(i != fields.length - 1) {
				builder.append(fields[i] + "=?,");
			} else {
				builder.append(fields[i] + "=?");
			}
		}
		
		//where 
		String sql = builder.toString();
		if(where != null && where != "") {
			sql += " where " + where;
		}
		
		Field[] fields2 = mClass.getDeclaredFields();
		List<Object> values = new ArrayList<>();
		for(Field field : fields2) {
			if(field.getAnnotation(Ignore.class) != null) {
				continue;
			}
			
			String key = field.getName();
			//如果有field注解使用该注解的value作为字段键值
			if(field.getAnnotation(com.shilec.leshowad.dao.anno.Field.class) != null) {
				key = field.getAnnotation(com.shilec.leshowad.dao.anno.Field.class).value();
			}
			
			for(String fieldName : fields) {
				if(fieldName == key) {
					try {
						field.setAccessible(true);
						Object object = field.get(t);
						//保存字段value
						values.add(object);
						break;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		Connection connection = MySqlConHelper.getInstance().getConnection();
		try {
			PreparedStatement cmd = connection.prepareStatement(sql);
			for(int i = 0; i < values.size(); i++) {
				setValue(i + 1, fields2[i].getType().getSimpleName(), values.get(i), cmd);
			}
			cmd.execute();
			cmd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<T> loadAll(String where, String order) {
		return loadSome(null, order);
	}

	@Override
	public List<T> loadSome(String where, String order) {
		Connection connection = MySqlConHelper.getInstance().getConnection();
		List<T> ts = new ArrayList<>();
		try {
			Statement cmd = connection.createStatement();
			String sql = "select * from " + mTableName;
			if(where != null && where != "") {
				sql += " where " + where;
			}
			if(order != null && order != "") {
				sql += " order " + order;
			}
			
			ResultSet result = cmd.executeQuery(sql);
			if(result == null) {
				return null;
			}
			while(result.next()) {
				T t = getData(result);
				ts.add(t);
			}
			cmd.close();
			return ts;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	} 

}
