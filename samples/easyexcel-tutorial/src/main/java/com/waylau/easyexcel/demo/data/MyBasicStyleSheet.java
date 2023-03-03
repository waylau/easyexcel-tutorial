package com.waylau.easyexcel.demo.data;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.waylau.easyexcel.export.BasicStyleSheet;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MyBasicStyleSheet extends BasicStyleSheet {

    @ExcelProperty("字符串")
    private String string;

    @ExcelProperty("字符串1")
    private String string1;

    @ExcelProperty("日期")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private Date date;

    @ExcelProperty(value = "数字")
    @NumberFormat("#.##%")
    private Double number;

    @ExcelProperty("忽略我")
    @ExcelIgnore
    private String string2;

}
