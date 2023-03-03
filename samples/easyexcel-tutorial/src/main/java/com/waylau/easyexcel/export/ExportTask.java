package com.waylau.easyexcel.export;

import com.waylau.easyexcel.util.IdUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExportTask {
    private String taskId = IdUtil.createID();

    // 报表的结构：对象->
    private List<SheetParam> sheetParamList = new ArrayList<>();

    public ExportTask() {

    }

    public ExportTask addSheet(SheetParam sheetParam) {
        sheetParamList.add(sheetParam);
        return this;
    }
}
