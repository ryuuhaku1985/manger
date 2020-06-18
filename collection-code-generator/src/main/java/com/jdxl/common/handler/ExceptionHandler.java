package com.jdxl.common.handler;

import com.alibaba.fastjson.JSON;
import com.jdxl.common.exception.BizException;
import com.jdxl.common.message.DefaultMsgInfo;
import com.jdxl.common.result.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理器
 */
@Component
@Slf4j
public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ErrorResult error  = null;
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("utf-8");

			if (ex instanceof BizException) {
                BizException bizex = (BizException) ex;
                error = ErrorResult.response(bizex.getErrorInfo(), bizex.getArgs());
			} else if(ex instanceof DuplicateKeyException){
                error = ErrorResult.response(DefaultMsgInfo.CUSTOM_SERVER_ERROR, "数据库中已存在该记录");
			} else {
				// 其他异常，报统一错误信息
                error = ErrorResult.response(DefaultMsgInfo.DEFAULT_SERVER_ERROR);
			}

			// 记录异常日志
			log.error(ex.getMessage(), ex);
			String json = JSON.toJSONString(error);
			response.getWriter().print(json);
		} catch (Exception e) {
			log.error("ExceptionHandler 异常处理失败", e);
		}
		return new ModelAndView();
	}
}
