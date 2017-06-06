package com.certusnet.xproject.admin.service.impl;

import java.util.List;
import java.util.function.Consumer;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.certusnet.xproject.admin.consts.em.AdminRoleTypeEnum;
import com.certusnet.xproject.admin.mapper.AdminRoleMapper;
import com.certusnet.xproject.admin.model.AdminResource;
import com.certusnet.xproject.admin.model.AdminRole;
import com.certusnet.xproject.admin.service.AdminRoleService;
import com.certusnet.xproject.common.support.BusinessAssert;
import com.certusnet.xproject.common.support.OrderBy;
import com.certusnet.xproject.common.support.Pager;
import com.certusnet.xproject.common.support.PagingList;
import com.certusnet.xproject.common.support.ValidationAssert;
import com.certusnet.xproject.common.util.ModelDecodeUtils;
import com.certusnet.xproject.common.util.CollectionUtils;

@Service("adminRoleService")
public class AdminRoleServiceImpl implements AdminRoleService {

	public static final Consumer<AdminRole> ADMIN_ROLE_DECODER = model -> {
		if(model.getRoleType() != null){
			AdminRoleTypeEnum em = AdminRoleTypeEnum.getType(model.getRoleType());
			if(em != null){
				model.setRoleTypeName(em.getTypeName());
			}
		}
	};
	
	@Autowired
	private AdminRoleMapper adminRoleMapper;
	
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void createRole(AdminRole role) {
		ValidationAssert.notNull(role, "参数不能为空!");
		try {
			adminRoleMapper.insertRole(role);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_NAME"), "新增角色失败,该角色名称已经存在!");
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_CODE"), "新增角色失败,该角色代码已经存在!");
		}
	}

	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void updateRole(AdminRole role) {
		ValidationAssert.notNull(role, "参数不能为空!");
		ValidationAssert.notNull(role.getRoleId(), "角色ID不能为空!");
		AdminRole prole = adminRoleMapper.selectThinRoleById(role.getRoleId());
		ValidationAssert.notNull(prole, "该角色已经不存在了!");
		try {
			adminRoleMapper.updateRole(role);
		} catch(DuplicateKeyException e) {
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_NAME"), "修改角色失败,该角色名称已经存在!");
			BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("ROLE_CODE"), "修改角色失败,该角色代码已经存在!");
		}
	}

	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void deleteRoleById(Long roleId) {
		ValidationAssert.notNull(roleId, "角色ID不能为空!");
		AdminRole role = adminRoleMapper.selectThinRoleById(roleId);
		ValidationAssert.notNull(role, "该角色已经不存在了!");
		BusinessAssert.isTrue(!AdminRoleTypeEnum.ADMIN_ROLE_TYPE_SYSTEM.getTypeCode().equals(role.getRoleType()), "删除角色失败,系统角色不允许删除!");
		BusinessAssert.isTrue(!role.isInuse(), "删除角色失败,该角色已经在使用不允许删除!");
		adminRoleMapper.deleteRoleById(roleId); //删除角色信息
		adminRoleMapper.deleteRoleResourcesByRoleId(roleId); //删除该角色下的所有资源关系
	}

	public AdminRole getRoleById(Long roleId) {
		return ModelDecodeUtils.decodeModel(adminRoleMapper.selectRoleById(roleId), ADMIN_ROLE_DECODER);
	}

	public PagingList<AdminRole> getRoleList(AdminRole condition, Pager pager, OrderBy orderby) {
		List<AdminRole> dataList = ModelDecodeUtils.decodeModel(adminRoleMapper.selectRoleList(condition, orderby, new RowBounds(pager.getOffset(), pager.getLimit())), ADMIN_ROLE_DECODER);
		return new PagingList<AdminRole>(dataList, adminRoleMapper.countRoleList(condition));
	}

	public List<AdminResource> getResourceListByRoleId(Long roleId) {
		return adminRoleMapper.selectResourceListByRoleId(roleId);
	}

	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void configRoleResources(Long roleId, List<Long> resourceIdList, Long optUserId, String optTime) {
		AdminRole role = adminRoleMapper.selectThinRoleById(roleId);
		ValidationAssert.notNull(role, "该角色已经不存在了!");
		adminRoleMapper.deleteRoleResourcesByRoleId(roleId);
		if(!CollectionUtils.isEmpty(resourceIdList)){
			adminRoleMapper.insertRoleResources(roleId, resourceIdList, optUserId, optTime);
		}
	}

}
