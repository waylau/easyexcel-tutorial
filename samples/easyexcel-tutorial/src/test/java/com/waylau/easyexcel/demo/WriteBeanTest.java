package com.waylau.easyexcel.demo;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.waylau.easyexcel.demo.data.LargeData;
import com.waylau.easyexcel.demo.data.StyleData;
import com.waylau.easyexcel.demo.util.TestDataGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteBeanTest {
    private static final int SHEET_COUNT = 10;

    private static final int ROW_COUNT = 1_000;

    private static final String FILE_DIR = "d://";

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteBeanTest.class);

    /**
     * 同一对象写到不同的sheet；
     *
     * @throws Exception
     */
    @Test
    public void testForOnClassMutiSheet() throws Exception {
        // 方法2 如果写到不同的sheet 同一个对象
        String fileName = FILE_DIR + "large" + System.currentTimeMillis() + ".xlsx";

        ExcelWriter excelWriter = EasyExcel.write(fileName, LargeData.class).build();

        for (int j = 0; j < SHEET_COUNT; j++) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            writeSheet.setSheetNo(j);
            writeSheet.setSheetName("test" + j);
            excelWriter.write(TestDataGenerator.getLargeData(ROW_COUNT), writeSheet);
            LOGGER.info("{} fill success.", j);
        }
        excelWriter.finish();
    }


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
                excelWriter.write(TestDataGenerator.getStyleData(ROW_COUNT), writeSheet);
            } else {
                writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(LargeData.class).build();
                excelWriter.write(TestDataGenerator.getLargeData(ROW_COUNT), writeSheet);
            }

            LOGGER.info("{} fill success.", i);
        }
        excelWriter.finish();
    }

}
