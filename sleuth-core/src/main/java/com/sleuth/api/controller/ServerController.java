package com.sleuth.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sleuth.api.ServerApi;
import com.sleuth.api.JsonReponse;

@Controller
@RequestMapping("/server")
public class ServerController {
	
	@Resource
	private ServerApi serverApi;
	
	@ResponseBody
	@RequestMapping(value="/start", method=RequestMethod.GET)
	public JsonReponse start(HttpServletRequest request) {
		serverApi.start(request);
		return new JsonReponse();
	}
	
	@ResponseBody
	@RequestMapping(value="/reload", method=RequestMethod.GET)
	public JsonReponse reload(HttpServletRequest request) {
		serverApi.reload(request);
		return new JsonReponse();
	}
	
	@ResponseBody
	@RequestMapping(value="/stop", method=RequestMethod.GET)
	public JsonReponse stop(HttpServletRequest request) {
		serverApi.stop(request);
		return new JsonReponse();
	}

}
