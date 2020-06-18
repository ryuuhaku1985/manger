package com.jdxl.datasources.toolkit;


import lombok.extern.slf4j.Slf4j;

/**
 * 核心基于ThreadLocal的切换数据源工具类
 */
@SuppressWarnings("unchecked")
@Slf4j
public final class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> DATASOURCE_KEY_HOLDER = new ThreadLocal();

    private DynamicDataSourceContextHolder() {
    }

    public static String getDataSourceLookupKey() {
        String result = DATASOURCE_KEY_HOLDER.get();
        log.debug("get DS {}", result);
        return result;
    }

    public static void setDataSourceLookupKey(String dataSourceLookupKey) {
        DATASOURCE_KEY_HOLDER.set(dataSourceLookupKey);
        log.debug("Set DS {}", dataSourceLookupKey);
    }

    public static void clearDataSourceLookupKey() {
        DATASOURCE_KEY_HOLDER.remove();
        log.debug("Remove DS");
    }

}
