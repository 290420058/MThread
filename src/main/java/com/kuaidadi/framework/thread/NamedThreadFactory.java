package com.kuaidadi.framework.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 支持命名的 {@link ThreadFactory}
 * 
 * @author kyo.ou 2013-6-26
 * 
 */
public class NamedThreadFactory implements ThreadFactory {
    private final String  name;
    private AtomicInteger counter = new AtomicInteger(0);

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        String threadName = name + "_" + counter.incrementAndGet();
        t.setName(threadName);
        return t;
    }
}
