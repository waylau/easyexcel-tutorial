package com.waylau.easyexcel.export;

import lombok.Getter;
import lombok.Setter;

/**
 * 导出任务的Sheet中的Column参数
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-15
 */
@Getter
@Setter
public class ExportColumn {
    /**
     * 数据类型
     */
    //private String dataType;

    /**
     * 字段的名称
     */
    private String field;

    /**
     * 字段显示标题
     */
    private String title;

    /**
     * 字段显示顺序
     */
    private int order;
}

