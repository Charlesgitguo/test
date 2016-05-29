package com.gqq.quartz;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Bootstrap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            // 获取Scheduler实例
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            Date startTime = DateBuilder.nextGivenSecondDate(null,30); 

            // 具体任务
            JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job4", "group1").build();

            // 触发时间点
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5).repeatForever();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger4", "group1")
                    .startNow().withSchedule(simpleScheduleBuilder).build();
            
            SimpleScheduleBuilder simpleScheduleBuilder1 = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(10).repeatForever();
            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger4", "group1")
                    .startAt(startTime)
                    .withSchedule(simpleScheduleBuilder1).build();

            // 交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger1);
            
            /* 为观察程序运行，此设置主程序睡眠3分钟才继续往下运行（因下一个步骤是“关闭Scheduler”） */
            try {
                TimeUnit.MINUTES.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 关闭Scheduler
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
	}

}
