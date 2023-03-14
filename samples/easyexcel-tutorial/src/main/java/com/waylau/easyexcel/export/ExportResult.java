package com.waylau.easyexcel.export;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.ByteArrayOutputStream;

/**
 * 导出任务执行结果
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-07
 */
@Getter
@Setter
@ToString
public class ExportResult {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 结果状态
     */
    private ExportTaskStatusEnum exportTaskStatus;

    /**
     * 结果的描述
     */
    private String content;

    /**
     * 导出的文件流
     */
    private ByteArrayOutputStream byteArrayOutputStream;
}
