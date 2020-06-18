package com.jdxl.common.service;

/**
 * 注入SpringContext中自身的代理
 */
public interface BeanSelfAware {
    /**
     * InjectBeanSelfProcessor执行时调用方法，注入自身的代理
     * @param proxy 代理对象
     */
    void setSelf(Object proxy);
}
