package com.shilec.leshowad.dao.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��ʶ���ݿ��е�����,ֻ����int��
 * @author shijiale
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.FIELD})
public @interface Id {
	
}
