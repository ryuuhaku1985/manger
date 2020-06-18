package com.jdxl.modules.oss.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.oss.entity.SysOssEntity;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.oss.entity.SysOssEntity;
import com.jdxl.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * OSS文件上传
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
