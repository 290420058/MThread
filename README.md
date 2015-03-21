# monitor-thread
 普通JDK自带的线程池时无法实现线程池的自动切换，基于监控与上下文自动切换的需求，封住了一套taxi开头的线程池，接入方式很简单，它的使用方式与Jdk的使用基本方式一致，只需在对应的类前加一个Taxi，现将对应方式列举如下：
1）Executors<-------->TaxiExecutors 
     Example：     private final ExecutorService producerPool = TaxiExecutors.newCachedThreadPool(new ThreadFactoryBuilder("Scheduled-EncourageOrderPushProducer-scheduler"));
     
2）ScheduledThreadPoolExecutor<-------->WrapTaxiScheduledThreadPoolExecutor
      Example：   private WrapTaxiScheduledThreadPoolExecutor executor = new WrapTaxiScheduledThreadPoolExecutor(2, new ThreadFactoryBuilder("TimerMonitorImpl-executor"));
      
3）ThreadPoolExecutor<-------->TaxiThreadPoolExecutor
      Example：private final ExecutorService dataFlushExecutor = new TaxiThreadPoolExecutor(100, 200, 60L,TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(5000000), new ThreadFactoryBuilder(
      "DataFlush-executor"));
      
4）Callable<-------->TaxiCallableWrapper
      Example：public <T> Future<T> submit(Callable<T> task) {
                       return schedule(new TaxiCallableWrapper<T>(task), 0, TimeUnit.NANOSECONDS);
                        }
                        
5）Runnable<-------->TaxiRunnableWrapper
    Example：public void execute(Runnable command) {
                     schedule(new TaxiRunnableWrapper(command), 0, TimeUnit.NANOSECONDS);
                     }
                     
应用依赖的Log4j中做如下配置
<appender name="treadPoolRun" class="org.apache.log4j.DailyRollingFileAppender">
<param name="Encoding" value="UTF-8" />
<param name="File" value="${log.dir}/treadPoolRun.log" />
<param name="Append" value="true" />
<param name="DatePattern" value="'.'yyyy-MM-dd" />
<layout class="org.apache.log4j.PatternLayout">
<param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss},%m%n" />
</layout>
</appender>
<appender name="treadPoolRunAsync" class="org.apache.log4j.AsyncAppender">
<param name="bufferSize" value="10000" />
<param name="blocking" value="false" />
<appender-ref ref="treadPoolRun" />
</appender>
<appender name="treadPoolSta" class="org.apache.log4j.DailyRollingFileAppender">
<param name="Encoding" value="UTF-8" />
<param name="File" value="${log.dir}/treadPoolSta.log" />
<param name="Append" value="true" />
<param name="DatePattern" value="'.'yyyy-MM-dd" />
<layout class="org.apache.log4j.PatternLayout">
<param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss},%m%n" />
</layout>
</appender>
<logger name="treadPoolRunLogger" additivity="false">
<level value="INFO" />
<appender-ref ref="treadPoolRunAsync" />
</logger>
<logger name="treadPoolStaLogger" additivity="false">
<level value="INFO" />
<appender-ref ref="treadPoolSta" />
</logger>
