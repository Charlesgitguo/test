package test;

import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;



public class Main {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws SchedulerException {
		
       startSchedule();
		//resumeJob();
        
	}
	
	public static void startSchedule(){
		try {
			// 1������һ��JobDetailʵ����ָ��Quartz
			JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
			// ����ִ����
					.withIdentity("job1_1", "jGroup1")
					// ��������������
					.build();
			
			// 2������Trigger
			
			SimpleScheduleBuilder builder = SimpleScheduleBuilder
					.simpleSchedule()
					// ����ִ�д���
				    .repeatSecondlyForTotalCount(10);
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("trigger1_1", "tGroup1").startNow()
					.withSchedule(builder).build();
			// 3������Scheduler
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			// 4������ִ��
			scheduler.scheduleJob(jobDetail, trigger);
			
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			scheduler.shutdown();

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public static void resumeJob() {
		try {

			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			// �ٻ�ȡ�����������еĴ�������
			List<String> triggerGroups = scheduler.getTriggerGroupNames();
			// �����»ָ���tgroup1���У���Ϊtrigger1_1������������
			for (int i = 0; i < triggerGroups.size(); i++) {
				List<String> triggers = scheduler.getTriggerGroupNames();
				for (int j = 0; j < triggers.size(); j++) {
					Trigger tg = scheduler.getTrigger(new TriggerKey(triggers
							.get(j), triggerGroups.get(i)));
					// ��-1:���������ж�
					if (tg instanceof SimpleTrigger
							&& tg.getDescription().equals("tgroup1.trigger1_1")) {
						// ��-1:�ָ�����
						scheduler.resumeJob(new JobKey(triggers.get(j),
								triggerGroups.get(i)));
					}
				}

			}
			scheduler.start();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
