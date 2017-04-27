package com.certusnet.xproject.admin.support;

import java.util.List;

import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.common.support.AbstractXTreeBuilder;

/**
 * 资源树结构builder
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午8:04:19
 * @version  	1.0
 */
public class AdminResourceTreeBuilder extends AbstractXTreeBuilder<Long, AdminResource> {

	protected Long getParentTreeNodeId(AdminResource treeObj) {
		return treeObj.getParentResourceId();
	}

	protected Long getTreeNodeId(AdminResource treeObj) {
		return treeObj.getResourceId();
	}

	protected void setSubTreeNodeList(AdminResource treeObj, List<AdminResource> directChildList) {
		treeObj.setSubResourceList(directChildList);
	}

	protected List<AdminResource> getSubTreeNodeList(AdminResource treeObj) {
		return treeObj.getSubResourceList();
	}

	protected void setTreeNodeLevel(AdminResource treeObj, Integer level) {
		treeObj.setResourceLevel(level);
	}

	protected void setTreeNodePath(AdminResource treeNode, String path) {
		treeNode.setTreeNodePath(path);
	}

	protected String getTreeNodePath(AdminResource treeNode) {
		return treeNode.getTreeNodePath();
	}

}
