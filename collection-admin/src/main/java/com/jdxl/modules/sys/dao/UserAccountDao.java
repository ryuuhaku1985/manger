package com.jdxl.modules.sys.dao;

import com.jdxl.modules.sys.entity.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户账户表
 * 
 * @author CodeGen
 * @date 2018-09-21 15:24:19
 */
@Repository
@Mapper
public interface UserAccountDao extends BaseMapper<UserAccountEntity> {
	
}
