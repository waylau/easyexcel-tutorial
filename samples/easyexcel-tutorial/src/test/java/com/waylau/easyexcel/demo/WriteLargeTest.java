package com.waylau.easyexcel.demo;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.waylau.easyexcel.demo.data.LargeData;
import com.waylau.easyexcel.demo.data.LargeData2;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 临时测试
 *
 * @author Jiaju Zhuang
 **/

public class WriteLargeTest {
    private static final int SHEET_COUNT = 10;

    private static final int ROW_COUNT = 1_000;

    private static final int BUFFER = 1_000;

    private static final String FILE_DIR = "d://";

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteLargeTest.class);

    /**
     * 同一对象写到相同的sheet；
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        long startTime = System.currentTimeMillis();

        String fileName = FILE_DIR + "large" + startTime + ".xlsx";

        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 20);
        headWriteCellStyle.setWriteFont(headWriteFont);

        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        ExcelWriter excelWriter = EasyExcel.write(fileName, LargeData.class).registerWriteHandler(
                horizontalCellStyleStrategy).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();

        // 测试100w数据
        excelWriter.write(data(1_000_000), writeSheet);

        long endTime = System.currentTimeMillis();
        LOGGER.info("const time: {}", endTime - startTime);

        excelWriter.finish();

    }

    /**
     * 内存模式，感觉差异不大
     * @throws Exception
     */
    @Test
    public void testMemory() throws Exception {
        long startTime = System.currentTimeMillis();

        String fileName = FILE_DIR + "large" + startTime + ".xlsx";

        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 20);
        headWriteCellStyle.setWriteFont(headWriteFont);

        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        ExcelWriter excelWriter = EasyExcel.write(fileName, LargeData.class).registerWriteHandler(
                horizontalCellStyleStrategy)
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();

        // 测试100w数据
        excelWriter.write(data(1_000_000), writeSheet);

        long endTime = System.currentTimeMillis();
        LOGGER.info("const time: {}", endTime - startTime);

        excelWriter.finish();

    }


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
            excelWriter.write(data(ROW_COUNT), writeSheet);
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
                writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(LargeData2.class).build();
                excelWriter.write(data(ROW_COUNT), writeSheet);
            } else {
                writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(LargeData.class).build();
                excelWriter.write(data(ROW_COUNT), writeSheet);
            }

            LOGGER.info("{} fill success.", i);
        }
        excelWriter.finish();
    }

    private List<List<String>> data(int count) {
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
}
