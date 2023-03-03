package com.waylau.easyexcel.demo;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.waylau.easyexcel.demo.data.LargeData;
import com.waylau.easyexcel.demo.data.MyBasicStyleSheet;
import com.waylau.easyexcel.demo.data.StyleData;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyBasicStyleSheetTest {
    private static final int SHEET_COUNT = 10;

    private static final int ROW_COUNT = 1_000;

    private static final String FILE_DIR = "d://";

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBasicStyleSheetTest.class);


    /**
     * 不同对象写到不同的sheet；
     *
     * @throws Exception
     */
    @Test
    public void testForStyleData() throws Exception {
        // 方法2 如果写到不同的sheet 同一个对象
        String fileName = FILE_DIR + "large" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();

        WriteSheet writeSheet = EasyExcel.writerSheet(0, "模板").head(MyBasicStyleSheet.class).build();
        excelWriter.write(styleData(ROW_COUNT), writeSheet);
        LOGGER.info("{} fill success.", 0);

        excelWriter.finish();
    }
    public List<MyBasicStyleSheet> styleData(int count) {
        long startTime = System.currentTimeMillis();

        List<MyBasicStyleSheet> list = new ArrayList<>();

        for (int j = 0; j < count; j++) {
            MyBasicStyleSheet oneRow = new MyBasicStyleSheet();
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
