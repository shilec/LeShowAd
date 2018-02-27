package com.shilec.leshowad.dao.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * moudle�б��������ݿ��ж�Ӧ���ֶ���,��������������ݿ���ֶ�����ͬ����Ҫ��ע
 * @author shijiale
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.FIELD})
public @interface Field {
	String value() default "";
}
