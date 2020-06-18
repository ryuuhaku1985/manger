package com.jdxl.common.validator;

import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.DefaultMsgInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 */
@Component
@Service("ValidatorUtilsNewton")
public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        ValidatorUtils.messageSource = messageSource;
    }

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws BizException  校验不通过，则报BizException异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BizException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {

            Iterator<ConstraintViolation<Object>> iterator =  constraintViolations.iterator();
            StringBuilder sb = new StringBuilder("");
            while (iterator.hasNext()) {
                ConstraintViolation<Object> constraint = iterator.next();
                String message = constraint.getMessage();
                // TODO msg 判断
                // messageSource

                if (iterator.hasNext()) {
                    sb.append(message).append("<br/>");
                } else {
                    sb.append(message);
                }
            }
            throw new BizException(DefaultMsgInfo.CUSTOM_SERVER_ERROR, sb.toString());
        }
    }
}
