package com.lxb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * model绑定数据库表注解
 * 
 * @since 2018年4月16日 下午8:06:47
 * @author LXB
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface TableBind {
	/** 表名 */
	String tableName();

	/** 主键名 */
	String pk() default "id";
}
