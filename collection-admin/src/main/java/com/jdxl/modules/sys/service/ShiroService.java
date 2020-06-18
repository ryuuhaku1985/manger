package com.jdxl.modules.sys.service;

import com.jdxl.modules.sys.entity.SysUserEntity;
import com.jdxl.modules.sys.entity.SysUserTokenEntity;

import java.util.Set;

/**
 * shiro接口
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     * @param userId 用户Id
     * @return 权限列表
     */
    Set<String> getUserPermissions(long userId);

    /**
     * 获取Token详细信息
     * @param token 客户端token
     * @return 用户Token详细信息
     */
    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId 用户Id
     * @return 用户详细信息
     */
    SysUserEntity queryUser(Long userId);
}
