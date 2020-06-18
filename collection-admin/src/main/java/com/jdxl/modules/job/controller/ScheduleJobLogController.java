package com.jdxl.modules.job.controller;

import com.jdxl.common.result.Result;
import com.jdxl.common.utils.MapUtils;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.job.entity.ScheduleJobLogEntity;
import com.jdxl.modules.job.service.ScheduleJobLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定时任务日志
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
	@Autowired
	private ScheduleJobLogService scheduleJobLogService;
	
	/**
	 * 定时任务日志列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:schedule:log")
	public Result list(@RequestParam Map<String, Object> params){
		PageUtils page = scheduleJobLogService.queryPage(params);
		
		return Result.response(new MapUtils().put("page", page));
	}
	
	/**
	 * 定时任务日志信息
	 */
	@GetMapping("/info/{logId}")
	public Result info(@PathVariable("logId") Long logId){
		ScheduleJobLogEntity log = scheduleJobLogService.selectById(logId);
		
		return Result.response(new MapUtils().put("log", log));
	}
}
