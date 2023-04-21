package com.waylau.easyexcel.demo.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


/**
 * 合并的数据对象
 */

@Data
@Getter
@Setter
public class MergeData {
    @ExcelProperty({"response","姓名","姓名","姓名"})
    private String username;

    @ExcelProperty({"response","手机号","手机号","手机号"})
    private String phoneNumber;

    @ExcelProperty({"response","手机号","项目","城市"})
    private String city;

    @ExcelProperty({"response","手机号","项目","项目名"})
    private String projectName;

    @ExcelProperty({"response","手机号","项目","项目资金"})
    @NumberFormat("#.##%")
    private Double projectMoney;

}
