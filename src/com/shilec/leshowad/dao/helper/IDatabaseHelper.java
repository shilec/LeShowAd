package com.shilec.leshowad.dao.helper;


import java.util.List;

/**
 * 数据库帮助类接口
 * @author shijiale
 *
 * @param <T>
 */
public interface IDatabaseHelper<T> {
	void add(T t);
	
	void add(List<T> ts);
	
	void delete(String where);
	
	void deleteAll();
	
	void update(T t,String where);
	
	void update(T t,String[] fields,String where);
	
	List<T> loadAll();
	
	T load(String where);
	
	List<T> loadSome(String where);
}
