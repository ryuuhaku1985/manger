package com.jdxl.datasources.config.sharding;

import com.google.common.base.Preconditions;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.exception.ShardingJdbcException;
import io.shardingjdbc.core.util.DataSourceUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动初始化ShardingJdbc数据源
 */
@Configuration
@ConditionalOnProperty(name = "sharding.jdbc.enable", havingValue = "true")
@EnableConfigurationProperties({CoreShardingRuleConfigurationProperties.class, CoreMasterSlaveRuleConfigurationProperties.class})
public class CoreDataSourceConfiguration implements EnvironmentAware {

    private final CoreShardingRuleConfigurationProperties shardingProperties;

    private final CoreMasterSlaveRuleConfigurationProperties masterSlaveProperties;

    public CoreDataSourceConfiguration(CoreShardingRuleConfigurationProperties shardingProperties,
                                       CoreMasterSlaveRuleConfigurationProperties masterSlaveProperties) {
        this.shardingProperties = shardingProperties;
        this.masterSlaveProperties = masterSlaveProperties;
    }

    private final Map<String, DataSource> coreDataSourcesMap = new HashMap<>();

    /**
     * 初始化bizDataSource
     * @return ShardingJdbc数据源
     * @throws SQLException SQL异常
     */
    @Bean("bizDataSource")
    public DataSource shardingDataSource() throws SQLException {
        return null == masterSlaveProperties.getMasterDataSourceName()
                ? ShardingDataSourceFactory.createDataSource(coreDataSourcesMap,
                shardingProperties.getShardingRuleConfiguration(),
                shardingProperties.getConfigMap(),
                shardingProperties.getProps())
                : MasterSlaveDataSourceFactory.createDataSource(coreDataSourcesMap,
                masterSlaveProperties.getMasterSlaveRuleConfiguration(),
                masterSlaveProperties.getConfigMap());
    }

    /**
     * 自动配置Spring运行时环境
     * @param environment Springboot运行时环境
     */
    @Override
    public void setEnvironment(final Environment environment) {
        setDataSourceMap(environment);
    }

    /**
     * 实际的ShardingJdbc数据源初始化方法
     * @param environment Springboot运行时环境
     */
    private void setDataSourceMap(final Environment environment) {
        String dataSourceKeyPrefix  = "sharding.jdbc.biz.datasource.";
        String dataSources = environment.getProperty(dataSourceKeyPrefix + "names");
        for (String each : dataSources.split(",")) {
            try {
                Map dataSourceProps = Binder.get(environment)
                        .bind(dataSourceKeyPrefix + each, Map.class).orElse(null);

                Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
                DataSource dataSource = DataSourceUtil.getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
                coreDataSourcesMap.put(each, dataSource);
            } catch (final ReflectiveOperationException ex) {
                throw new ShardingJdbcException("Can't find datasource type!", ex);
            }
        }
    }

}
