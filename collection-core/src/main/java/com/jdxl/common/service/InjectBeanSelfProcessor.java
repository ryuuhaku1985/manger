package com.jdxl.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 向实现了BeanSelfAware接口并且具有@Service注解的类注入其自身的Spring代理类
 * 刻用于解决
 * 1、调用自身方法时，事务不起作用的问题
 * 2、@DataSource注解切片被执行后，调用this.method()时方法注解声明被Clear掉的问题
 * </pre>
 * @see com.jdxl.common.service.BeanSelfAware
 * @see org.springframework.beans.factory.config.BeanPostProcessor
 * @see import org.springframework.context.ApplicationContextAware
 */
@Component
@Slf4j
public class InjectBeanSelfProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BeanSelfAware) {
            BeanSelfAware myBean = (BeanSelfAware) bean;

            //直接从context中获取bean，确保获取的是代理类（建立事务）如果申明的是@Service，但没有Proxy的类，将从Context中获取，
            // 如果仍然获取不到代理类，或者获取出现BeanCurrentlyInCreationException异常，使用原有的类 。出现此问题的原因还是serivce互相引用
            if (!AopUtils.isAopProxy(bean)) {
                //if @Service
                Class c = bean.getClass();
                Service serviceAnnotation = (Service) c.getAnnotation(Service.class);
                if (serviceAnnotation != null) {
                    //service 互相引用，需要从Context从获取代理后的Bean
                    try {
                        bean = context.getBean(beanName);
                        if (!AopUtils.isAopProxy(bean)) {
                            //仍然不是Proxy
                            log.warn("No Proxy Bean for service {}", bean.getClass());
                        }
                    } catch (BeanCurrentlyInCreationException ex) {
                        //告警，但仍然在一个No Proxy的情况正常运行
                        log.warn("No Proxy Bean for service {}", bean.getClass(), ex);
                    } catch (Exception ex) {
                        //告警，但仍然在一个No Proxy的情况正常运行
                        log.warn("No Proxy Bean for service {}", bean.getClass(), ex);
                    }
                }
            }
            myBean.setSelf(bean);
            return myBean;
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
