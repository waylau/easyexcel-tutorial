package com.waylau.easyexcel.export;

import java.util.concurrent.CompletableFuture;

public interface IExportExcutor {
    CompletableFuture<ExportResult> submit(ExportTask exportTask);
}
