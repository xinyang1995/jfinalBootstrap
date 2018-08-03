package com.lxb.controller.sysoperation;

import com.jfinal.core.Controller;
import com.lxb.annotation.Permission;
import com.lxb.annotation.RouteBind;
import com.lxb.bean.PermissionInfo;

@RouteBind(controllerKey = "/sysuser", viewPath = "/sysuser")
@Permission(id = 10000, type = PermissionInfo.TYPE_NOTE, name = "后台管理", icon = "glyphicon-glyphicon-cog", sort = 1)
public class SysUserController extends Controller {

	public void index() {
		this.renderFreeMarker("/welcome.html");
	}

}
