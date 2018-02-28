package com.shilec.leshowad.dao.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.shilec.leshowad.dao.helper.IDatabaseHelper;


/**
 * 在Moudle 类标识 该类的Dao 类
 * @author shijiale
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE})
public @interface Dao {
	Class<? extends IDatabaseHelper> value();
}
