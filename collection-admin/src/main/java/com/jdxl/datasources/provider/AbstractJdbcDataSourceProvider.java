package com.jdxl.datasources.provider;

import com.jdxl.datasources.spring.boot.autoconfigure.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static com.jdxl.datasources.toolkit.DataSourceFactory.createDataSource;

/**
 * JDBC数据源提供者(抽象)
 */
@Slf4j
public abstract class AbstractJdbcDataSourceProvider implements DynamicDataSourceProvider {

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

    public AbstractJdbcDataSourceProvider(String driverClassName, String url, String username, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(driverClassName);
            log.info("成功加载数据库驱动程序");
            conn = DriverManager.getConnection(url, username, password);
            log.info("成功获取数据库连接");
            stmt = conn.createStatement();
            Map<String, DataSourceProperty> dataSourcePropertiesMap = executeStmt(stmt);
            Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size());
            for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
                dataSourceMap.put(item.getKey(), createDataSource(item.getValue()));
            }
            return dataSourceMap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
        }
        return null;
    }

    protected abstract Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException;

}
