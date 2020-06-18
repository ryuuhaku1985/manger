package com.jdxl.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 机构管理
 */
public interface SysDeptService extends IService<SysDeptEntity> {

	List<SysDeptEntity> queryList(Map<String, Object> map);

	/**
	 * 查询机构信息与上级机构名称
	 */
	SysDeptEntity selectDeptAndParentDeptNameById(Long deptId);

	/**
	 * 查询子机构ID列表
	 * @param parentId  上级机构ID
	 */
	List<Long> queryDetpIdList(Long parentId);

	/**
	 * 获取子机构ID，用于数据过滤
	 */
	List<Long> getSubDeptIdList(Long deptId);

}
