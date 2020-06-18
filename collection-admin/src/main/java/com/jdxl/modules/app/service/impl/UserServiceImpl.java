package com.jdxl.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdxl.common.exception.BizException;
import com.jdxl.common.validator.Assert;
import com.jdxl.modules.app.dao.UserDao;
import com.jdxl.modules.app.form.LoginForm;
import com.jdxl.modules.app.message.AppMsgInfo;
import com.jdxl.modules.app.service.UserService;
import com.jdxl.modules.app.entity.UserEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

	@Override
	public UserEntity queryByMobile(String mobile) {
		UserEntity userEntity = new UserEntity();
		userEntity.setMobile(mobile);
		return baseMapper.selectOne(userEntity);
	}

	@Override
	public long login(LoginForm form) {
		UserEntity user = queryByMobile(form.getMobile());
		Assert.isNull(user, AppMsgInfo.USR0001);

		//密码错误
		if(!user.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))){
			throw new BizException(AppMsgInfo.USR0001);
		}

		return user.getUserId();
	}
}
