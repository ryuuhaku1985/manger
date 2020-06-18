package com.jdxl.datasources.config;

import com.jdxl.datasources.DynamicRoutingDataSource;
import com.jdxl.datasources.aspect.DynamicDataSourceAnnotationAdvisor;
import com.jdxl.datasources.aspect.DynamicDataSourceAnnotationInterceptor;
import com.jdxl.datasources.provider.DynamicDataSourceProvider;
import com.jdxl.datasources.provider.sharding.ShardingYmlDynamicDataSourceProvider;
import com.jdxl.datasources.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.jdxl.datasources.spring.boot.autoconfigure.druid.DruidDynamicDataSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 动态数据源核心自动配置类
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(DruidDynamicDataSourceConfiguration.class)
public class DynamicDataSourceConfiguration {

    private DynamicDataSourceProperties properties;

    @Autowired
    public void setProperties(DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

    private DataSource bizDataSource;

    // 每个shardingjdbc数据源照此配置
    // required=false是为了当 sharding.jdbc.enable:false 时避免启动时的Autowired错误
    @Autowired(required=false)
    @Qualifier("bizDataSource")
    public void setBizDataSource(DataSource bizDataSource) {
        this.bizDataSource = bizDataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider() throws SQLException {

        ShardingYmlDynamicDataSourceProvider provider = new ShardingYmlDynamicDataSourceProvider(properties);
        if(bizDataSource != null) {
            provider.addShardingJdbcDatasource("biz", bizDataSource);
        }

        return provider;
    }

    @Bean
    @ConditionalOnMissingBean
    @Primary
    public DynamicRoutingDataSource dynamicRoutingDataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        dynamicRoutingDataSource.setPrimary(properties.getPrimary());
        dynamicRoutingDataSource.setStrategy(properties.getStrategy());
        dynamicRoutingDataSource.setProvider(dynamicDataSourceProvider);
        return dynamicRoutingDataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor(properties.isMpEnabled());
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }

}