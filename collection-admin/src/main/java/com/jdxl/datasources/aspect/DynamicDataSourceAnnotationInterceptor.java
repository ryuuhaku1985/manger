package com.jdxl.datasources.aspect;

import com.jdxl.datasources.annotation.DataSource;
import com.jdxl.datasources.support.MybatisPlusResolver;
import com.jdxl.datasources.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 动态数据源AOP核心拦截器
 */
@Slf4j
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    private boolean mpEnabled;

    private MybatisPlusResolver mybatisPlusResolver;

    public DynamicDataSourceAnnotationInterceptor(boolean mpEnabled) {
        this.mpEnabled = mpEnabled;
        if (mpEnabled) {
            mybatisPlusResolver = new MybatisPlusResolver();
        }
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            log.debug("setDataSourceLookupKey");
            DynamicDataSourceContextHolder.setDataSourceLookupKey(determineDatasource(invocation));
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceLookupKey();
            log.debug("clearDataSourceLookupKey");
        }
    }

    private String determineDatasource(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        if (mpEnabled) {
            declaringClass = mybatisPlusResolver.targetClass(invocation);
        }
        DataSource ds = method.isAnnotationPresent(DataSource.class) ? method.getAnnotation(DataSource.class)
                : AnnotationUtils.findAnnotation(declaringClass, DataSource.class);
        return ds.value();
    }

}