package com.certusnet.xproject.admin.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.common.support.TreeNodeConverter;

/**
 * 基于ElementUI.NavMenu组件的资源菜单转换器
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午8:03:51
 * @version  	1.0
 */
public class AdminResourceNavMenuNodeConverter implements TreeNodeConverter<AdminResource, Map<String,Object>> {

	public AdminResourceNavMenuNodeConverter() {
		super();
	}

	public Map<String, Object> convertTreeNode(AdminResource targetTreeNode) {
		Map<String,Object> treeNodeMap = new LinkedHashMap<String,Object>();
		treeNodeMap.put("menuId", targetTreeNode.getResourceId());
		treeNodeMap.put("menuName", targetTreeNode.getResourceName());
		treeNodeMap.put("menuUrl", targetTreeNode.getResourceUrl());
		treeNodeMap.put("menuIcon", targetTreeNode.getResourceIcon());
		treeNodeMap.put("menuLevel", targetTreeNode.getResourceLevel());
		treeNodeMap.put("menuPath", targetTreeNode.getTreeNodePath());
		treeNodeMap.put("parentMenuId", targetTreeNode.getParentResourceId());
		return treeNodeMap;
	}

	public void setSubTreeNodeList(Map<String, Object> resultTreeNode, List<Map<String, Object>> subTreeNodeList) {
		resultTreeNode.put("subMenuList", subTreeNodeList);
	}
	
}