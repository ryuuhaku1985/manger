package com.jdxl.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jdxl.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统日志
 */
@Repository
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {
	
}
