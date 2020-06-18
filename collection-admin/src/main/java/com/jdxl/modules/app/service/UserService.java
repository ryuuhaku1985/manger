package com.jdxl.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.modules.app.form.LoginForm;
import com.jdxl.modules.app.entity.UserEntity;

/**
 * 用户
 */
public interface UserService extends IService<UserEntity> {

	UserEntity queryByMobile(String mobile);

	/**
	 * 用户登录
	 * @param form    登录表单
	 * @return        返回用户ID
	 */
	long login(LoginForm form);
}
