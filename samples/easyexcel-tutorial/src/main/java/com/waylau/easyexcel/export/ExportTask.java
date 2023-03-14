package com.waylau.easyexcel.export;

import com.waylau.easyexcel.util.IdUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出任务
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-07
 */
@Getter
public class ExportTask {
    /**
     * 任务ID。
     * 会自动生成确保唯一
     */
    private String taskId = "ExportTask"+ IdUtil.createID();

    /**
     * 导出任务的数据
     */
    private List<ExportSheet> exportSheetList = new ArrayList<>();

    public ExportTask() {

    }

    /**
     * 添加Sheet。
     * 一个导出任务会由多个Sheet组成。每个Sheet可以是不同的对象。
     * @param exportSheet 定义Sheet的参数
     * @return 导出任务
     */
    public ExportTask addSheet(ExportSheet exportSheet) {
        exportSheetList.add(exportSheet);
        return this;
    }
}
