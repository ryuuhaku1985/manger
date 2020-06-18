package com.jdxl.datasources.config;

import com.jdxl.datasources.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DynamicDataSourceProperties.class})
public class DynamicDataSourceConfigurationProperties {
}
