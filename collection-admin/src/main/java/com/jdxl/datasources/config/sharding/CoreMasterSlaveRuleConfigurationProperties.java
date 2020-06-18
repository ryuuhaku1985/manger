package com.jdxl.datasources.config.sharding;

import io.shardingjdbc.core.yaml.masterslave.YamlMasterSlaveRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自动加载ShardingJdbc的主从配置
 */
@ConfigurationProperties(prefix = "sharding.jdbc.biz.config.masterslave")
public class CoreMasterSlaveRuleConfigurationProperties extends YamlMasterSlaveRuleConfiguration {
}
