/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidadi.framework.thread;

import java.util.concurrent.Callable;

import com.kuaidadi.framework.log.LogFactory;

/**
 * 
 * @author zhangliang
 * @version $Id: TaxiCallable.java, v 0.1 Sep 2, 2014 9:31:34 AM zhangliang Exp
 *          $
 */
public class TaxiCallableWrapper<T> implements Callable<T> {
    /** 线程上线文 */
    private String      flag;

    private Callable<T> callable;

    public TaxiCallableWrapper(final Callable<T> callable) {
        this.flag = LogFactory.getFlag();
        this.callable = callable;
    }

    /**
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public T call() throws Exception {
        try {
            LogFactory.setFlag(flag);
            return callable.call();
        } finally {
            LogFactory.removeFlag();
        }
    }
}
