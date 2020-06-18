package com.jdxl.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jdxl.modules.sys.entity.SysCaptchaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 验证码
 */
@Repository
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
