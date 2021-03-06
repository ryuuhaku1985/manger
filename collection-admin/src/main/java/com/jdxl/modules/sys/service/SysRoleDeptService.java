package com.jdxl.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.modules.sys.entity.SysRoleDeptEntity;

import java.util.List;

/**
 * 角色与机构对应关系
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity> {
	
	void saveOrUpdate(Long roleId, List<Long> deptIdList);
	
	/**
	 * 根据角色ID，获取机构ID列表
	 */
	List<Long> queryDeptIdList(Long[] roleIds) ;

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
