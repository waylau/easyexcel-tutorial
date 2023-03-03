package com.waylau.easyexcel.export;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportExcutorProperties {
    private int corePoolSize = 4;
    private int maximumPoolSize = 8;
    private long keepAliveTime = 10;
    private int capacity = 1024;
}
