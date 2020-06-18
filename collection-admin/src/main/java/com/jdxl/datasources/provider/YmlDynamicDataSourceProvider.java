package com.jdxl.datasources.provider;

import com.jdxl.datasources.spring.boot.autoconfigure.DataSourceProperty;
import com.jdxl.datasources.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.jdxl.datasources.toolkit.DataSourceFactory.createDataSource;

/**
 * YML数据源提供
 */
@Slf4j
public class YmlDynamicDataSourceProvider implements DynamicDataSourceProvider {

    private DynamicDataSourceProperties properties;

    public YmlDynamicDataSourceProvider(DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSourceProperty> dataSourcePropertiesMap = properties.getDatasource();
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size());
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            dataSourceMap.put(item.getKey(), createDataSource(item.getValue()));
        }
        return dataSourceMap;
    }

}
