/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidadi.framework.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * TaxiExecutors
 * 
 * @author zhangliang
 * @version $Id: TaxiExecutors.java, v 0.1 Aug 1, 2014 1:11:28 PM zhangliang Exp
 *          $
 */
public class TaxiExecutors {

    /**
     * Creates a thread pool that reuses a fixed number of threads operating off
     * a shared unbounded queue, using the provided ThreadFactory to create new
     * threads when needed. At any point, at most <tt>nThreads</tt> threads will
     * be active processing tasks. If additional tasks are submitted when all
     * threads are active, they will wait in the queue until a thread is
     * available. If any thread terminates due to a failure during execution
     * prior to shutdown, a new one will take its place if needed to execute
     * subsequent tasks. The threads in the pool will exist until it is
     * explicitly {@link ExecutorService#shutdown shutdown}.
     * 
     * @param nThreads
     *            the number of threads in the pool
     * @param threadFactory
     *            the factory to use when creating new threads
     * @return the newly created thread pool
     * @throws NullPointerException
     *             if threadFactory is null
     * @throws IllegalArgumentException
     *             if {@code nThreads <= 0}
     */
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactoryBuilder threadFactoryBuilder) {
        return new TaxiThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), threadFactoryBuilder);
    }

    /**
     * Creates a thread pool that creates new threads as needed, but will reuse
     * previously constructed threads when they are available, and uses the
     * provided ThreadFactory to create new threads when needed.
     * 
     * @param threadFactory
     *            the factory to use when creating new threads
     * @return the newly created thread pool
     * @throws NullPointerException
     *             if threadFactory is null
     */
    public static ExecutorService newCachedThreadPool(ThreadFactoryBuilder threadFactoryBuilder) {
        return new TaxiThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), threadFactoryBuilder);
    }

    /**
     * Creates a single-threaded executor that can schedule commands to run
     * after a given delay, or to execute periodically. (Note however that if
     * this single thread terminates due to a failure during execution prior to
     * shutdown, a new one will take its place if needed to execute subsequent
     * tasks.) Tasks are guaranteed to execute sequentially, and no more than
     * one task will be active at any given time. Unlike the otherwise
     * equivalent <tt>newScheduledThreadPool(1, threadFactory)</tt> the returned
     * executor is guaranteed not to be reconfigurable to use additional
     * threads.
     * 
     * @param threadFactory
     *            the factory to use when creating new threads
     * @return a newly created scheduled executor
     * @throws NullPointerException
     *             if threadFactory is null
     */
    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactoryBuilder threadFactoryBuilder) {
        return new TaxiScheduledThreadPoolExecutor(1, threadFactoryBuilder);
    }

    /**
     * Creates a thread pool that can schedule commands to run after a given
     * delay, or to execute periodically.
     * 
     * @param corePoolSize
     *            the number of threads to keep in the pool, even if they are
     *            idle.
     * @param threadFactory
     *            the factory to use when the executor creates a new thread.
     * @return a newly created scheduled thread pool
     * @throws IllegalArgumentException
     *             if {@code corePoolSize < 0}
     * @throws NullPointerException
     *             if threadFactory is null
     */
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize,
                                                                  ThreadFactoryBuilder threadFactoryBuilder) {
        return new TaxiScheduledThreadPoolExecutor(corePoolSize, threadFactoryBuilder);
    }

}
