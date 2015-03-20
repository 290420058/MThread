/*
 * Copyright (C) 2010 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuaidadi.framework.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.base.Preconditions;

/**
 * * @author zhangliang
 * 
 * @version $Id: TaxiThreadPoolExecutor.java, v 0.1 Aug 1, 2014 11:37:08 AM
 *          zhangliang Exp $
 */
public class ThreadFactoryBuilder {

    /** 线程池名称 */
    private String                   name                     = null;
    /** 是否为 daemon进程 */
    private Boolean                  daemon                   = null;
    /** 优先级 */
    private Integer                  priority                 = null;
    /** 异常处理类 */
    private UncaughtExceptionHandler uncaughtExceptionHandler = null;

    /**
     * Creates a new {@link ThreadFactory} builder.
     */
    public ThreadFactoryBuilder(String name) {
        this.name = name;
    }

    /**
     * setDaemon
     * 
     * @param daemon
     * @return
     */
    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    /**
     * setPriority
     * 
     * @param priority
     * @return
     */
    public ThreadFactoryBuilder setPriority(int priority) {
        Preconditions.checkArgument(priority >= Thread.MIN_PRIORITY, "Thread priority (%s) must be >= %s", priority,
            Thread.MIN_PRIORITY);
        Preconditions.checkArgument(priority <= Thread.MAX_PRIORITY, "Thread priority (%s) must be <= %s", priority,
            Thread.MAX_PRIORITY);
        this.priority = priority;
        return this;
    }

    /**
     * setUncaughtExceptionHandler
     * 
     * @param uncaughtExceptionHandler
     * @return
     */
    public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = Preconditions.checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    public ThreadFactory build() {
        return build(this);
    }

    /**
     * build ThreadFactory
     * 
     * @param builder
     * @return
     */
    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        final String namePrefix = builder.name;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        final ThreadFactory backingThreadFactory = Executors.defaultThreadFactory();
        final AtomicLong count = (namePrefix != null) ? new AtomicLong(0) : null;
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = backingThreadFactory.newThread(runnable);
                if (namePrefix != null) {
                    thread.setName(namePrefix + "_" + count.getAndIncrement());
                }
                if (daemon != null) {
                    thread.setDaemon(daemon);
                }
                if (priority != null) {
                    thread.setPriority(priority);
                }
                if (uncaughtExceptionHandler != null) {
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }
                return thread;
            }
        };
    }

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public String getName() {
        return name;
    }
}
