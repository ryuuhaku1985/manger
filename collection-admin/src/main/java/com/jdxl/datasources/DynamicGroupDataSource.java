package com.jdxl.datasources;

import com.jdxl.datasources.strategy.DynamicDataSourceStrategy;
import lombok.Data;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 分组数据源
 */
@Data
public class DynamicGroupDataSource {

    /**
     * 组名
     */
    private String groupName;

    /**
     * 策略
     */
    private DynamicDataSourceStrategy dynamicDataSourceStrategy;

    /**
     * 当前组下所有数据源
     */
    private List<DataSource> dataSources = new ArrayList<>();

    public DynamicGroupDataSource(String groupName, DynamicDataSourceStrategy dynamicDataSourceStrategy) {
        this.groupName = groupName;
        this.dynamicDataSourceStrategy = dynamicDataSourceStrategy;
    }

    public void addDatasource(DataSource dataSource) {
        dataSources.add(dataSource);
    }

    public DataSource determineDataSource() {
        return dynamicDataSourceStrategy.determineDataSource(dataSources);
    }

    public int size() {
        return dataSources.size();
    }

}
