/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidadi.framework.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.kuaidadi.framework.log.ILog;
import com.kuaidadi.framework.log.LogFactory;
import com.kuaidadi.framework.thread.bean.TaxiFutureTask;

/**
 * 
 * @author zhangliang
 * @version $Id: TaxiThreadPoolExecutor.java, v 0.1 Aug 1, 2014 11:37:08 AM
 *          zhangliang Exp $
 */
public class TaxiThreadPoolExecutor extends ThreadPoolExecutor {

    /** 线程池运行日志类 */
    private static final ILog        runLogger  = LogFactory.getLog("treadPoolRunLogger");

    /** 线程统计日志类 */
    private static final ILog        staLogger  = LogFactory.getLog("treadPoolStaLogger");

    /** 线程变量，用于保存线程开始执行的时间 */
    private final ThreadLocal<Long>  startTime  = new ThreadLocal<Long>();

    /** 线程名称 */
    private ThreadLocal<String>      threadName = new ThreadLocal<String>();

    /** 线程池名称 */
    private String                   threadPoolName;

    /** 线程已执行的任务数 */
    private long                     executedTaskCount;

    /** 线程池统计任务 */
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Returns a <tt>RunnableFuture</tt> for the given runnable and default
     * value.
     */
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new TaxiFutureTask<T>(runnable, value);
    }

    /**
     * Returns a <tt>RunnableFuture</tt> for the given callable task.
     */
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new TaxiFutureTask<T>(callable);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    public TaxiThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
            new ThreadFactoryBuilder("taxi-pool-thread"), new AbortPolicy());
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param handler
     */
    public TaxiThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
            new ThreadFactoryBuilder("taxi-pool-thread"), handler);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     */
    public TaxiThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, ThreadFactoryBuilder threadFactoryBuilder) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactoryBuilder, new AbortPolicy());
    }

    /**
     * Executes the given task sometime in the future. The task may execute in a
     * new thread or in an existing pooled thread.
     * 
     * If the task cannot be submitted for execution, either because this
     * executor has been shutdown or because its capacity has been reached, the
     * task is handled by the current {@code RejectedExecutionHandler}.
     * 
     * @param command
     *            the task to execute
     * @throws RejectedExecutionException
     *             at discretion of {@code RejectedExecutionHandler}, if the
     *             task cannot be accepted for execution
     * @throws NullPointerException
     *             if {@code command} is null
     */
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        RunnableFuture<Void> ftask = newTaskFor(command, null);
        super.execute(ftask);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     * @param handler
     */
    public TaxiThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue, ThreadFactoryBuilder threadFactoryBuilder,
                                  RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactoryBuilder.build(), handler);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder.build());
        scheduledExecutorService.scheduleWithFixedDelay(new ThreadPoolStatistics(), 5, 60, TimeUnit.SECONDS);
        executedTaskCount = 0;
        threadPoolName = threadFactoryBuilder.getName();
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
            StringBuffer sb = new StringBuffer();
            sb.append(threadName.get());
            sb.append(",");
            sb.append(result);
            sb.append(",");
            sb.append(useTime);

            runLogger.info(sb);
        }
        startTime.remove();
        LogFactory.removeFlag();
    }

    class ThreadPoolStatistics implements Runnable {
        public ThreadPoolStatistics() {
        }

        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            int maxPoolSize = getMaximumPoolSize();
            int corePoolSize = getCorePoolSize();
            int activeCount = getActiveCount();
            int idleCount = getPoolSize() - activeCount;
            long lastPeroidProcTotalTaskNum = executedTaskCount;
            long curProcTotalTaskNum = getCompletedTaskCount();
            long curProcTaskNum = curProcTotalTaskNum - lastPeroidProcTotalTaskNum;
            long toBeExecuteTaskNum = getTaskCount() - curProcTotalTaskNum;
            executedTaskCount = curProcTotalTaskNum;
            StringBuffer sb = new StringBuffer();
            sb.append(threadPoolName);
            sb.append(",");
            sb.append(maxPoolSize);
            sb.append(",");
            sb.append(corePoolSize);
            sb.append(",");
            sb.append(activeCount);
            sb.append(",");
            sb.append(idleCount);
            sb.append(",");
            sb.append(curProcTaskNum);
            sb.append(",");
            sb.append(toBeExecuteTaskNum);
            sb.append(",");
            sb.append(getQueue().size());
            staLogger.info(sb);
        }
    }
}
