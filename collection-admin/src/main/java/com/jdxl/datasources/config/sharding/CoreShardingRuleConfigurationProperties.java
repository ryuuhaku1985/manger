package com.jdxl.datasources.config.sharding;

import io.shardingjdbc.core.yaml.sharding.YamlShardingRuleConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自动加载ShardingJdbc的分片配置
 */
@ConfigurationProperties(prefix = "sharding.jdbc.biz.config.sharding")
public class CoreShardingRuleConfigurationProperties extends YamlShardingRuleConfiguration {
}
