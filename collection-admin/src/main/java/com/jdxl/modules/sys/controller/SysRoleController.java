package com.jdxl.modules.sys.controller;

import com.jdxl.common.annotation.SysLog;
import com.jdxl.common.result.Result;
import com.jdxl.common.utils.Constant;
import com.jdxl.common.utils.MapUtils;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.validator.ValidatorUtils;
import com.jdxl.modules.sys.entity.SysRoleEntity;
import com.jdxl.modules.sys.service.SysRoleDeptService;
import com.jdxl.modules.sys.service.SysRoleMenuService;
import com.jdxl.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    /**
     * 角色列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public Result list(@RequestParam Map<String, Object> params) {
        //如果不是超级管理员，则只查询自己创建的角色列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("create_user_id", getUserId());
        }

        PageUtils page = sysRoleService.queryPage(params);

        return Result.response(new MapUtils().put("page", page));
    }

    /**
     * 角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public Result select() {
        Map<String, Object> map = new HashMap<>();

        //如果不是超级管理员，则只查询自己所拥有的角色列表
//		if(getUserId() != Constant.SUPER_ADMIN){
        map.put("create_user_id", 1);
//		}
        List<SysRoleEntity> list = sysRoleService.selectByMap(map);

        return Result.response(new MapUtils().put("list", list));
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public Result info(@PathVariable("roleId") Long roleId) {
        // 查询角色信息与所属机构名称
        SysRoleEntity role = sysRoleService.selectRoleAndDeptNameById(roleId);

        // 查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        // 查询角色对应的机构
        List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
        role.setDeptIdList(deptIdList);

        return Result.response(new MapUtils().put("role", role));
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public Result save(@RequestBody SysRoleEntity role) {
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        sysRoleService.save(role);

        return Result.response();
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public Result update(@RequestBody SysRoleEntity role) {
        ValidatorUtils.validateEntity(role);

        role.setCreateUserId(getUserId());
        sysRoleService.update(role);

        return Result.response();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public Result delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);

        return Result.response();
    }
}
