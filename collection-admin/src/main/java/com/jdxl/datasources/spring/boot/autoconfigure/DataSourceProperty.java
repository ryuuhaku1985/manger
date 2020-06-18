package com.jdxl.datasources.spring.boot.autoconfigure;

import com.jdxl.datasources.spring.boot.autoconfigure.druid.DruidDataSourceProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * 连接池基础信息配置
 */
@Getter
@Setter
public class DataSourceProperty {

    /**
     * 连接池类型，如果不设置自动查找 Druid > HikariCp
     */
    private Class<? extends DataSource> type;

    /**
     * JDBC driver
     */
    private String driverClassName;

    /**
     * JDBC url 地址
     */
    private String url;

    /**
     * JDBC 用户名
     */
    private String username;

    /**
     * JDBC 密码
     */
    private String password;

    /**
     * Druid参数配置
     */
    @NestedConfigurationProperty
    private DruidDataSourceProperties druid = new DruidDataSourceProperties();

}
