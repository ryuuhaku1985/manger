package com.jdxl.datasources.spring.boot.autoconfigure;

import com.jdxl.datasources.strategy.DynamicDataSourceStrategy;
import com.jdxl.datasources.strategy.LoadBalanceDynamicDataSourceStrategy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DynamicDataSourceProperties
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceProperties {

    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";

    /**
     * 是否开启对mybatis-plus的支持，默认false
     */
    private boolean mpEnabled = false;

    /**
     * 每一个数据源
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();

    /**
     * 多数据源选择算法clazz，默认负载均衡算法
     */
    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;

    /**
     * aop切面顺序，默认优先级最高
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;

}
