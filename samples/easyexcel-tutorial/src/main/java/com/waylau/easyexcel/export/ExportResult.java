package com.waylau.easyexcel.export;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.ByteArrayOutputStream;

@Getter
@Setter
@ToString
public class ExportResult {
    private boolean isSuccess;

    private String resultMsg;

    private String taskId;

    private ByteArrayOutputStream byteArrayOutputStream;
}
