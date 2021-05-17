package com.sleuth.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sleuth.api.JsonReponse;
import com.sleuth.api.OAuthApi;
import com.sleuth.api.dto.OAuthDTO;
import com.sleuth.api.form.KeyPairForm;
import com.sleuth.core.web.annotation.Valid;

@Controller
@RequestMapping("/api/oauth")
public class OAuthController {
	
	@Resource
	private OAuthApi oAuthApi;
	
	@ResponseBody
	@RequestMapping(value="/info", method=RequestMethod.GET)
	public JsonReponse info(HttpServletRequest request) {
		OAuthDTO oAuth = oAuthApi.get();
		return new JsonReponse(oAuth);
	}
	
	@ResponseBody
	@RequestMapping(value="/import", method=RequestMethod.POST)
	public JsonReponse imports(@Valid KeyPairForm form, HttpServletRequest request) {
		OAuthDTO oAuth = oAuthApi.imports(form);
		return new JsonReponse(oAuth);
	}
	
}
