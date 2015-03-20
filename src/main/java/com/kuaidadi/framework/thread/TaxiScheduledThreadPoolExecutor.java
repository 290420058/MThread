/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidadi.framework.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.kuaidadi.framework.log.ILog;
import com.kuaidadi.framework.log.LogFactory;

/**
 * 
 * @author zhangliang
 * @version $Id: TaxiScheduledThreadPoolExecutor.java, v 0.1 Aug 5, 2014 4:19:17
 *          PM zhangliang Exp $
 */
class TaxiScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    /** 线程池运行日志类 */
    private static final ILog       runLogger  = LogFactory.getLog("treadPoolRunLogger");

    /** 线程变量，用于保存线程开始执行的时间 */
    private final ThreadLocal<Long> startTime  = new ThreadLocal<Long>();

    /** 线程名称 */
    private ThreadLocal<String>     threadName = new ThreadLocal<String>();

    /**
     * @param corePoolSize
     */
    public TaxiScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, new ThreadFactoryBuilder("taxi-schedule-pool").build());
    }

    /**
     * @param corePoolSize
     * @param handler
     */
    public TaxiScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, new ThreadFactoryBuilder("taxi-schedule-pool").build(), handler);
    }

    /**
     * @param corePoolSize
     * @param threadFactory
     */
    public TaxiScheduledThreadPoolExecutor(int corePoolSize, ThreadFactoryBuilder threadFactoryBuilder) {
        super(corePoolSize, threadFactoryBuilder.build());
    }

    /**
     * @param corePoolSize
     * @param threadFactory
     * @param handler
     */
    public TaxiScheduledThreadPoolExecutor(int corePoolSize, ThreadFactoryBuilder threadFactoryBuilder,
                                           RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactoryBuilder.build(), handler);
    }

    /**
     * beforeExecute
     * 
     * @param t
     *            the thread that will run task {@code r}
     * @param r
     *            the task that will be executed
     */
    protected void beforeExecute(Thread t, Runnable r) {
        startTime.set(System.currentTimeMillis());
        String treadName = t.getName();
        threadName.set(treadName);
        super.beforeExecute(t, r);
    }

    /**
     * afterExecute
     * 
     * @param r
     *            the runnable that has completed
     * @param t
     *            the exception that caused termination, or null if execution
     *            completed normally
     */
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        int result = 0;
        if (t != null) {
            result = 1;
        }
        long useTime = System.currentTimeMillis() - startTime.get();
        if (useTime > 0) {
            runLogger.info(threadName.get() + "," + result + "," + useTime);
        }
        startTime.remove();
        threadName.remove();
    }
}
