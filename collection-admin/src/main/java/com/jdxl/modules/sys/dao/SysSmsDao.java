package com.jdxl.modules.sys.dao;

import com.jdxl.modules.sys.entity.SysSmsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 
 * 
 * @author CodeGen
 * @date 2019-08-14 16:42:21
 */
@Mapper
public interface SysSmsDao extends BaseMapper<SysSmsEntity> {

    SysSmsEntity selectbymobile(Map<String, Object> map);
	
}
