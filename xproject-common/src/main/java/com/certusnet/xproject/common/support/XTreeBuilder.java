package com.certusnet.xproject.common.support;

import java.util.List;

/**
 * java树形数据构造器
 * 
 * @param <I>
 * @param <T>
 * @author 	pengpeng
 * @date   		2017年3月29日 下午3:09:25
 * @version 	1.0
 */
public interface XTreeBuilder<I,T extends Comparable<T>> {
	
	/**
	 * 根据根treeNodeId及全部TreeNode列表来渲染一颗具有父子关系的对象树
	 * @param rootTreeNodeId
	 * @param allTreeNodeList
	 * @return
	 */
	public List<T> buildObjectTree(I rootTreeNodeId, List<T> allTreeNodeList);
	
	/**
	 * 根据根treeNodeId及全部TreeNode列表来渲染一颗具有父子关系的对象树,同时提供对树节点数据类型的转换
	 * @param rootTreeNodeId
	 * @param allTreeNodeList
	 * @param treeNodeBuilder
	 * @return
	 */
	public <R> List<R> buildObjectTree(I rootTreeNodeId, List<T> allTreeNodeList, TreeNodeConverter<T,R> treeNodeBuilder);
	
	public List<T> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList);
	
	public <R> List<R> getDirectChildNodeList(I parentTreeNodeId, List<T> allTreeNodeList, TreeNodeConverter<T,R> treeNodeBuilder);
	
}
