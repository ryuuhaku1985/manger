package com.jdxl.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 机构管理
 */
@Data
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//机构ID
	@TableId
	private Long deptId;
	//上级机构ID，一级机构为0
	private Long parentId;
	//机构名称
	private String name;
	//上级机构名称
	@TableField(exist=false)
	private String parentName;
	//排序
	private Integer orderNum;

	@TableLogic
	private Integer delFlag;
	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	@TableField(exist=false)
	private List<?> list;

}
