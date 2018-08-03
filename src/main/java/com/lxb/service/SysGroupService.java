package com.lxb.service;

import com.lxb.entity.SysGroup;

/**
 * 系统用户分组service层
 * 
 * @since 2018年8月1日 下午2:05:46
 * @author LXB
 */
public class SysGroupService {

	private static volatile SysGroupService service;

	private SysGroupService() {
	}

	public static SysGroupService getInstance() {
		if (null == service) {
			synchronized (SysGroupService.class) {
				if (null == service) {
					service = new SysGroupService();
				}
			}
		}
		return service;
	}

	/**
	 * 根据Id获取用户组
	 * 
	 * @param groupId
	 * @return
	 * @since 2018年8月1日 下午2:07:20
	 * @author LXB
	 */
	public SysGroup getSysGroupById(int groupId) {
		SysGroup sysgroup = SysGroup.dao.findById(groupId);
		return sysgroup;
	}

}
