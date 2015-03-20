/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidaidi.framework.thread;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.kuaidadi.framework.log.ILog;
import com.kuaidadi.framework.log.LogFactory;
import com.kuaidadi.framework.thread.TaxiExecutors;
import com.kuaidadi.framework.thread.ThreadFactoryBuilder;

/**
 * 
 * @author zhangliang
 * @version $Id: ScheduleThreadPoolTester.java, v 0.1 Aug 5, 2014 5:19:08 PM
 *          zhangliang Exp $
 */
public class ScheduleThreadPoolTester {

    @Test
    public void testScheduleThreadPool() throws InterruptedException {
        /** 线程池运行日志类 */
        final ILog logger = LogFactory.getLog(CachedThreadPoolTester.class);
        LogFactory.setFlag(LogFactory.getUniqueFlag());
        logger.info("test begin");
        ScheduledExecutorService exec = TaxiExecutors.newScheduledThreadPool(1, new ThreadFactoryBuilder(
            "test-schedule-pool"));

        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logger.info("start to send loggs to flume ");
            }
        }, 5, 10, TimeUnit.SECONDS);

        exec.awaitTermination(10, TimeUnit.MINUTES);
        logger.info("test end");
    }
}
