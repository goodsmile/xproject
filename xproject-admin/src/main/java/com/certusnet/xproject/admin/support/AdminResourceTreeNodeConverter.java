package com.certusnet.xproject.admin.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.common.support.TreeNodeConverter;

/**
 * 基于ElementUI.Tree组件的资源节点树转换器
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午8:03:51
 * @version  	1.0
 */
public class AdminResourceTreeNodeConverter implements TreeNodeConverter<AdminResource, Map<String,Object>> {

	public AdminResourceTreeNodeConverter() {
		super();
	}

	public Map<String, Object> convertTreeNode(AdminResource targetTreeNode) {
		Map<String,Object> treeNodeMap = new LinkedHashMap<String,Object>();
		treeNodeMap.put("id", targetTreeNode.getResourceId());
		treeNodeMap.put("label", targetTreeNode.getResourceName());
		treeNodeMap.put("parentResourceId", targetTreeNode.getParentResourceId());
		treeNodeMap.put("parentResourceName", targetTreeNode.getParentResourceName());
		treeNodeMap.put("siblingsIndex", targetTreeNode.getSiblingsIndex());
		treeNodeMap.put("actionType", targetTreeNode.getActionType());
		treeNodeMap.put("actionTypeName", targetTreeNode.getActionTypeName());
		treeNodeMap.put("resourceUrl", targetTreeNode.getResourceUrl());
		treeNodeMap.put("resourceType", targetTreeNode.getResourceType());
		treeNodeMap.put("resourceIcon", targetTreeNode.getResourceIcon());
		treeNodeMap.put("permissionExpression", targetTreeNode.getPermissionExpression());
		return treeNodeMap;
	}

	public void setSubTreeNodeList(Map<String, Object> resultTreeNode, List<Map<String, Object>> subTreeNodeList) {
		resultTreeNode.put("children", subTreeNodeList);
	}
	
}