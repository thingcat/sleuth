package com.sleuth.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sleuth.api.JsonReponse;
import com.sleuth.api.NetworkApi;
import com.sleuth.api.dto.TopologyDTO;

@Controller
@RequestMapping("/api/network")
public class NetworkController {
	
	@Resource
	private NetworkApi networkApi;
	
	@ResponseBody
	@RequestMapping(value="/topology", method=RequestMethod.GET)
	public JsonReponse topology(HttpServletRequest request) {
		TopologyDTO topology = networkApi.getTopology();
		return new JsonReponse(topology);
	}

}
