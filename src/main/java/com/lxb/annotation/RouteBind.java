package com.lxb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定Controller或者Controller的方法上面的注解
 * 
 * @since 2018年4月16日 下午8:20:42
 * @author LXB
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface RouteBind {
	/** Action访问路径,也就是actionKey */
	String controllerKey();

	/**
	 * 视图所在目录，相对于JFWebConfig里面配置常量constants.setBaseViewPath("WEB-INF/template")
	 * 路径而言
	 */
	String viewPath();

}
