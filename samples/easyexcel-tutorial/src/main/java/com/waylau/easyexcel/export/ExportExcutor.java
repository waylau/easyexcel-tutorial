package com.waylau.easyexcel.export;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

public class ExportExcutor implements IExportExcutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcutor.class);

    private ExportExcutorProperties exportExcutorProperties;

    private Executor executorService;
    private ExportExcutor() {
        // 禁止示例化
    }

    public ExportExcutor(ExportExcutorProperties exportExcutorProperties) {
        this.exportExcutorProperties = exportExcutorProperties;

        this.executorService = getExecutor(exportExcutorProperties);
    }

    @Override
    public CompletableFuture<ExportResult> submit(ExportTask exportTask) {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("start taskId:{}", exportTask.getTaskId());
            long startTime = System.currentTimeMillis();

            ExportResult result = new ExportResult();
            result.setResultMsg("hi");
            result.setStatus(1);
            result.setTaskId(exportTask.getTaskId());


            List<SheetParam> sheetParamList = exportTask.getSheetParamList();

            //创建任意一个OutPutStream流
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //String fileName = "d://large" + System.currentTimeMillis() + ".xlsx";
            ExcelWriter excelWriter = EasyExcel.write(bos).inMemory(false).build();

            WriteSheet writeSheet;
            int i = 0;
            for (SheetParam<?> param : sheetParamList) {
                String sheetName = param.getSheetName();
                Class genericType = param.getClazz();

                // 使用了不同对象LargeData、LargeData2
                writeSheet = EasyExcel.writerSheet(i, sheetName)
                        .head(genericType)
                        .build();
                excelWriter.write(param.getSheetData(), writeSheet);

                i ++;
                LOGGER.info("{} fill success.", i);
            }
            excelWriter.finish();
            result.setByteArrayOutputStream(bos);

            try {
                bos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long endTime = System.currentTimeMillis();
            LOGGER.info("end  taskId:{}, cost:{}", exportTask.getTaskId(), endTime - startTime);

            return result;

        }, executorService);

    }

    private Executor getExecutor(ExportExcutorProperties exportExcutorProperties) {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(
                    exportExcutorProperties.getCorePoolSize(),
                    exportExcutorProperties.getMaximumPoolSize(),
                    exportExcutorProperties.getKeepAliveTime(),
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(exportExcutorProperties.getCapacity()),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy());
        }
        return executorService;
    }
}
