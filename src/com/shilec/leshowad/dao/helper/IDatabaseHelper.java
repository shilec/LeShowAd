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
	
	/**
	 * 删除数据，例如: where = "id = 1 and age < 10"
	 * @param where
	 */
	void delete(String where);
	
	void deleteAll();
	
	void update(T t,String where);
	
	/**
	 * 更新记录
	 * @param t moudle
	 * @param fields 要更新哪些字段
	 * @param where 例如: "id = 1 and age < 10"
	 */
	void update(T t,String[] fields,String where);
	
	List<T> loadAll();
	
	T load(String where);
	
	List<T> loadSome(String where);
}
