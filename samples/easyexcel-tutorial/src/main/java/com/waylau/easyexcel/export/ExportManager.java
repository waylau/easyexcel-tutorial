package com.waylau.easyexcel.export;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出任务管理器
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 2023-03-07
 */
public class ExportManager implements IExportManager {
    private static final String CACHE_ALIAS = "EXPORT_PROGRESS_CACHE";

    private CacheManager cacheManager;

    public ExportManager(ExportExecutorProperties eportExcutorProperties) {
        if (cacheManager == null) {
            // 定义缓存
            this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                    .withCache(CACHE_ALIAS,
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, List.class,
                                            ResourcePoolsBuilder.heap(eportExcutorProperties.getProgerssEntries())) // 缓存条目数
                                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(eportExcutorProperties.getProgerssExpiration())))//缓存时间
                                    .build())
                    .build(true);
        }
    }

    private Cache<String, List> getExportProgressCache() {
        return this.cacheManager.getCache(CACHE_ALIAS, String.class, List.class);
    }

    @Override
    public void appendStatus(ExportProgress exportProgress) {
        if (exportProgress != null) {
            List<ExportProgress> exportProgressList = getExportProgressCache().get(exportProgress.getTaskId());

            if (exportProgressList == null) {
                exportProgressList = new ArrayList<>();
            }

            exportProgressList.add(exportProgress);

            getExportProgressCache().put(exportProgress.getTaskId(), exportProgressList);
        }
    }

    @Override
    public List<ExportProgress> queryStatus(String taskId) {
        return getExportProgressCache().get(taskId);
    }

    @Override
    public void close() {
        cacheManager.removeCache(CACHE_ALIAS);
        cacheManager.close();
    }
}
