package com.waylau.easyexcel.export;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 任务进度
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-07
 */
@Getter
@ToString
public class ExportProgress {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 当前值。进度百分比的分子
     */
    private int value;

    /**
     * 最大值。进度百分比的分母
     */
    private int max;

    /**
     * 执行时间
     */
    private long startTime;

    /**
     * 任务状态
     */
    private ExportTaskStatusEnum exportTaskStatus;

    /**
     * 进度的描述
     */
    private String content;


    private ExportProgress() {
    }

    public ExportProgress(String taskId, int value, int max, ExportTaskStatusEnum exportTaskStatus, String content) {
        this.taskId = taskId;
        this.value = value;
        this.max = max;
        this.content = content;
        this.exportTaskStatus = exportTaskStatus;
        this.startTime = System.currentTimeMillis();
    }
}
