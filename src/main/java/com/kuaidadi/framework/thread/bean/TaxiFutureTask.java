/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidadi.framework.thread.bean;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.kuaidadi.framework.log.LogFactory;

/**
 * 
 * @author zhangliang
 * @version $Id: TaxiFutureTask.java, v 0.1 Aug 5, 2014 2:22:16 PM zhangliang
 *          Exp $
 */
public class TaxiFutureTask<V> extends FutureTask<V> {

    /** taxi 日志上下文 */
    private String flag;

    /**
     * @param runnable
     * @param result
     */
    public TaxiFutureTask(Runnable runnable, V result) {
        super(runnable, result);
        flag = LogFactory.getFlag();
        if (flag == null) {
            flag = LogFactory.getUniqueFlag();
        }
    }

    /**
     * @param callable
     */
    public TaxiFutureTask(Callable<V> callable) {
        super(callable);
        flag = LogFactory.getFlag();
        if (flag == null) {
            flag = LogFactory.getUniqueFlag();
        }
    }

    public void run() {
        LogFactory.setFlag(flag);
        super.run();
    }

    /**
     * Getter method for property <tt>flag</tt>.
     * 
     * @return property value of flag
     */
    public String getFlag() {
        return flag;
    }
}
