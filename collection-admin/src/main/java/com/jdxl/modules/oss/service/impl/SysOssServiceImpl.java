package com.jdxl.modules.oss.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.common.utils.Query;
import com.jdxl.modules.oss.dao.SysOssDao;
import com.jdxl.modules.oss.entity.SysOssEntity;
import com.jdxl.modules.oss.service.SysOssService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * OSS接口上传是实现类
 */
@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<SysOssEntity> page = this.selectPage(
				new Query<SysOssEntity>(params).getPage()
		);

		return new PageUtils(page);
	}
	
}
