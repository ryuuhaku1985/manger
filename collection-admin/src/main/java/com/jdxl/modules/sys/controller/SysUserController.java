package com.jdxl.modules.sys.controller;

import com.jdxl.common.annotation.SysLog;
import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.SysMsgInfo;
import com.jdxl.common.result.Result;
import com.jdxl.common.utils.Constant;
import com.jdxl.common.utils.MapUtils;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.validator.Assert;
import com.jdxl.common.validator.ValidatorUtils;
import com.jdxl.common.validator.group.AddGroup;
import com.jdxl.common.validator.group.UpdateGroup;
import com.jdxl.modules.sys.entity.SysUserEntity;
import com.jdxl.modules.sys.form.PasswordForm;
import com.jdxl.modules.sys.service.SysUserRoleService;
import com.jdxl.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public Result list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		if(getDeptId() != Constant.SUPER_ADMIN){
			params.put("create_user_id", getUserId());
		}
		PageUtils page = sysUserService.queryPage(params);

		return Result.response(new MapUtils().put("page", page));
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public Result info(){
		return Result.response(new MapUtils().put("user", getUser()));
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	public Result password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), SysMsgInfo.SYS0011);
		
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			throw new BizException(SysMsgInfo.SYS0012);
		}

		return Result.response();
	}
	
	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public Result info(@PathVariable("userId") Long userId){
		// 查询用户详细信息与所属机构名称
		SysUserEntity user = sysUserService.selectRoleAndDeptNameById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return Result.response(new MapUtils().put("user", user));
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public Result save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserId());
		sysUserService.save(user);

		return Result.response();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public Result update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.update(user);

		return Result.response();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public Result delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			throw new BizException(SysMsgInfo.SYS0013);
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			throw new BizException(SysMsgInfo.SYS0014);
		}
		
		sysUserService.deleteBatch(userIds);
		
		return Result.response();
	}
}
