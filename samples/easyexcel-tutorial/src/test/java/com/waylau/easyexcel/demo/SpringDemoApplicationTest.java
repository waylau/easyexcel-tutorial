package com.waylau.easyexcel.demo;


import com.waylau.easyexcel.demo.data.LargeData;
import com.waylau.easyexcel.demo.data.StyleData;
import com.waylau.easyexcel.demo.util.TestDataGenerator;
import com.waylau.easyexcel.export.*;
import com.waylau.easyexcel.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDemoApplication.class})
public class SpringDemoApplicationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDemoApplicationTest.class);

    @Autowired
    private IExportExcutor exportExcutor;

    @Test
    public void testForSubmit() throws IOException {
        List<CompletableFuture<ExportResult>> resultList = new ArrayList<>();
        List<String> taskList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ExportTask task = new ExportTask();
            task.addSheet(new ExportSheet(LargeData.class, "sheet1", TestDataGenerator.getLargeData(200)));
            task.addSheet(new ExportSheet(StyleData.class, "sheet2", TestDataGenerator.getStyleData(20)));

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
            String fileName = "d://large" + IdUtil.createID() + ".xlsx";

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
    }

}
