package com.shilec.leshowad.dao.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * ��ʶ�ĸ�moudle ��Ӧ���ݿ��еı���
 * @author shijiale
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE})
public @interface Table {
	String value() default "";
}
