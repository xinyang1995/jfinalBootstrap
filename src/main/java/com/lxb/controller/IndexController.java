package com.lxb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.lxb.annotation.RouteBind;
import com.lxb.config.GlobalConfig;
import com.lxb.entity.SysUser;
import com.lxb.util.MD5Util;

@RouteBind(controllerKey = "/", viewPath = "/")
@Clear
public class IndexController extends Controller {
	
	/**
	 * 进入首页
	 */
	public void index() {

		HttpServletRequest request=this.getRequest();
		HttpSession session=request.getSession();
		
		if (null==session.getAttribute(GlobalConfig.SESSION_USER)) {
			renderFreeMarker("login_pc.html");
		}else {
			renderFreeMarker("index_pc/index.html");
		}
		
	}
	
	/**
	 * 登陆操作
	 */
	public void login() {
		
		HttpServletRequest request=this.getRequest();
		String userName=request.getParameter("login");
		String password=request.getParameter("pwd");
		
		SysUser sysUser=SysUser.dao.findFirst("select * from sys_user where userName='"+userName+"' and password='"+MD5Util.MD5Encode(password)+"'");
		if (null!=sysUser) {
			HttpSession session=request.getSession();
			session.setAttribute("sessionUser", sysUser);
			session.setAttribute("sessionUserAuth", sysUser.get("userAuth"));
			session.setAttribute("sessionUserGroup", sysUser.get("groupId"));
			
			setAttr("status", 1);
			
			renderJson();
		}else {
			setAttr("status", 0);
			renderJson();
		}
		
	}
	
}
