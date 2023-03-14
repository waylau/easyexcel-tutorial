package com.waylau.easyexcel.export;


import lombok.Getter;
import lombok.Setter;

/**
 * 导出任务执行器配置用属性
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-06
 */
@Getter
@Setter
public class ExportExecutorProperties {
    /**
     * 线程池核心线程大小。
     * 线程池中会维护一个最小的线程数量，即使这些线程处理空闲状态，他们也不会被销毁
     */
    private int corePoolSize = 2;

    /**
     * 线程池最大线程数量。
     */
    private int maximumPoolSize = 4;

    /**
     * 空闲线程存活时间（单位秒）。
     * 一个线程如果处于空闲状态，并且当前的线程数量大于corePoolSize，那么在指定时间后，这个空闲线程会被销毁
     */
    private long keepAliveTime = 10;
    /**
     * 任务队列的大小。
     * 超过队列限制，则直接丢弃任务，并抛出RejectedExecutionException异常。
     */
    private int capacity = 100;

    /**
     * 缓存的进度记录数
     */
    private int progerssEntries = 1024;

    /**
     * 缓存的进度记录过期时间（单位秒）
     */
    private int progerssExpiration = 86400;
}
