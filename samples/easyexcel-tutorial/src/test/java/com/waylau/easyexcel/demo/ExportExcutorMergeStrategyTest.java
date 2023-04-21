package com.waylau.easyexcel.demo;

import com.waylau.easyexcel.demo.data.MergeData;
import com.waylau.easyexcel.demo.util.TestDataGenerator;
import com.waylau.easyexcel.export.*;
import com.waylau.easyexcel.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 导出任务执行器使用MergeStrategy Test
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-08
 */
public class ExportExcutorMergeStrategyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcutorMergeStrategyTest.class);

    @Test
    public void testForSubmit() throws IOException {
        ExportExecutorProperties exportExecutorProperties = new ExportExecutorProperties();
        ExportExcutor exportExcutor = new ExportExcutor(exportExecutorProperties);
        List<CompletableFuture<ExportResult>> resultList = new ArrayList<>();
        List<String> taskList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ExportTask task = new ExportTask();
            // 使用了合并策略
            task.addSheet(new ExportSheet(MergeData.class, "sheet1", TestDataGenerator.getMergeData(), Arrays.asList(new ExportMergeStrategy(TestDataGenerator.getMergeData().size(), 0, 1, 2, 3))));
            // 没有使用合并策略
            task.addSheet(new ExportSheet(MergeData.class, "sheet2", TestDataGenerator.getMergeData(), null));

            resultList.add(exportExcutor.submit(task));
            taskList.add(task.getTaskId());
        }

        // 监控进度
        CompletableFuture<Boolean> finish = CompletableFuture.supplyAsync(() -> {
            while (true) {
                int i = 0;
                for (String taskId : taskList) {
                    List<ExportProgress> exportProgressList = exportExcutor.getExportManager().queryStatus(taskId);
                    LOGGER.info("logging {}", exportProgressList);

                    ExportProgress lastProgress = exportProgressList.get(exportProgressList.size() - 1);
                    if (lastProgress.getValue() != 0 && lastProgress.getValue() == lastProgress.getMax()) {
                        i++;
                    }

                    if (i == taskList.size()) {
                        return true;
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        });

        for (CompletableFuture<ExportResult> result : resultList) {
            ExportResult exportResult = result.join();
            ByteArrayOutputStream byteArrayOutputStream = exportResult.getByteArrayOutputStream();

            // 将表格导入本地文件
            FileOutputStream fileOutputStream = null;
            String fileName = "d://ExportExcutorMergeStrategyTest" + IdUtil.createID() + ".xlsx";

            try {
                fileOutputStream = new FileOutputStream(fileName);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fileOutputStream.close();
            }

            LOGGER.info("done taskId:{}", exportResult.getTaskId());
        }

        // 不停，便于观察日志
        while (!finish.join()) {
        }

        // 关闭执行器
        exportExcutor.close();
    }

}
