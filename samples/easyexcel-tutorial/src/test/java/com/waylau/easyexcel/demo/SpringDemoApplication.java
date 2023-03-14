package com.waylau.easyexcel.demo;

import com.waylau.easyexcel.export.ExportExcutor;
import com.waylau.easyexcel.export.ExportExecutorProperties;
import com.waylau.easyexcel.export.IExportExcutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDemoApplication {
    @Bean
    public IExportExcutor exportExcutor() {
        ExportExecutorProperties exportExecutorProperties = new ExportExecutorProperties();
        exportExecutorProperties.setCapacity(2);
        exportExecutorProperties.setCorePoolSize(2);
        exportExecutorProperties.setKeepAliveTime(10);
        exportExecutorProperties.setMaximumPoolSize(2);
        exportExecutorProperties.setProgerssEntries(100);
        exportExecutorProperties.setProgerssExpiration(86400);

        ExportExcutor exportExcutor = new ExportExcutor(exportExecutorProperties);
        return exportExcutor;
    }
}
