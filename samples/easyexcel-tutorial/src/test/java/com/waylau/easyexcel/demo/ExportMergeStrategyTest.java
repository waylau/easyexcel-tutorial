package com.waylau.easyexcel.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.waylau.easyexcel.demo.data.MergeData;
import com.waylau.easyexcel.demo.util.TestDataGenerator;
import com.waylau.easyexcel.export.ExportMergeStrategy;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ExportMergeStrategy Test
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-14
 */
public class ExportMergeStrategyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcutorTest.class);
    @Test
    public void test() throws Exception {

        // 方法2 如果写到不同的sheet 同一个对象
        String fileName = "d://ExportMergeStrategy" + System.currentTimeMillis() + ".xlsx";

        List<?> data = TestDataGenerator.getMergeData();

        ExcelWriter excelWriter = EasyExcel.write(fileName, MergeData.class)
                .registerWriteHandler(new ExportMergeStrategy(data.size(),0,1,2,3))
                .build();

        for (int j = 0; j < 2; j++) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            writeSheet.setSheetNo(j);
            writeSheet.setSheetName("test" + j);
            excelWriter.write(data, writeSheet);
            LOGGER.info("{} fill success.", j);
        }
        excelWriter.finish();
    }
}
