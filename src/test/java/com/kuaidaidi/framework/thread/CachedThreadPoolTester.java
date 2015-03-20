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
 * @version $Id: CachedThreadPoolTester.java, v 0.1 Aug 5, 2014 5:17:21 PM
 *          zhangliang Exp $
 */
public class CachedThreadPoolTester {

    @Test
    public void testCachedThreadPool() throws InterruptedException {
        /** 线程池运行日志类 */
        final ILog logger = LogFactory.getLog(CachedThreadPoolTester.class);
        LogFactory.setFlag(LogFactory.getUniqueFlag());
        logger.info("test begin");
        ExecutorService exec = TaxiExecutors.newCachedThreadPool(new ThreadFactoryBuilder("test-cache-pool"));
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
