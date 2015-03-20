/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidaidi.framework.thread;

import java.util.concurrent.ExecutorService;

import org.junit.Test;

import com.kuaidadi.framework.log.ILog;
import com.kuaidadi.framework.log.LogFactory;
import com.kuaidadi.framework.thread.TaxiExecutors;
import com.kuaidadi.framework.thread.ThreadFactoryBuilder;

/**
 * 
 * @author zhangliang
 * @version $Id: FixedThreadPoolTester.java, v 0.1 Aug 5, 2014 5:16:36 PM
 *          zhangliang Exp $
 */
public class FixedThreadPoolTester {

    @Test
    public void testFixedThreadPool() throws InterruptedException {
        /** 线程池运行日志类 */
        final ILog logger = LogFactory.getLog(CachedThreadPoolTester.class);
        LogFactory.setFlag(LogFactory.getUniqueFlag());
        logger.info("test begin");
        ExecutorService exec = TaxiExecutors.newFixedThreadPool(10, new ThreadFactoryBuilder("test-fix-pool"));

        for (int k = 0; k < 20; k++) {
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    logger.info("start to send loggs to flume ");
                }
            });
        }

        exec.shutdown();
        while (true) {
            if (exec.isTerminated()) {
                logger.info("Thread end running!");
                break;
            }
            Thread.sleep(200);
        }

        logger.info("test end");
    }
}
