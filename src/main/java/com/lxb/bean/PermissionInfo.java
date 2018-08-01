package com.lxb.bean;

import java.util.ArrayList;
import java.util.List;

import com.lxb.annotation.Permission;

/**
 * 权限实体类
 * 
 * @since 2018年4月16日 下午8:19:53
 * @author LXB
 */
public class PermissionInfo {
	public static final int TYPE_NOTE = 1;
	public static final int TYPE_MENU = 2;
	public static final int TYPE_METHOD = 3;

	private int id;
	private int parent;
	/** 权限标识或访问url */
	private String accessUrl;
	/** 权限名 */
	private String name;
	/*** 节点还是菜单还是方法 */
	private int type;
	/** 显示顺序 */
	private int sort;
	/** 首页左侧菜单icon */
	private String icon;
	/** 该权限(节点)下的Permission */
	private List<PermissionInfo> subPermission;

	public PermissionInfo(Permission p, String actionKey) {
		super();
		this.id = p.id();
		this.parent = p.parent();
		this.accessUrl = actionKey;
		this.name = p.name();
		this.type = p.type();
		this.sort = p.sort();
		this.icon = p.icon();
		this.subPermission = new ArrayList<PermissionInfo>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<PermissionInfo> getSubPermission() {
		return subPermission;
	}

	public void setSubPermission(List<PermissionInfo> subPermission) {
		this.subPermission = subPermission;
	}

}
