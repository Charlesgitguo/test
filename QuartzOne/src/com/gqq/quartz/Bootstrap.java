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
            // ��ȡSchedulerʵ��
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            Date startTime = DateBuilder.nextGivenSecondDate(null,30); 

            // ��������
            JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job4", "group1").build();

            // ����ʱ���
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5).repeatForever();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger4", "group1")
                    .startNow().withSchedule(simpleScheduleBuilder).build();
            
            SimpleScheduleBuilder simpleScheduleBuilder1 = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(10).repeatForever();
            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger4", "group1")
                    .startAt(startTime)
                    .withSchedule(simpleScheduleBuilder1).build();

            // ����Scheduler���Ŵ���
            scheduler.scheduleJob(job, trigger1);
            
            /* Ϊ�۲�������У�������������˯��3���Ӳż����������У�����һ�������ǡ��ر�Scheduler���� */
            try {
                TimeUnit.MINUTES.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // �ر�Scheduler
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
	}

}
