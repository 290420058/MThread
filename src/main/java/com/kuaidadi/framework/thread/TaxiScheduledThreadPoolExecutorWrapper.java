/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidadi.framework.thread;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author zhangliang
 * @version $Id: WrapScheduledThreadPoolExecutor.java, v 0.1 Sep 1, 2014 6:52:28
 *          PM zhangliang Exp $
 */
public class TaxiScheduledThreadPoolExecutorWrapper implements ScheduledExecutorService {

    private ScheduledThreadPoolExecutor executor;

    public TaxiScheduledThreadPoolExecutorWrapper(int corePoolSize) {
        executor = new TaxiScheduledThreadPoolExecutor(corePoolSize, new ThreadFactoryBuilder("taxi-schedule-pool"));
    }

    public TaxiScheduledThreadPoolExecutorWrapper(int corePoolSize, RejectedExecutionHandler handler) {
        executor = new TaxiScheduledThreadPoolExecutor(corePoolSize, new ThreadFactoryBuilder("taxi-schedule-pool"),
            handler);
    }

    public TaxiScheduledThreadPoolExecutorWrapper(int corePoolSize, ThreadFactoryBuilder threadFactoryBuilder) {
        executor = new TaxiScheduledThreadPoolExecutor(corePoolSize, threadFactoryBuilder);
    }

    public TaxiScheduledThreadPoolExecutorWrapper(int corePoolSize, ThreadFactoryBuilder threadFactoryBuilder,
                                                  RejectedExecutionHandler handler) {
        executor = new TaxiScheduledThreadPoolExecutor(corePoolSize, threadFactoryBuilder, handler);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return executor.schedule(new TaxiRunnableWrapper(command), delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return executor.schedule(new TaxiCallableWrapper<V>(callable), delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return executor.scheduleAtFixedRate(new TaxiRunnableWrapper(command), initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return executor.scheduleAtFixedRate(new TaxiRunnableWrapper(command), initialDelay, delay, unit);
    }

    public void execute(Runnable command) {
        schedule(new TaxiRunnableWrapper(command), 0, TimeUnit.NANOSECONDS);
    }

    public Future<?> submit(Runnable task) {
        return schedule(new TaxiRunnableWrapper(task), 0, TimeUnit.NANOSECONDS);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return schedule(Executors.callable(new TaxiRunnableWrapper(task), result), 0, TimeUnit.NANOSECONDS);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return schedule(new TaxiCallableWrapper<T>(task), 0, TimeUnit.NANOSECONDS);
    }

    /**
     * @see java.util.concurrent.ExecutorService#shutdown()
     */
    @Override
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * @see java.util.concurrent.ExecutorService#shutdownNow()
     */
    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    /**
     * @see java.util.concurrent.ExecutorService#isShutdown()
     */
    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    /**
     * @see java.util.concurrent.ExecutorService#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    /**
     * @see java.util.concurrent.ExecutorService#awaitTermination(long,
     *      java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    /**
     * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executor.invokeAll(tasks);
    }

    /**
     * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection,
     *      long, java.util.concurrent.TimeUnit)
     */
    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
                                                                                                              throws InterruptedException {
        return executor.invokeAll(tasks, timeout, unit);
    }

    /**
     * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executor.invokeAny(tasks);
    }

    /**
     * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection,
     *      long, java.util.concurrent.TimeUnit)
     */
    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
                                                                                                throws InterruptedException,
                                                                                                ExecutionException,
                                                                                                TimeoutException {
        return executor.invokeAny(tasks, timeout, unit);
    }
}
