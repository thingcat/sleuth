package com.sleuth.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sleuth.api.ConfigApi;
import com.sleuth.api.JsonReponse;
import com.sleuth.api.form.ConfigForm;
import com.sleuth.core.web.annotation.Valid;

@Controller
@RequestMapping("/api/config")
public class ConfigController {
	
	@Resource
	private ConfigApi configApi;
	
	@ResponseBody
	@RequestMapping(value="/info", method=RequestMethod.GET)
	public JsonReponse info(HttpServletRequest request) {
		return new JsonReponse(configApi.get());
	}
	
	@ResponseBody
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public JsonReponse update(@Valid ConfigForm form, HttpServletRequest request) {
		configApi.update(form);
		return new JsonReponse();
	}

}
