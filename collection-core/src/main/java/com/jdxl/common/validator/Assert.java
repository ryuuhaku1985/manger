package com.jdxl.common.validator;

import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.MsgInfo;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验基类
 */
public abstract class Assert {

    public static void isBlank(String str, MsgInfo message, String... args) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(message, args);
        }
    }

    public static void isNull(Object object, MsgInfo message, String... args) {
        if (object == null) {
            throw new BizException(message, args);
        }
    }
}
