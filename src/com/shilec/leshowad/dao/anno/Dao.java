package com.shilec.leshowad.dao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;


/**
 * ��Moudle ���ʶ �����Dao ��
 * @author shijiale
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE})
public @interface Dao {
	Class<? extends IDatabaseHelper> value();
}
