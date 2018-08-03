package com.lxb.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.lxb.annotation.RouteBind;
import com.lxb.bean.PermissionInfo;
import com.lxb.config.GlobalConfig;
import com.lxb.config.RouteConfig;
import com.lxb.entity.SysGroup;
import com.lxb.entity.SysUser;
import com.lxb.service.SysGroupService;
import com.lxb.service.SysUserService;
import com.lxb.util.DateUtils;

@RouteBind(controllerKey = "/", viewPath = "/")
public class IndexController extends Controller {

	private SysUserService sysUserService = SysUserService.getInstance();
	private SysGroupService sysGroupService = SysGroupService.getInstance();

	/**
	 * 进入首页
	 */
	@Clear
	public void index() {

		// 判断用户登录
		if (null == getSession().getAttribute(GlobalConfig.SESSION_USER)) {
			renderFreeMarker("login_pc.html");
			return;
		}
		// 获取用户权限
		SysUser sysUser = (SysUser) getSession()
				.getAttribute(GlobalConfig.SESSION_USER);
		JSONArray navs = new JSONArray();
		String base = getAttr("base");
		// 根据用户的相应权限过滤菜单
		for (String key : RouteConfig.levelAuth.keySet()) {
			PermissionInfo p = RouteConfig.levelAuth.get(key);
			if (!judgeAuth(sysUser, p.getId())) {// 一级菜单权限判断
				continue;
			}
			JSONObject node = new JSONObject();
			node.put("title", p.getName());
			node.put("icon", p.getIcon());
			node.put("spread", false);
			JSONArray children = new JSONArray();
			if (p.getSubPermission().size() <= 0) {
				node.put("children", children);
				navs.add(node);
				continue;
			}
			for (PermissionInfo cPI : p.getSubPermission()) {
				if (!judgeAuth(sysUser, cPI.getId())) {// 二级菜单权限
					continue;
				}
				JSONObject child = new JSONObject();
				child.put("title", cPI.getName());
				child.put("icon", cPI.getIcon());
				child.put("href", base + cPI.getAccessUrl());
				children.add(child);
			}
			node.put("children", children);
			navs.add(node);
		}
		if (sysUser.getInt("groupId") == 0) {
			JSONObject extend = new JSONObject();
			extend.put("title", "所有权限");
			extend.put("icon", "&#xe613;");
			extend.put("spread", false);
			extend.put("href", base + "/allPermission");
			navs.add(extend);
		}
		setAttr("navs", JSONObject.toJSONString(navs));
		setAttr("loginUser", sysUser);
		setAttr("servertime",
				DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		renderFreeMarker("index_pc.html");
	}

	private boolean judgeAuth(SysUser sysUser, int pid) {
		int groupId = sysUser.getInt("groupId");
		if (SysGroup.GROUP_SUPER_ADMIN == groupId) {
			return true;
		}
		SysGroup group = sysGroupService.getSysGroupById(groupId);
		if (null == group) {
			return false;
		}
		if (SysGroup.TYPE_NORMAL_ADMIN == group.getInt("type")) {
			return true;
		}
		Set<String> userAuthSet = getUserAuthStr(sysUser);
		if (userAuthSet.contains(pid + "")) {
			return true;
		}
		return false;
	}

	private Set<String> getUserAuthStr(SysUser sysUser) {
		Set<String> result = new HashSet<String>();
		if (null == sysUser) {
			return result;
		}
		String userAuth = sysUser.get("userAuth");
		if (null == userAuth) {
			return result;
		}
		result.addAll(Arrays.asList(userAuth.split(",")));
		return result;
	}

	/**
	 * 登陆操作
	 */
	@Clear
	public void login() {

		HttpServletRequest request = this.getRequest();
		String userName = request.getParameter("login");
		String password = request.getParameter("pwd");

		SysUser sysUser = sysUserService.getSysUser(userName, password);
		if (null != sysUser) {
			HttpSession session = request.getSession();
			session.setAttribute(GlobalConfig.SESSION_USER, sysUser);
			session.setAttribute(GlobalConfig.SESSION_USER_AUTH,
					sysUser.get("userAuth"));
			SysGroup group = sysGroupService
					.getSysGroupById(sysUser.getInt("groupId"));
			session.setAttribute(GlobalConfig.SESSION_USER_GROUP, group);

			setAttr("status", 1);

			renderJson();
		} else {
			setAttr("status", 0);
			renderJson();
		}

	}

}
