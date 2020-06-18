package com.jdxl.modules.job.controller;

import com.jdxl.common.annotation.SysLog;
import com.jdxl.common.result.Result;
import com.jdxl.common.utils.*;
import com.jdxl.common.validator.ValidatorUtils;
import com.jdxl.common.utils.PageUtils;
import com.jdxl.modules.job.entity.ScheduleJobEntity;
import com.jdxl.modules.job.service.ScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定时任务
 */
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 定时任务列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:schedule:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = scheduleJobService.queryPage(params);

        return Result.response(new MapUtils().put("page", page));
    }

    /**
     * 定时任务信息
     */
    @GetMapping("/info/{jobId}")
    @RequiresPermissions("sys:schedule:info")
    public Result info(@PathVariable("jobId") Long jobId){
        ScheduleJobEntity schedule = scheduleJobService.selectById(jobId);

        return Result.response(new MapUtils().put("schedule", schedule));
    }

    /**
     * 保存定时任务
     */
    @SysLog("保存定时任务")
    @PostMapping("/save")
    @RequiresPermissions("sys:schedule:save")
    public Result save(@RequestBody ScheduleJobEntity scheduleJob){
        ValidatorUtils.validateEntity(scheduleJob);

        scheduleJobService.save(scheduleJob);

        return Result.response();
    }

    /**
     * 修改定时任务
     */
    @SysLog("修改定时任务")
    @PostMapping("/update")
    @RequiresPermissions("sys:schedule:update")
    public Result update(@RequestBody ScheduleJobEntity scheduleJob){
        ValidatorUtils.validateEntity(scheduleJob);

        scheduleJobService.update(scheduleJob);

        return Result.response();
    }

    /**
     * 删除定时任务
     */
    @SysLog("删除定时任务")
    @PostMapping("/delete")
    @RequiresPermissions("sys:schedule:delete")
    public Result delete(@RequestBody Long[] jobIds){
        scheduleJobService.deleteBatch(jobIds);

        return Result.response();
    }

    /**
     * 立即执行任务
     */
    @SysLog("立即执行任务")
    @PostMapping("/run")
    @RequiresPermissions("sys:schedule:run")
    public Result run(@RequestBody Long[] jobIds){
        scheduleJobService.run(jobIds);

        return Result.response();
    }

    /**
     * 暂停定时任务
     */
    @SysLog("暂停定时任务")
    @PostMapping("/pause")
    @RequiresPermissions("sys:schedule:pause")
    public Result pause(@RequestBody Long[] jobIds){
        scheduleJobService.pause(jobIds);

        return Result.response();
    }

    /**
     * 恢复定时任务
     */
    @SysLog("恢复定时任务")
    @PostMapping("/resume")
    @RequiresPermissions("sys:schedule:resume")
    public Result resume(@RequestBody Long[] jobIds){
        scheduleJobService.resume(jobIds);

        return Result.response();
    }

}
