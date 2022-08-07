package com.jiao.testproject.testproject.quartz;

import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


public class MyScheduler {

    // 1、创建调度器Scheduler
    DirectSchedulerFactory directSchedulerFactory = DirectSchedulerFactory.getInstance();
    Scheduler scheduler = directSchedulerFactory.getScheduler();
    public MyScheduler() throws SchedulerException {
        // 2、创建JobDetail实例，并与PrintJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(PrintJob.class)
                .withIdentity("job", "group").build();
        // 3、构建Trigger实例，每隔1s执行一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger", "triggerGroup")
                .startNow()//立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)//每隔1s执行一次
                        .repeatForever()).build();//一直执行
        //4、Scheduler绑定Job和Trigger，并执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();
    }


}
