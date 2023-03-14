package com.waylau.easyexcel.export;

/**
 * 导出任务状态
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-07
 */
public enum ExportTaskStatusEnum {
    WAITING("等待执行"),
    RUNNING("已经执行"),
    SUCCESS("执行成功"),
    FAILED("执行失败");

    private String content;

    private ExportTaskStatusEnum(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return this.content;
    }
}
