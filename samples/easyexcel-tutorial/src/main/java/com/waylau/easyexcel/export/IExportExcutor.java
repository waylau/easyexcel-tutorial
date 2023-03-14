package com.waylau.easyexcel.export;

import java.util.concurrent.CompletableFuture;

/**
 * 导出任务执行器
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-06
 */
public interface IExportExcutor {
    /**
     * 提交导出任务
     * @param exportTask 导出任务
     * @return 导出结果
     */
    CompletableFuture<ExportResult> submit(ExportTask exportTask);

    /**
     * 导出任务管理器
     * @return
     */
    IExportManager getExportManager();

    /**
     * 关闭执行器，释放资源
     */
    void close();
}
