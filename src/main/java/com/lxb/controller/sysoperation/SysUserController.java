package com.lxb.controller.sysoperation;

import com.jfinal.core.Controller;
import com.lxb.annotation.RouteBind;
import com.lxb.service.SysUserService;

@RouteBind(controllerKey = "/sysUser", viewPath = "/")
public class SysUserController extends Controller {

	private SysUserService sysUserService = new SysUserService();

	public void index() {

//		String userName = sysUserService.getUser(1);
//		System.out.println(userName);
		this.renderFreeMarker("/welcome.html");
	}

}
