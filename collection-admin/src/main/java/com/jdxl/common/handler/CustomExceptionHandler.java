package com.jdxl.common.handler;

import com.google.common.base.Strings;
import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.DefaultMsgInfo;
import com.jdxl.common.message.MessageUtils;
import com.jdxl.common.message.MsgInfo;
import com.jdxl.common.result.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 面向最终客户端的异常处理 以JSON的形式返回给客户端
 * <pre>
 * 异常增强类型：
 * BizException, DuplicateKeyException, NoHandlerFoundException, Exception
 * </pre>
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    /**
     * 拦截BizException
     *
     * @param request 请求信息
     * @param ex      拦截的异常
     * @return 错误信息
     */
    @ExceptionHandler(value = BizException.class)
    public ErrorResult handleBizException(HttpServletRequest request, BizException ex) {

        if (ex.getCause() != null) {
            log.error(ex.getMessage(), ex);
        }

        return translateErrorResult(ex.getErrorInfo(), ex.getArgs());
    }

    /**
     * 拦截DuplicateKeyException
     *
     * @param request 请求信息
     * @param ex      拦截的异常
     * @return 错误信息
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ErrorResult handleDuplicateKeyException(HttpServletRequest request, DuplicateKeyException ex){
        log.error(ex.getMessage(), ex);
        return translateErrorResult(DefaultMsgInfo.CUSTOM_SERVER_ERROR, "数据库中已存在该记录");
    }

    /**
     * 拦截DuplicateKeyException
     *
     * @param request 请求信息
     * @param ex      拦截的异常
     * @return 错误信息
     */
    @ExceptionHandler(AuthorizationException.class)
    public ErrorResult handleAuthorizationException(HttpServletRequest request, AuthorizationException ex){
        log.error(ex.getMessage(), ex);
        return translateErrorResult(DefaultMsgInfo.UNAUTHORIZED, "没有权限，请联系管理员授权");
    }

    /**
     * 拦截NoHandlerFoundException
     *
     * @param request 请求信息
     * @param ex      拦截的异常
     * @return 错误信息
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorResult handlerNoFoundException(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage(), ex);
        return translateErrorResult(DefaultMsgInfo.NOT_FOUND);
    }

    /**
     * 拦截JSR303校验异常MethodArgumentNotValidException或者BindException
     *
     * @param request 请求信息
     * @param ex      拦截的异常
     * @return 错误信息
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ErrorResult handleValidationExceptions(HttpServletRequest request, Exception ex) {

        List<ObjectError> objectErrors = new ArrayList<>();
        if (ex instanceof MethodArgumentNotValidException) {
            objectErrors = ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors();
        } else if (ex instanceof BindException) {
            objectErrors = ((BindException) ex).getBindingResult().getAllErrors();
        }

        List<String> errors = new ArrayList<>();
        for (ObjectError error : objectErrors) {
            String message = MessageUtils.getMessage(error, LocaleContextHolder.getLocale());
            errors.add(message);
        }
        Iterator<String> iterator =  errors.iterator();
        StringBuilder sb = new StringBuilder("");
        while (iterator.hasNext()) {
            String msg = iterator.next();
            if (iterator.hasNext()) {
                sb.append(msg).append("<br/>");
            } else {
                sb.append(msg);
            }
        }

        return ErrorResult.response(DefaultMsgInfo.BAD_REQUEST.getCode(), sb.toString());
    }

    /**
     * 拦截其余全部Exception
     *
     * @param request 请求信息
     * @param ex      拦截的异常
     * @return 错误信息
     */
    @ExceptionHandler(value = Exception.class)
    public ErrorResult handleException(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage(), ex);
        return translateErrorResult(DefaultMsgInfo.DEFAULT_SERVER_ERROR);
    }

    private ErrorResult translateErrorResult(MsgInfo msgInfo, String... args) {
        String msgCode = msgInfo.getCode();
        String message = MessageUtils.getMessage(msgCode, args);

        if (Strings.isNullOrEmpty(message) || StringUtils.endsWithIgnoreCase(msgCode, message)) {
            // 取不到message或者properties没有定义相应的code，用MsgInfo中的默认消息兜底
            return ErrorResult.response(msgInfo, args);
        }

        return ErrorResult.response(msgCode, message);
    }
}
