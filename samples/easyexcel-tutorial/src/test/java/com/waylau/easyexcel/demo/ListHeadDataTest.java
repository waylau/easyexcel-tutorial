package com.waylau.easyexcel.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.DateUtils;
import com.waylau.easyexcel.util.IdUtil;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * ExportMergeStrategy Test
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-14
 */
public class ListHeadDataTest {

    @Test
    public void testWrite() throws Exception {
        readAndWrite("D://ListHeadData" + IdUtil.createID() + ".xlsx");
    }

    private void readAndWrite(String fileName) throws Exception {
        EasyExcel.write(fileName).head(head()).sheet().doWrite(data());
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串");
        List<String> head1 = new ArrayList<String>();
        head1.add("数字");
        List<String> head2 = new ArrayList<String>();
        head2.add("日期");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<Object>> data() throws ParseException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        List<Object> data0 = new ArrayList<Object>();
        data0.add("字符串0");
        data0.add(1);
        data0.add(DateUtils.parseDate("2020-01-01 01:01:01"));
        data0.add("额外数据");
        list.add(data0);
        return list;
    }
}
