/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2014 All Rights Reserved.
 */
package com.kuaidadi.framework.thread;

import com.kuaidadi.framework.log.LogFactory;

/**
 * 
 * @author zhangliang
 * @version $Id: TaxiRunnableWrapper.java, v 0.1 Sep 2, 2014 9:34:10 AM
 *          zhangliang Exp $
 */
public class TaxiRunnableWrapper implements Runnable {
    /** 线程上线文 */
    private String   flag;

    private Runnable runnable;

    public TaxiRunnableWrapper(Runnable runnable) {
        this.runnable = runnable;
        this.flag = LogFactory.getFlag();

    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            LogFactory.setFlag(flag);
            runnable.run();
        } finally {
            LogFactory.removeFlag();
        }
    }
}
