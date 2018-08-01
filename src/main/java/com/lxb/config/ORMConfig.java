package com.lxb.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.lxb.annotation.TableBind;
import com.lxb.util.LogsUtil;
import com.lxb.util.PackageUtil;

/**
 * 配置表与实体类的映射关系
 * 
 * @since 2018年4月16日 下午7:59:36
 * @author LXB
 */
public class ORMConfig {

	private static final String PACK_MODEL_PATH = "com.lxb.entity";

	@SuppressWarnings("unchecked")
	public static void init(ActiveRecordPlugin arp) {
		String packages = PACK_MODEL_PATH;
		List<String> actionPackages = Arrays.asList(packages.split(","));
		LogsUtil.info(ORMConfig.class,"加载对象与表映射关系开始...");
		for (int i = 0; i < actionPackages.size(); i++) {
			Set<Class<?>> classSet = PackageUtil
					.getClasses(actionPackages.get(i));
			for (Class<?> c : classSet) {
				if (c.isAnnotationPresent(TableBind.class)
						&& Model.class.isAssignableFrom(c)) {
					TableBind tableBind = c.getAnnotation(TableBind.class);
					String tableName = tableBind.tableName();
					String pk = tableBind.pk();
					arp.addMapping(tableName, pk,
							(Class<? extends Model<?>>) c);
				}
			}
		}
		LogsUtil.info(ORMConfig.class,"加载对象与表映射关系完毕!");
	}

}
