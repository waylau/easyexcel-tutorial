package com.waylau.easyexcel.demo;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.waylau.easyexcel.demo.data.LargeData;
import com.waylau.easyexcel.demo.data.LargeData2;
import com.waylau.easyexcel.demo.data.StyleData;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriteBeanTest {
    private static final int SHEET_COUNT = 10;

    private static final int ROW_COUNT = 1_000;

    private static final String FILE_DIR = "d://";

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteBeanTest.class);


    /**
     * 不同对象写到不同的sheet；
     *
     * @throws Exception
     */
    @Test
    public void testForMutiClassMutiSheet() throws Exception {
        // 方法2 如果写到不同的sheet 同一个对象
        String fileName = FILE_DIR + "large" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();

        WriteSheet writeSheet;
        for (int i = 0; i < SHEET_COUNT; i++) {
            // 使用了不同对象LargeData、LargeData2
            if (i % 2 == 0) {
                writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(StyleData.class).build();
                excelWriter.write(styleData(ROW_COUNT), writeSheet);
            } else {
                writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(LargeData.class).build();
                excelWriter.write(data(ROW_COUNT), writeSheet);
            }

            LOGGER.info("{} fill success.", i);
        }
        excelWriter.finish();
    }

    public List<List<String>> data(int count) {
        long startTime = System.currentTimeMillis();

        List<List<String>> list = new ArrayList<>();

        for (int j = 0; j < count; j++) {
            List<String> oneRow = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                oneRow.add("这是测试字段" + i);
            }
            list.add(oneRow);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("data count: {}, cost time: {}", count, endTime - startTime);

        return list;
    }

    public List<StyleData> styleData(int count) {
        long startTime = System.currentTimeMillis();

        List<StyleData> list = new ArrayList<>();

        for (int j = 0; j < count; j++) {
            StyleData oneRow = new StyleData();
            oneRow.setDate(new Date());
            oneRow.setNumber(1.1D);
            oneRow.setString1("hello");
            oneRow.setString("world");
            list.add(oneRow);
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("data count: {}, cost time: {}", count, endTime - startTime);

        return list;
    }
}
