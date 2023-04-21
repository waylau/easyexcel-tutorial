package com.waylau.easyexcel.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.waylau.easyexcel.export.ExportColumn;
import com.waylau.easyexcel.util.IdUtil;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ExportMergeStrategy Test
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-14
 */
public class ListHeadDataTest2 {

    @Test
    public void testWrite() throws Exception {
        readAndWrite("D://ListHeadData" + IdUtil.createID() + ".xlsx");
    }

    private void readAndWrite(String fileName) throws Exception {

        List<ExportColumn> columns = new ArrayList<>();
        ExportColumn column = new ExportColumn();
        column.setOrder(0);
        column.setField("field0");
        column.setTitle("字段0");

        ExportColumn column1 = new ExportColumn();
        column1.setOrder(1);
        column1.setField("field1");
        column1.setTitle("字段1");


        ExportColumn column2 = new ExportColumn();
        column2.setOrder(2);
        column2.setField("field2");
        column2.setTitle("字段2");


        ExportColumn column3 = new ExportColumn();
        column3.setOrder(3);
        column3.setField("field3");
        column3.setTitle("字段3");

        columns.add(column3);
        columns.add(column2);
        columns.add(column);
        columns.add(column1);


        // 按照order排序
        List<ExportColumn> sortedColumns = columns.stream().sorted(Comparator.comparing(ExportColumn::getOrder)).collect(Collectors.toList());


        EasyExcel.write(fileName).head(getHead(sortedColumns)).sheet().doWrite(getData(sortedColumns));
    }

    private List<List<String>> getHead(List<ExportColumn> columns) {
        if (columns == null || columns.isEmpty()) {
            return null;
        }

        List<List<String>> result = new ArrayList<List<String>>(columns.size());

        for (ExportColumn column : columns) {
            List<String> head = new ArrayList<String>();
            head.add(column.getTitle());
            result.add(head);
        }

        return result;
    }

    private List<List<Object>> getData(List<ExportColumn> columns) throws ParseException {
        List<List<Object>> result = new ArrayList<List<Object>>();


        String str = "[{\"field0\":\"dsad\",\"field1\":\"123\",\"field3\":\"44\" ,\"field2\":\"333\"},{\"field0\":\"2dsad\",\"field1\":\"2123\",\"field3\":\"244\" ,\"field2\":\"2333\"}]";
        JSONArray data = JSON.parseArray(str);


        for( Object object :  data) {
            JSONObject jsonObject = (JSONObject)object;
            List<Object> listObject = new ArrayList<>(jsonObject.size());

            // 按照order排序
            List<ExportColumn> sortedColumns = columns.stream().sorted(Comparator.comparing(ExportColumn::getOrder)).collect(Collectors.toList());

            for (ExportColumn column: sortedColumns) {
                listObject.add(jsonObject.get(column.getField()));
            }

            result.add(listObject);
        }

        return result;
    }
}
