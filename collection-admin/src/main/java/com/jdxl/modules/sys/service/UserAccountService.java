package com.jdxl.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.sys.entity.UserAccountEntity;

import java.util.Map;

/**
 * 用户账户表
 *
 * @author CodeGen
 * @date 2018-09-21 15:24:19
 */
public interface UserAccountService extends IService<UserAccountEntity> {

    PageUtils queryPage(Map<String, Object> params);

//    int addUserAccountToManage(UserAccountEntity entity);
//
//    int selectCountFromManege(Wrapper<UserAccountEntity> wrapper);
}

