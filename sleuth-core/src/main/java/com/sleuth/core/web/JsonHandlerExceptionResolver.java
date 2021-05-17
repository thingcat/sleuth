package com.sleuth.core.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.sleuth.core.web.exception.ResponseCodeException;
import com.sleuth.core.web.validator.ValidatorException;

public class JsonHandlerExceptionResolver implements HandlerExceptionResolver {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView view = new ModelAndView();
		FastJsonJsonView jsonView = new FastJsonJsonView();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		if (ex instanceof ValidatorException) {
			ValidatorException exception = (ValidatorException) ex;
			String message = Messages.getString(exception.getMessage());
			attributes.put("message", message == null ? "Unknown error" : message);
			attributes.put("success", false);
		} else if (ex instanceof ResponseCodeException) {
			ResponseCodeException exception = (ResponseCodeException) ex;
			String message = Messages.getString(exception.getMessage());
			attributes.put("message", message == null ? "Unknown exception" : message);
			attributes.put("success", false);
		}
		
		logger.error("An exception occurred in the request", ex);
		logger.error("Response info = {}", attributes.toString());
		jsonView.setAttributesMap(attributes);
		view.setView(jsonView);
		return view;
	}

}
