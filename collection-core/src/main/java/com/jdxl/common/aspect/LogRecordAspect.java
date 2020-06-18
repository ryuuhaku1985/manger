package com.jdxl.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 定义接口的方法切面
 */
@Aspect
@Configuration
@Slf4j
@Service("LogRecordAspectNewton")
public class LogRecordAspect {
    /**
     * 定义切点Pointcut
     */
    @Pointcut("execution(public * *.*..*Controller.*(..))")
    public void executeApi() {
        // Pointcut has no content
    }

    /** 用于存放方法执行时间  */
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 方法开始前切入
     *
     * @param joinPoint 织入点
     * @throws Throwable 异常
     */
    @Before("executeApi()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("PROFILE : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 方法执行后切入
     *
     * @param ret 返回值
     * @throws Throwable 异常
     */
    @AfterReturning(returning = "ret", pointcut = "executeApi()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("RESPONSE : " + ret);
        log.info("ELAPSED : " + (System.currentTimeMillis() - startTime.get()));
    }


}
