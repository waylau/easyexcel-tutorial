package com.waylau.easyexcel.demo;


import com.waylau.easyexcel.demo.data.StyleData;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class ExportExcutorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcutorTest.class);

    @Test
    public void testForSubmit() throws IOException {
        ExportExcutorProperties exportExcutorProperties = new ExportExcutorProperties();
        ExportExcutor exportExcutor = new ExportExcutor(exportExcutorProperties);
        List<CompletableFuture<ExportResult>> resultList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ExportTask task = new ExportTask();
            task.addSheet(new SheetParam(new ArrayList<String>().getClass(), "sheet1", new WriteBeanTest().data(2)));
            task.addSheet(new SheetParam(StyleData.class, "sheet2", new WriteBeanTest().styleData(2)));

            resultList.add(exportExcutor.submit(task));
        }

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

    }

}
