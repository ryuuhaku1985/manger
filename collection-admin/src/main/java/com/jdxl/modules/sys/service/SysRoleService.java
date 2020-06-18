package com.jdxl.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void save(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);

	/**
	 * 查询角色信息与所属机构名称
	 */
	SysRoleEntity selectRoleAndDeptNameById(Long roleId);

	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
