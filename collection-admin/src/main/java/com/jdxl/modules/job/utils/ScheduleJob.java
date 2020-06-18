package com.jdxl.modules.job.utils;

import com.jdxl.common.utils.Constant;
import com.jdxl.common.utils.SpringContextUtils;
import com.jdxl.modules.job.entity.ScheduleJobEntity;
import com.jdxl.modules.job.entity.ScheduleJobLogEntity;
import com.jdxl.modules.job.service.ScheduleJobLogService;
import com.jdxl.modules.job.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 定时任务
 */
@Slf4j
public class ScheduleJob extends QuartzJobBean {
	private ExecutorService service = Executors.newSingleThreadExecutor();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap()
				.get(ScheduleJobEntity.JOB_PARAM_KEY);

		//获取spring bean
		ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtils.getBean("scheduleJobLogService");
		ScheduleJobService scheduleJobService = (ScheduleJobService) SpringContextUtils.getBean("scheduleJobService");
		//数据库保存执行记录
		ScheduleJobLogEntity logEntity = new ScheduleJobLogEntity();
		logEntity.setJobId(scheduleJob.getJobId());
		logEntity.setBeanName(scheduleJob.getBeanName());
		logEntity.setMethodName(scheduleJob.getMethodName());
		logEntity.setParams(scheduleJob.getParams());
		logEntity.setCreateTime(new Date());

		//任务开始时间
		long startTime = System.currentTimeMillis();

		try {
			//执行任务
			log.info("任务准备执行，任务ID：{}", scheduleJob.getJobId());
			ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(),
					scheduleJob.getMethodName(), scheduleJob.getParams());
			Future<?> future = service.submit(task);
			future.get();
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			logEntity.setTimes((int)times);
			//任务状态    0：成功    1：失败
			logEntity.setStatus(0);
			log.info("任务执行完毕，任务ID:{} 总共耗时: {}毫秒", scheduleJob.getJobId(), times);
		} catch (Exception e) {
			log.error("任务执行失败，任务ID:{}", scheduleJob.getJobId(), e);

			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			logEntity.setTimes((int)times);

			//任务状态    0：成功    1：失败
			logEntity.setStatus(1);
			logEntity.setError(StringUtils.substring(e.toString(), 0, 2000));
		}finally {
			scheduleJobLogService.insert(logEntity);
			//如果是只执行一次的任务，无论成功还是失败，结束之后将此任务暂停
			if (Constant.ScheduleType.ONETIME.getValue() == scheduleJob.getJobType()) {
				scheduleJob.setStatus(Constant.ScheduleStatus.FINISHED.getValue());
				scheduleJobService.update(scheduleJob);
			}
		}
	}
}
