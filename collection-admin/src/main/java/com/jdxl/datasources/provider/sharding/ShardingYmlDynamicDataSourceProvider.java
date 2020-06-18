package com.jdxl.datasources.provider.sharding;

import com.jdxl.datasources.provider.DynamicDataSourceProvider;
import com.jdxl.datasources.spring.boot.autoconfigure.DataSourceProperty;
import com.jdxl.datasources.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.jdxl.datasources.toolkit.DataSourceFactory;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * YML数据源提供者
 */
@Slf4j
public class ShardingYmlDynamicDataSourceProvider implements DynamicDataSourceProvider {

    private DynamicDataSourceProperties properties;

    public ShardingYmlDynamicDataSourceProvider(DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

    private Map<String, DataSource> shardingJdbcDataSourcesMap = new HashMap<>();

    public void addShardingJdbcDatasource(String key, DataSource shardingDataSource){
        this.shardingJdbcDataSourcesMap.put(key, shardingDataSource);
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSourceProperty> dataSourcePropertiesMap = properties.getDatasource();
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size());
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            dataSourceMap.put(item.getKey(), DataSourceFactory.createDataSource(item.getValue()));
        }

        if (!shardingJdbcDataSourcesMap.isEmpty()) {
            dataSourceMap.putAll(shardingJdbcDataSourcesMap);
        }

        return dataSourceMap;
    }

}
