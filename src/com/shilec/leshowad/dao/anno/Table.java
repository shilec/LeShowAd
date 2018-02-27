package com.shilec.leshowad.dao.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 标识的该moudle 对应数据库中的表名
 * @author shijiale
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE})
public @interface Table {
	String value() default "";
}
