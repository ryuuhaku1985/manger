package com.jdxl.datasources.support;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.binding.MapperProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 对mybatis-plus的支持
 */
@Slf4j
public class MybatisPlusResolver {

    private static Field field;

    static {
        Class<?> proxyClass;
        try {
            proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.PageMapperProxy");
        } catch (ClassNotFoundException e) {
            log.debug("未适配 mybatis-plus3,适配 mybatis-plus2");
            proxyClass = MapperProxy.class;
        }
        try {
            field = proxyClass.getDeclaredField("mapperInterface");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public Class<?> targetClass(MethodInvocation invocation) throws IllegalAccessException {
        Object target = invocation.getThis();
        return Proxy.isProxyClass(target.getClass()) ? (Class) field.get(Proxy.getInvocationHandler(target)) : target.getClass();
    }

}
