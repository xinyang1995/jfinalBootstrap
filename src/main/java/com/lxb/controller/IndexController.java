package com.lxb.controller;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.lxb.annotation.RouteBind;
import com.lxb.bean.PermissionInfo;
import com.lxb.config.GlobalConfig;
import com.lxb.config.RouteConfig;
import com.lxb.entity.LoginLog;
import com.lxb.entity.SysGroup;
import com.lxb.entity.SysUser;
import com.lxb.patchca.color.SingleColorFactory;
import com.lxb.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.lxb.patchca.service.ConfigurableCaptchaService;
import com.lxb.patchca.utils.encoder.EncoderHelper;
import com.lxb.service.SysGroupService;
import com.lxb.service.SysUserService;
import com.lxb.util.CheckMobileUtil;
import com.lxb.util.DateUtils;
import com.lxb.util.MD5Util;
import com.lxb.util.StringUtils;

@RouteBind(controllerKey = "/", viewPath = "/")
public class IndexController extends Controller {

	private static Logger log = LoggerFactory.getLogger(IndexController.class);

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
			extend.put("icon", "glyphicon glyphicon-lock");
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

	/*
	 * 首页显示
	 */
	@Clear
	public void main() {

		SysUser loginUser = (SysUser) getSession()
				.getAttribute(GlobalConfig.SESSION_USER);
		setAttr("loginUser", loginUser);
		HttpServletRequest request = getRequest();
		String ip = StringUtils.getRemoteIp(request);
		setAttr("nowIp", ip);
		setAttr("nowTime",
				DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
		this.renderFreeMarker("welcome.html");
	}

	/**
	 * 登陆操作
	 */
	@Clear
	public void login() {

		HttpSession session = getSession();

		String userName = getPara("username", "");
		String password = getPara("password", "");
		String code = getPara("code", "");

		if (!code.equals(session.getAttribute("code"))) {
			renderFreeMarker("login_pc.html");
			setAttr("msg", "验证码有误！");
			return;
		}

		SysUser sysUser = sysUserService.getSysUser(userName, password);
		if (null != sysUser) {

			// 登录成功
			sysUser.set("lastLogin", new Date());
			sysUser.update();
			getSession().setMaxInactiveInterval(3600 * 3);
			session.setAttribute(GlobalConfig.SESSION_USER, sysUser);
			session.setAttribute(GlobalConfig.SESSION_USER_AUTH,
					sysUser.get("userAuth"));
			SysGroup group = sysGroupService
					.getSysGroupById(sysUser.getInt("groupId"));
			session.setAttribute(GlobalConfig.SESSION_USER_GROUP, group);

			// 添加登录日志
			HttpServletRequest request = getRequest();
			LoginLog loginLog = new LoginLog();
			loginLog.set("type",
					CheckMobileUtil.check(request) ? "mobile设备" : "pc设备");
			loginLog.set("uid", sysUser.getInt("id"));
			String ip = request.getHeader("X-Real-IP");
			if (null == ip || ip.trim().length() == 0) {
				ip = request.getRemoteAddr();
			}
			loginLog.set("ip", ip);
			loginLog.set("createTime", new Date());
			loginLog.save();
			redirect("/");
		} else {
			setAttr("msg", "登录失败，请确认用户名和密码！");
			renderFreeMarker("login_pc.html");
		}

	}

	/*
	 * 退出登录
	 */
	@Clear
	public void logout() {

		Enumeration<?> e = getSession().getAttributeNames();
		while (e.hasMoreElements()) {
			String sessionName = (String) e.nextElement();
			log.info("存在的session属性名有：" + sessionName);
			getSession().removeAttribute(sessionName);
		}

		setAttr("status", 1);

		renderJson();
	}

	@Clear
	public void modifyPass() {

		String oldPass = getPara("oldPass", "");
		String newPass = getPara("newPass", "");
		String confirmPass = getPara("confirmPass", "");
		if ("".equals(oldPass) || "".equals(newPass)
				|| "".equals(confirmPass)) {
			setAttr("status", 0);
			renderJson();
			return;
		}

		String oldMd5 = MD5Util.MD5Encode(oldPass);
		String newMd5 = MD5Util.MD5Encode(newPass);
		String confirmMd5 = MD5Util.MD5Encode(confirmPass);

		// 旧密码是否正确
		SysUser sysUser = (SysUser) getSession()
				.getAttribute(GlobalConfig.SESSION_USER);
		if (!sysUser.get("password").equals(oldMd5)) {
			setAttr("status", 0);
			renderJson();
			return;
		}

		// 密码确认是否正确
		if (!newMd5.equals(confirmMd5)) {
			setAttr("status", 0);
			renderJson();
			return;
		}

		// 设置新密码
		sysUser.set("password", newMd5);
		boolean result = sysUser.update();
		if (result == true) {
			setAttr("status", 1);
		} else {
			setAttr("status", 0);
		}

		renderJson();
	}

	@Clear
	public void getCodeImage() {
		ServletOutputStream sos = null;
		try {
			HttpServletResponse response = getResponse();
			ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
			cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
			cs.setFilterFactory(
					new CurvesRippleFilterFactory(cs.getColorFactory()));
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/png");
			sos = response.getOutputStream();
			String code = EncoderHelper.getChallangeAndWriteImage(cs, "png",
					sos);
			getSession().setAttribute("code", code);
			sos.flush();
			sos.close();
			sos = null;
		} catch (Exception e) {

		} finally {
			if (null != sos) {
				try {
					sos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		renderNull();
	}
}
