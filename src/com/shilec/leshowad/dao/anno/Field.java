package com.shilec.leshowad.dao.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * moudle中变量在数据库中对应的字段名,如果变量名和数据库的字段名相同则不需要标注
 * @author shijiale
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.FIELD})
public @interface Field {
	String value() default "";
}
