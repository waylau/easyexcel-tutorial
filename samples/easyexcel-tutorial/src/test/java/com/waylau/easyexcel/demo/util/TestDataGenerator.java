package com.waylau.easyexcel.demo.util;

import com.waylau.easyexcel.demo.bean.LargeData;
import com.waylau.easyexcel.demo.data.MergeData;
import com.waylau.easyexcel.demo.data.StyleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试数据生成器
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-08
 */
public class TestDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataGenerator.class);

    public static List<StyleData> getStyleData(int count) {
        long startTime = System.currentTimeMillis();

        List<StyleData> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            StyleData oneRow = new StyleData();
            oneRow.setDate(new Date());
            oneRow.setNumber(1.1D + 1);
            oneRow.setString1("hello" + i);
            oneRow.setString("world" + i);
            oneRow.setString2("ignore" + i);
            list.add(oneRow);
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("generate data count: {}, cost time: {}", count, endTime - startTime);

        return list;
    }

    public static List<LargeData> getLargeData(int count) {
        long startTime = System.currentTimeMillis();

        List<LargeData> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            LargeData oneRow = new LargeData();

            // 遍历实体属性来设置属性值
            for (Field field : LargeData.class.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    field.set(oneRow, field.getName() + " r" + i);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            list.add(oneRow);
        }

        long endTime = System.currentTimeMillis();
        LOGGER.info("generate data count: {}, cost time: {}", count, endTime - startTime);

        return list;
    }

    public static List<MergeData> getMergeData() {
        long startTime = System.currentTimeMillis();

        List<MergeData> list = new ArrayList<>();

        MergeData oneRow = new MergeData();
        oneRow.setUsername("陈先生");
        oneRow.setPhoneNumber("18900000000");
        oneRow.setCity("江门");
        oneRow.setProjectName("项目1");
        list.add(oneRow);

        MergeData oneRow2 = new MergeData();
        oneRow2.setUsername("陈先生");
        oneRow2.setPhoneNumber("18900000000");
        oneRow2.setCity("广州");
        oneRow2.setProjectName("项目2");
        list.add(oneRow2);

        MergeData oneRow3 = new MergeData();
        oneRow3.setUsername("梨先生");
        oneRow3.setPhoneNumber("13300000000");
        oneRow3.setCity("惠州");
        oneRow3.setProjectName("项目3");
        list.add(oneRow3);

        MergeData oneRow5 = new MergeData();
        oneRow5.setUsername("梨先生");
        oneRow5.setPhoneNumber("13300000000");
        oneRow5.setCity("惠州");
        oneRow5.setProjectName("项目5");
        list.add(oneRow5);


        MergeData oneRow4 = new MergeData();
        oneRow4.setUsername("梨先生");
        oneRow4.setPhoneNumber("13300000000");
        oneRow4.setCity("广州");
        oneRow4.setProjectName("项目4");
        list.add(oneRow4);


        long endTime = System.currentTimeMillis();
        LOGGER.info("generate data count: {}, cost time: {}", list.size(), endTime - startTime);

        return list;
    }

}
