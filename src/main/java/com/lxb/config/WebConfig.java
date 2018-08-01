package com.lxb.config;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.lxb.annotation.RouteBind;
import com.lxb.entity.SysGroup;
import com.lxb.entity.SysUser;
import com.lxb.interceptor.GlobalInterceptor;
import com.lxb.util.LogsUtil;
import com.lxb.util.MD5Util;

public class WebConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants constants) {
		// 设置编码格式
		constants.setEncoding("UTF-8");
		// 设置为开发模式（如果为fals，jfinal会缓存页面，导致开发时修改页面后不能立即呈现）
		constants.setDevMode(true);
		// jfinal支持很多类型的页面，这里设置为FreeMarker
		constants.setViewType(ViewType.FREE_MARKER);
		// 自定义404页面
		constants.setError404View("WEB-INF/template/404.html");
		// 自定义500页面
		constants.setError500View("WEB-INF/template/500.html");
		// 配置下载文件路径
		constants.setBaseDownloadPath("download");
		// 配置上传文件路径
		constants.setBaseUploadPath("upload");
	}

	@Override
	public void configEngine(Engine arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers handlers) {
		/*
		 * 指定上下文路径的key，类似于在jsp中通过request.setAttribute("base",
		 * request.getContextPath())指定上下文路径的key一样
		 */
		handlers.add(new ContextPathHandler("base"));

	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		// 添加权限拦截器
		interceptors.add(new GlobalInterceptor());

	}

	@Override
	public void configPlugin(Plugins plugins) {
		// 这里启用Jfinal插件 读取jdbc配置
		PropKit.use("dataSource.properties");
		final String DRIVERCLASS = PropKit.get("jdbc.driverClass");
		final String URL = PropKit.get("jdbc.jdbcUrl");
		final String USERNAME = PropKit.get("jdbc.user");
		final String PASSWORD = PropKit.get("jdbc.password");
		final Integer INITIALSIZE = PropKit.getInt("jdbc.initialSize");
		final Integer MIDIDLE = PropKit.getInt("jdbc.minIdle");
		final Integer MAXACTIVEE = PropKit.getInt("jdbc.maxActive");
		final long MAXWAIT = PropKit.getInt("jdbc.maxWait");
		final long TIMEBETWEENEVICTIONRUNSMILLIS = PropKit
				.getInt("jdbc.timeBetweenEvictionRunsMillis");
		final long MINEVICTABLEIDLETIMEMILLIS = PropKit
				.getInt("jdbc.minEvictableIdleTimeMillis");
		final String VALIDATIONQUERY = PropKit.get("jdbc.validationQuery");
		final Boolean TESTWHILEIDLE = PropKit.getBoolean("jdbc.testWhileIdle");
		final Boolean TESTONBORROW = PropKit.getBoolean("jdbc.testOnBorrow");
		final Boolean TESTONRETURN = PropKit.getBoolean("jdbc.testOnReturn");
		final Boolean REMOVEABANDONED = PropKit
				.getBoolean("jdbc.removeAbandoned");
		final long REMOVEABANDONEDTIMEOUT = PropKit
				.getInt("jdbc.removeAbandonedTimeout");
		final Boolean LOGABANDONED = PropKit.getBoolean("jdbc.logAbandoned");

		DruidPlugin druidPlugin = new DruidPlugin(URL, USERNAME, PASSWORD);
		druidPlugin.setDriverClass(DRIVERCLASS);
		druidPlugin.set(INITIALSIZE, MIDIDLE, MAXACTIVEE);
		// 监控统计和防御sql注入
		druidPlugin.setFilters("stat,wall");
		druidPlugin.setMaxWait(MAXWAIT);
		druidPlugin.setTimeBetweenEvictionRunsMillis(
				TIMEBETWEENEVICTIONRUNSMILLIS);
		druidPlugin.setMinEvictableIdleTimeMillis(MINEVICTABLEIDLETIMEMILLIS);
		druidPlugin.setValidationQuery(VALIDATIONQUERY);
		druidPlugin.setTestWhileIdle(TESTWHILEIDLE);
		druidPlugin.setTestOnBorrow(TESTONBORROW);
		druidPlugin.setTestOnReturn(TESTONRETURN);
		druidPlugin.setRemoveAbandoned(REMOVEABANDONED);
		druidPlugin.setRemoveAbandonedTimeoutMillis(REMOVEABANDONEDTIMEOUT);
		druidPlugin.setLogAbandoned(LOGABANDONED);
		plugins.add(druidPlugin);

		// 实体映射
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(
				druidPlugin);
		// 表与实体bean的映射开始
		ORMConfig.init(activeRecordPlugin);
		plugins.add(activeRecordPlugin);

	}

	@Override
	public void configRoute(Routes routes) {
		// 访问路由配置
		try {
			RouteConfig.init(routes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogsUtil.error(WebConfig.class,"路由设置出错...", e);
		}

	}

	/**
	 * JFinal启动完后，将会执行的操作
	 * 
	 * @see com.jfinal.config.JFinalConfig#afterJFinalStart()
	 */
	@Override
	public void afterJFinalStart() {
		LogsUtil.info(WebConfig.class,"JFinal启动完毕...");
		LogsUtil.info(WebConfig.class,"系统启动完成...");
		LogsUtil.info(WebConfig.class,"初始化系统用户...");
		SysUser admin = SysUser.dao
				.findFirst("select * from sys_user where userName = 'admin'");
		if (null == admin) {
			LogsUtil.info(WebConfig.class,"不存在admin超级用户...");
			LogsUtil.info(WebConfig.class,"新增超级用户admin...");
			admin = new SysUser();
			admin.set("groupId", SysGroup.GROUP_SUPER_ADMIN);
			admin.set("userName", "admin");
			admin.set("nickName", "超级管理员");
			admin.set("password", MD5Util.MD5Encode("123321"));
			admin.set("creater", 0);
			admin.set("createTime", new Date());
			admin.set("status", SysUser.STATUS_NORMAL);
			admin.save();
		} else {
			LogsUtil.info(WebConfig.class,"已经存在admin超级用户...");
		}
		LogsUtil.info(WebConfig.class,"初始化系统用户admin完毕...");
	}

	@Override
	public void beforeJFinalStop() {
		// TODO Auto-generated method stub
		super.beforeJFinalStop();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		System.out.println("开始反注册driver...");
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				System.out.println("反注册driver:" + driver);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		immolate();// tomcat 停止时那些线程无法从容器中移除的特殊处理
	}

	private Integer immolate() {
		int count = 0;
		try {
			final Field threadLocalsField = Thread.class
					.getDeclaredField("threadLocals");
			threadLocalsField.setAccessible(true);// 在用反射时访问私有变量
			final Field inheritableThreadLocalsField = Thread.class
					.getDeclaredField("inheritableThreadLocals");
			inheritableThreadLocalsField.setAccessible(true);
			for (final Thread thread : Thread.getAllStackTraces().keySet()) {
				count += clear(threadLocalsField.get(thread));
				count += clear(inheritableThreadLocalsField.get(thread));
				if (thread != null) {
					thread.setContextClassLoader(null);
				}
			}
			System.out
					.println("immolated " + count + " values in ThreadLocals");
		} catch (Exception e) {
			throw new Error("ThreadLocalImmolater.immolate()", e);
		}
		return count;
	}

	@SuppressWarnings("rawtypes")
	private int clear(final Object threadLocalMap) throws Exception {
		if (threadLocalMap == null)
			return 0;
		int count = 0;
		final Field tableField = threadLocalMap.getClass()
				.getDeclaredField("table");
		tableField.setAccessible(true);
		final Object table = tableField.get(threadLocalMap);
		for (int i = 0, length = Array.getLength(table); i < length; ++i) {
			final Object entry = Array.get(table, i);
			if (entry != null) {
				final Object threadLocal = ((WeakReference) entry).get();
				if (threadLocal != null) {
					// System.out.println(i + ":threadLocal:" + threadLocal);
					Array.set(table, i, null);
					++count;
				}
			}
		}
		return count;
	}

}
