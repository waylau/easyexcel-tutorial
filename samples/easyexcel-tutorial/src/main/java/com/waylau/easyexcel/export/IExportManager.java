package com.waylau.easyexcel.export;

import java.util.List;

/**
 * 导出任务管理器
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-07
 */
public interface IExportManager {
    /**
     * 附加进度记录
     * @param exportProgress
     */
    void appendStatus(ExportProgress exportProgress);

    /**
     * 查询进度记录
     * @param taskId
     * @return
     */
    List queryStatus(String taskId);

    /**
     * 关闭管理器，释放资源
     */
    void close();
}
