package com.jdxl.datasources.toolkit;

import com.alibaba.druid.pool.DruidDataSource;
import com.jdxl.datasources.spring.boot.autoconfigure.DataSourceProperty;
import com.jdxl.datasources.spring.boot.autoconfigure.druid.DruidDataSourceProperties;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 数据源工厂
 */
public class DataSourceFactory {

    /**
     * Alibaba Druid 数据源
     */
    private static final String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";

    private static Method createMethod;
    private static Method typeMethod;
    private static Method urlMethod;
    private static Method usernameMethod;
    private static Method passwordMethod;
    private static Method driverClassNameMethod;
    private static Method buildMethod;

    static {
        Class builderClass = null;
        try {
            builderClass = Class.forName("org.springframework.boot.jdbc.DataSourceBuilder");
        } catch (Exception e) {
            try {
                builderClass = Class.forName("org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder");
            } catch (Exception e1) {
            }
        }
        try {
            createMethod = builderClass.getDeclaredMethod("create");
            typeMethod = builderClass.getDeclaredMethod("type", Class.class);
            urlMethod = builderClass.getDeclaredMethod("url", String.class);
            usernameMethod = builderClass.getDeclaredMethod("username", String.class);
            passwordMethod = builderClass.getDeclaredMethod("password", String.class);
            driverClassNameMethod = builderClass.getDeclaredMethod("driverClassName", String.class);
            buildMethod = builderClass.getDeclaredMethod("build");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty 数据源信息
     * @return 数据源
     */
    public static DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        if (type == null) {
            try {
                Class.forName(DRUID_DATASOURCE);
                return createDruidDataSource(dataSourceProperty);
            } catch (ClassNotFoundException e) {
            }
        } else if (DRUID_DATASOURCE.equals(type.getName())) {
            return createDruidDataSource(dataSourceProperty);
        }
        return createBasicDataSource(dataSourceProperty);
    }

    private static DataSource createBasicDataSource(DataSourceProperty dataSourceProperty) {
        try {
            Object o1 = createMethod.invoke(null);
            Object o2 = typeMethod.invoke(o1, dataSourceProperty.getType());
            Object o3 = urlMethod.invoke(o2, dataSourceProperty.getUrl());
            Object o4 = usernameMethod.invoke(o3, dataSourceProperty.getUsername());
            Object o5 = passwordMethod.invoke(o4, dataSourceProperty.getPassword());
            Object o6 = driverClassNameMethod.invoke(o5, dataSourceProperty.getDriverClassName());
            return (DataSource) buildMethod.invoke(o6);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DataSource createDruidDataSource(DataSourceProperty dataSourceProperty) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceProperty.getUrl());
        druidDataSource.setUsername(dataSourceProperty.getUsername());
        druidDataSource.setPassword(dataSourceProperty.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperty.getDriverClassName());

        DruidDataSourceProperties druidProperties = dataSourceProperty.getDruid();

        druidDataSource.setInitialSize(druidProperties.getInitialSize());
        druidDataSource.setMaxActive(druidProperties.getMaxActive());
        druidDataSource.setMinIdle(druidProperties.getMinIdle());
        druidDataSource.setMaxWait(druidProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
        druidDataSource.setMaxEvictableIdleTimeMillis(druidProperties.getMaxEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(druidProperties.getValidationQuery());
        druidDataSource.setValidationQueryTimeout(druidProperties.getValidationQueryTimeout());
        druidDataSource.setTestOnBorrow(druidProperties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidProperties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(druidProperties.isPoolPreparedStatements());
        druidDataSource.setMaxOpenPreparedStatements(druidProperties.getMaxOpenPreparedStatements());
        druidDataSource.setSharePreparedStatements(druidProperties.isSharePreparedStatements());
        druidDataSource.setConnectProperties(druidProperties.getConnectionProperties());
        try {
            druidDataSource.setFilters(druidProperties.getFilters());
            druidDataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

}
