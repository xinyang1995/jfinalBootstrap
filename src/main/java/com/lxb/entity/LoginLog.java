/** 广州哇宝信息技术有限公司 */
package com.lxb.entity;

import com.jfinal.plugin.activerecord.Model;
import com.lxb.annotation.TableBind;

/**
 * 后台系统登录记录
 * 
 * @since 2018年8月14日 下午8:08:16
 * @author LXB
 */
@TableBind(tableName = "login_log")
public class LoginLog extends Model<LoginLog> {
	private static final long serialVersionUID = 1L;
	// 全类共享常量Dao
	public static final LoginLog dao = new LoginLog();
}
