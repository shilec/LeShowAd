package com.shilec.leshowad.dao.helper;


import java.util.List;

/**
 * ���ݿ������ӿ�
 * @author shijiale
 *
 * @param <T>
 */
public interface IDatabaseHelper<T> {
	void add(T t);
	
	void add(List<T> ts);
	
	/**
	 * ɾ�����ݣ�����: where = "id = 1 and age < 10"
	 * @param where
	 */
	void delete(String where);
	
	void deleteAll();
	
	void update(T t,String where);
	
	/**
	 * ���¼�¼
	 * @param t moudle
	 * @param fields Ҫ������Щ�ֶ�
	 * @param where ����: "id = 1 and age < 10"
	 */
	void update(T t,String[] fields,String where);
	
	List<T> loadAll();
	
	T load(String where);
	
	List<T> loadSome(String where);
}
