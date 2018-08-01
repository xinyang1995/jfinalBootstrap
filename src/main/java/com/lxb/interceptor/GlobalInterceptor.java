package com.lxb.interceptor;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.lxb.annotation.Permission;
import com.lxb.bean.PermissionInfo;
import com.lxb.config.GlobalConfig;
import com.lxb.config.RouteConfig;
import com.lxb.entity.OperateLog;
import com.lxb.entity.SysGroup;
import com.lxb.entity.SysUser;
import com.lxb.util.HttpUtil;

public class GlobalInterceptor implements Interceptor {

	private static Logger log = LoggerFactory
			.getLogger(GlobalInterceptor.class);

	@SuppressWarnings("unchecked")
	public void intercept(Invocation i) {
		Controller c = i.getController();// 获取Action调用的controller对象
		HttpServletRequest request = c.getRequest();
		if (null == c.getSession().getAttribute(GlobalConfig.SESSION_USER)) {
			log.info("未登录...");
			c.renderFreeMarker("/WEB-INF/template/login_pc.html");
			return;
		}
		SysUser loginUser = (SysUser) c.getSession()
				.getAttribute(GlobalConfig.SESSION_USER);
		SysGroup group = (SysGroup) c.getSession()
				.getAttribute(GlobalConfig.SESSION_USER_GROUP);
		String accessName = i.getActionKey();// 获取Action调用的action key值
		log.info(accessName + "开始...");
		Permission p = i.getMethod().getAnnotation(Permission.class);// 能够被当前拦截器拦下来的肯定会有Permission注解,不用判断非空
		PermissionInfo pi = RouteConfig.allAuth.get(p.id() + "");

		Set<String> userAuthSet = (Set<String>) c.getSession()
				.getAttribute(GlobalConfig.SESSION_USER_AUTH);
		if (loginUser.getInt("groupId") == 0) {// 超管直接过
			doInvocation(i);// 执行具体的业务方法
		} else if (group.getInt("type") == SysGroup.TYPE_NORMAL_ADMIN) {// 管理员也直接过
			doInvocation(i);// 执行具体的业务方法
		} else if (userAuthSet.contains("" + pi.getId())) {// 普通用户需要进行判断
			doInvocation(i);// 执行具体的业务方法
		} else {
			log.info("访问" + accessName + "没有权限,用户:"
					+ loginUser.getStr("userName"));
			c.renderText("permission_limit");// 没有权限
			return;
		}

		OperateLog operateLog = new OperateLog();
		operateLog.set("type", "pc");
		operateLog.set("accessName", pi.getName());
		operateLog.set("accessPath", pi.getAccessUrl());
		operateLog.set("creater", loginUser.getInt("id"));
		operateLog.set("date", new Date());
		operateLog.set("params", HttpUtil.getAllRequestParams(request));
		operateLog.save();
		log.info(accessName + "结束...");
	}

	private void doInvocation(Invocation i) {
		try {
			i.invoke();
		} catch (Exception e) {
			// 全局异常统一处理
			log.error(i.getActionKey(), e);
			i.getController().setAttr("error", e);
			i.getController().renderFreeMarker("/WEB-INF/template/500.html");
		}
	}

}
