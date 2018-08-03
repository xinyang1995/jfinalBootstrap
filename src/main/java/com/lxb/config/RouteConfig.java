package com.lxb.config;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.lxb.annotation.Permission;
import com.lxb.annotation.RouteBind;
import com.lxb.bean.PermissionInfo;
import com.lxb.util.PackageUtil;

/**
 * 路由和权限设置
 * 
 * @since 2018年4月16日 下午8:20:11
 * @author LXB
 */
public class RouteConfig {

	private static Logger log = LoggerFactory.getLogger(RouteConfig.class);

	private static final String PACK_CONTROLLER_PATH = "com.lxb.controller";
	public static final Map<String, PermissionInfo> levelAuth = new TreeMap<String, PermissionInfo>();
	public static final Map<String, PermissionInfo> allAuth = new HashMap<String, PermissionInfo>();
	public static final Set<String> checkControllerKey = new HashSet<String>();
	public static final String BASE_PATH_VIEW = "WEB-INF/template";

	@SuppressWarnings("unchecked")
	public static void init(Routes routes) throws Exception {
		List<String> controllerPackages = Arrays
				.asList(PACK_CONTROLLER_PATH.split(","));
		log.info("路由和权限设置开始...");
		for (int i = 0; i < controllerPackages.size(); i++) {
			Set<Class<?>> classSet = PackageUtil
					.getClasses(controllerPackages.get(i));
			if (null == classSet || 0 == classSet.size()) {
				continue;
			}
			for (Class<?> c : classSet) {
				// 路由设置
				if (c.isAnnotationPresent(RouteBind.class)
						&& Controller.class.isAssignableFrom(c)) {
					RouteBind routeBind = c.getAnnotation(RouteBind.class);
					String controllerKey = routeBind.controllerKey();
					String viewPath = routeBind.viewPath();
					routes.add(controllerKey, (Class<? extends Controller>) c,
							BASE_PATH_VIEW + viewPath);
				}
				// 权限设置
				if (c.isAnnotationPresent(RouteBind.class)
						&& c.isAnnotationPresent(Permission.class)
						&& Controller.class.isAssignableFrom(c)) {
					Permission p = c.getAnnotation(Permission.class);
					if (p.type() != PermissionInfo.TYPE_NOTE) {
						continue;
					}
					RouteBind routeBind = c.getAnnotation(RouteBind.class);
					String controllerKey = routeBind.controllerKey();
					PermissionInfo node = levelAuth.get("" + p.id());
					if (null == node) {
						node = new PermissionInfo(p, "");// 一级菜单没有访问路径
						levelAuth.put("" + node.getId(), node);
					}
					if (!allAuth.containsKey("" + node.getId())) {
						allAuth.put("" + node.getId(), node);
					}
					Method[] methods = c.getDeclaredMethods();
					PermissionInfo menu = null;// 一个Controller下面只允许存在一个TYPE_MENU的方法
					for (Method method : methods) {
						Permission menuPermission = method
								.getAnnotation(Permission.class);
						if (menuPermission == null
								|| PermissionInfo.TYPE_MENU != menuPermission
										.type()) {
							continue;
						}
						menu = new PermissionInfo(menuPermission,
								controllerKey + "/" + method.getName());
						menu.setParent(node.getId());
						if (allAuth.containsKey("" + menu.getId())) {
							throw new RuntimeException("已存在权限id:" + menu.getId()
									+ ", 冲突的controller:" + c.getName()
									+ ",冲突的Method:" + method.getName());
						} else {
							allAuth.put("" + menu.getId(), menu);
						}
						if (checkControllerKey.contains(menu.getAccessUrl())) {
							throw new RuntimeException(
									"已存在controllerKey:" + menu.getAccessUrl()
											+ " 冲突的controller:" + c.getName()
											+ ",冲突的Method:" + method.getName());
						} else {
							checkControllerKey.add(menu.getAccessUrl());
						}
						node.getSubPermission().add(menu);
						break;// 一个Controller下面只允许存在一个TYPE_MENU的方法
					}
					for (Method method : methods) {
						Permission methodPermission = method
								.getAnnotation(Permission.class);
						if (methodPermission == null
								|| PermissionInfo.TYPE_METHOD != methodPermission
										.type()) {
							continue;
						}
						PermissionInfo methodPi = new PermissionInfo(
								methodPermission,
								controllerKey + "/" + method.getName());
						if (methodPi.getParent() != menu.getId()) {
							throw new RuntimeException(
									"不存在父权限id:" + methodPi.getParent()
											+ ", controller:" + c.getName()
											+ ",Method:" + method.getName());
						}
						if (allAuth.containsKey("" + methodPi.getId())) {
							throw new RuntimeException(
									"已存在权限id:" + methodPi.getId()
											+ ", 冲突的controller:" + c.getName()
											+ ",冲突的Method:" + method.getName());
						} else {
							allAuth.put("" + methodPi.getId(), methodPi);
						}
						if (checkControllerKey
								.contains(methodPi.getAccessUrl())) {
							throw new RuntimeException("已存在controllerKey:"
									+ methodPi.getAccessUrl()
									+ " 冲突的controller:" + c.getName()
									+ ",冲突的Method:" + method.getName());
						} else {
							checkControllerKey.add(methodPi.getAccessUrl());
						}
						menu.getSubPermission().add(methodPi);
					}
				}
			}
		}
		log.info("路由和权限设置结束!");
		log.info("处理系统菜单和权限结构...");
		for (String nodeId : levelAuth.keySet()) {
			PermissionInfo node = levelAuth.get(nodeId);
			List<PermissionInfo> nodeSubMenuList = node.getSubPermission();
			Collections.sort(nodeSubMenuList, new Comparator<PermissionInfo>() {
				public int compare(PermissionInfo o1, PermissionInfo o2) {
					if (o1.getId() < o2.getId()) {
						return -1;
					} else if (o1.getId() > o2.getId()) {
						return 1;
					} else {
						return 0;
					}
				}
			});
			for (PermissionInfo menu : nodeSubMenuList) {
				List<PermissionInfo> menuSubMethodList = menu
						.getSubPermission();
				Collections.sort(menuSubMethodList,
						new Comparator<PermissionInfo>() {
							public int compare(PermissionInfo o1,
									PermissionInfo o2) {
								if (o1.getId() < o2.getId()) {
									return -1;
								} else if (o1.getId() > o2.getId()) {
									return 1;
								} else {
									return 0;
								}
							}
						});
			}
		}
		log.info("处理系统菜单和权限结构完毕...");
		log.info("系统菜单和权限最终结构:");
		log.info(JSONObject.toJSONString(levelAuth));
	}
}
