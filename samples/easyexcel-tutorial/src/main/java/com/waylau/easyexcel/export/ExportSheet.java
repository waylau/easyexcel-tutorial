package com.waylau.easyexcel.export;

import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Getter;

import java.util.List;

/**
 * 导出任务的Sheet参数
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-07
 */
@Getter
public class ExportSheet<T> {
    /**
     * Sheet对应的对象类型
     */
    private Class<T> clazz;

    /**
     * Sheet对应的名称
     */
    String sheetName;

    /**
     * Sheet对应的数据
     */
    List<T> sheetData;

    /**
     * 自定义处理器
     */
    List<WriteHandler> customWriteHandlerList;

    private ExportSheet() {
        // 禁止用默认构造函数
    }

    public ExportSheet(Class<T> clazz, String sheetName, List<T> sheetData) {
        this.clazz = clazz;
        this.sheetName = sheetName;
        this.sheetData = sheetData;
    }

    public ExportSheet(Class<T> clazz, String sheetName, List<T> sheetData, List<WriteHandler> customWriteHandlerList) {
        this.clazz = clazz;
        this.sheetName = sheetName;
        this.sheetData = sheetData;
        this.customWriteHandlerList = customWriteHandlerList;
    }

}
