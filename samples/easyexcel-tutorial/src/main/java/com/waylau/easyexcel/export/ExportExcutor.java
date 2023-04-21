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

/**
 * 导出任务执行器
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-06
 */
public class ExportExcutor implements IExportExcutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcutor.class);

    private ExportExecutorProperties exportExecutorProperties;

    private ExecutorService executorService;

    private IExportManager exportManager;

    public ExportExcutor() {
        if (this.executorService == null) {
            ExportExecutorProperties exportExecutorProperties = new ExportExecutorProperties();

            initExecutorAndManager(exportExecutorProperties);
        }
    }

    public ExportExcutor(ExportExecutorProperties exportExecutorProperties) {
        if (this.executorService == null) {
            this.exportExecutorProperties = exportExecutorProperties;

            initExecutorAndManager(exportExecutorProperties);
        }
    }

    @Override
    public CompletableFuture<ExportResult> submit(ExportTask exportTask) {
        String taskId = exportTask.getTaskId();
        LOGGER.info("submit taskId:{}", taskId);

        ExportProgress exportProgress = new ExportProgress(taskId, 0, 0, ExportTaskStatusEnum.WAITING, ExportTaskStatusEnum.WAITING.toString());
        this.exportManager.appendStatus(exportProgress);

        ExportResult result = new ExportResult();
        result.setTaskId(taskId);

        return CompletableFuture.supplyAsync(() -> {
                    LOGGER.info("start taskId:{}", taskId);
                    long startTime = System.currentTimeMillis();

                    ExportProgress exportProgress1 = new ExportProgress(taskId, 0, 0, ExportTaskStatusEnum.RUNNING, ExportTaskStatusEnum.RUNNING.toString());
                    this.exportManager.appendStatus(exportProgress1);

                    result.setContent(ExportTaskStatusEnum.RUNNING.toString());
                    result.setExportTaskStatus(ExportTaskStatusEnum.RUNNING);

                    List<ExportSheet> exportSheetList = exportTask.getExportSheetList();

                    //创建任意一个OutPutStream流
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    ExcelWriter excelWriter = EasyExcel.write(bos).inMemory(true).build();

                    WriteSheet writeSheet;
                    int i = 0;
                    int size = exportSheetList.size();

                    for (ExportSheet<?> param : exportSheetList) {

                        String sheetName = param.getSheetName();
                        ExportMergeStrategy exportMergeStrategy = param.getExportMergeStrategy();
                        Class clazz = param.getClazz();

                        // 记录进度
                        ExportProgress exportProgress2 = new ExportProgress(taskId, i, size, ExportTaskStatusEnum.RUNNING, "正在导出sheet:" + sheetName);
                        this.exportManager.appendStatus(exportProgress2);

                        // 使用了不同对象
                        // 使用合并策略
                        if (exportMergeStrategy != null) {
                            writeSheet = EasyExcel.writerSheet(i, sheetName)
                                    .head(clazz).registerWriteHandler(exportMergeStrategy).build();
                        } else {
                            writeSheet = EasyExcel.writerSheet(i, sheetName)
                                    .head(clazz).build();
                        }

                        // 添加自定义的处理器
                        /*if (customWriteHandlerList!=null && !customWriteHandlerList.isEmpty()) {
                            writeSheet.setCustomWriteHandlerList(customWriteHandlerList);
                        }*/

                        // 开始写
                        excelWriter.write(param.getSheetData(), writeSheet);

                        i++;

                        // 记录进度
                        ExportProgress exportProgress3 = new ExportProgress(taskId, i, size, ExportTaskStatusEnum.RUNNING, "完成导出sheet:" + sheetName);
                        this.exportManager.appendStatus(exportProgress3);

                        LOGGER.info("sheetName:{}, index:{} success.", sheetName, i);
                    }
                    excelWriter.finish();
                    result.setByteArrayOutputStream(bos);

                    try {
                        bos.close();

                        ExportProgress exportProgress4 = new ExportProgress(taskId, i, size, ExportTaskStatusEnum.RUNNING, ExportTaskStatusEnum.SUCCESS.toString());
                        this.exportManager.appendStatus(exportProgress4);

                        result.setContent(ExportTaskStatusEnum.SUCCESS.toString());
                        result.setExportTaskStatus(ExportTaskStatusEnum.SUCCESS);

                    } catch (IOException e) {
                        ExportProgress exportProgress4 = new ExportProgress(taskId, i, size, ExportTaskStatusEnum.FAILED, ExportTaskStatusEnum.FAILED.toString() + e.getMessage());
                        this.exportManager.appendStatus(exportProgress4);

                        result.setContent(ExportTaskStatusEnum.FAILED.toString() + e.getMessage());
                        result.setExportTaskStatus(ExportTaskStatusEnum.FAILED);

                        LOGGER.error(taskId + " error: {}", e);
                    }

                    long endTime = System.currentTimeMillis();
                    LOGGER.info("end  taskId:{}, cost:{}", taskId, endTime - startTime);

                    return result;

                }, executorService)
                // 异常处理
                .exceptionally(e -> {
                    ExportProgress exportProgress4 = new ExportProgress(taskId, 0, 0, ExportTaskStatusEnum.FAILED, ExportTaskStatusEnum.FAILED.toString() + e.getMessage());
                    this.exportManager.appendStatus(exportProgress4);

                    result.setContent(ExportTaskStatusEnum.FAILED.toString() + e.getMessage());
                    result.setExportTaskStatus(ExportTaskStatusEnum.FAILED);

                    LOGGER.error("error: {}", taskId, e);

                    return result;
                });
    }

    @Override
    public void close() {
        exportManager.close();
        executorService.shutdown();
    }

    private void initExecutorAndManager(ExportExecutorProperties exportExecutorProperties) {
        this.executorService = getExecutor(exportExecutorProperties);
        this.exportManager = getExportManager(exportExecutorProperties);
    }

    private ExecutorService getExecutor(ExportExecutorProperties exportExecutorProperties) {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(
                    exportExecutorProperties.getCorePoolSize(),
                    exportExecutorProperties.getMaximumPoolSize(),
                    exportExecutorProperties.getKeepAliveTime(),
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(exportExecutorProperties.getCapacity()),
                    Executors.defaultThreadFactory(),

                    // 当工作队列中的任务已到达最大限制，并且线程池中的线程数量也达到最大限制，
                    // 这时如果有新任务提交进来，直接丢弃任务，并抛出RejectedExecutionException异常。
                    new ThreadPoolExecutor.AbortPolicy());
        }
        return executorService;
    }

    public IExportManager getExportManager(ExportExecutorProperties exportExecutorProperties) {
        if (exportManager == null) {
            exportManager = new ExportManager(exportExecutorProperties);
        }
        return exportManager;
    }

    public IExportManager getExportManager() {
        return exportManager;
    }
}
