package com.jdxl.datasources.annotation;

import java.lang.annotation.*;

/**
 * 注解在类上或方法上来切换数据源
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    /**
     * 组名或者具体数据源名称
     *
     * @return 数据源名称
     */
    String value();

}