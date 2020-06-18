package com.jdxl.modules.sys.controller;

import com.jdxl.common.annotation.SysLog;
import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.SysMsgInfo;
import com.jdxl.common.result.Result;
import com.jdxl.common.utils.Constant;
import com.jdxl.common.utils.MapUtils;
import com.jdxl.modules.sys.entity.SysDeptEntity;
import com.jdxl.modules.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


/**
 * 机构管理
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dept:list")
	public List<SysDeptEntity> list(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		return deptList;
	}

	/**
	 * 选择机构(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:dept:select")
	public Result select(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

		//添加一级机构
		if(getUserId() == Constant.SUPER_ADMIN){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId(0L);
			root.setName("Root");
			root.setParentId(-1L);
			root.setOpen(true);
			deptList.add(root);
		}

		return Result.response(new MapUtils().put("deptList", deptList));
	}

	/**
	 * 上级机构Id(管理员则为0)
	 */
	@RequestMapping("/info")
	@RequiresPermissions("sys:dept:list")
	public Result info(){
		long deptId = 0;
		if(getUserId() != Constant.SUPER_ADMIN){
			List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
			Long parentId = null;
			for(SysDeptEntity sysDeptEntity : deptList){
				if(parentId == null){
					parentId = sysDeptEntity.getParentId();
					continue;
				}

				if(parentId > sysDeptEntity.getParentId().longValue()){
					parentId = sysDeptEntity.getParentId();
				}
			}
			deptId = parentId;
		}

		return Result.response(new MapUtils().put("deptId", deptId));
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{deptId}")
	@RequiresPermissions("sys:dept:info")
	public Result info(@PathVariable("deptId") Long deptId){
		// 查询机构信息与上级机构名称
		SysDeptEntity dept = sysDeptService.selectDeptAndParentDeptNameById(deptId);

		return Result.response(new MapUtils().put("dept", dept));
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存机构")
	@RequestMapping("/save")
	@RequiresPermissions("sys:dept:save")
	public Result save(@RequestBody SysDeptEntity dept){
		sysDeptService.insert(dept);
		
		return Result.response();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改机构")
	@RequestMapping("/update")
	@RequiresPermissions("sys:dept:update")
	public Result update(@RequestBody SysDeptEntity dept){
		sysDeptService.updateById(dept);

		return Result.response();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除机构")
	@RequestMapping("/delete/{deptId}")
	@RequiresPermissions("sys:dept:delete")
	public Result delete(long deptId){
		//判断是否有子机构
		List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			throw new BizException(SysMsgInfo.SYS0022);
		}

		sysDeptService.deleteById(deptId);

		return Result.response();
	}
	
}
