package com.lxb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lxb.bean.PermissionInfo;

/**
 * Permission 绑定Controller或者Controller的方法上面的注解
 * 
 * @since 2018年4月16日 下午8:19:27
 * @author LXB
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Permission {
	int id();// 权限id

	int parent() default 0;// 父权限id

	int type() default PermissionInfo.TYPE_METHOD;// 节点还是菜单还是方法,默认方法

	String name();// 名称

	String icon() default "";// 菜单图标,往往节点和菜单需要

	int sort() default 0;// 排序
}
