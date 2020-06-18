package com.jdxl.modules.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
}
