package com.jdxl.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.sys.entity.SysSmsEntity;

import java.util.Map;

/**
 * 
 *
 * @author CodeGen
 * @date 2019-08-14 16:42:21
 */
public interface SysSmsService extends IService<SysSmsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    SysSmsEntity selectbymobile(Map<String, Object> params);
}

