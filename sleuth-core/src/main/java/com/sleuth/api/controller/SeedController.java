package com.sleuth.api.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sleuth.api.JsonReponse;
import com.sleuth.api.SeedApi;
import com.sleuth.api.dto.NodeDTO;

@Controller
@RequestMapping("/seed")
public class SeedController {
	
	@Resource
	private SeedApi seedApi;
	
	@ResponseBody
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public JsonReponse nodes(HttpServletRequest request) {
		List<NodeDTO> dtos = seedApi.list();
		return new JsonReponse(dtos);	
	}
	
	@ResponseBody
	@RequestMapping(value="/import", method=RequestMethod.POST)
	public JsonReponse imports(HttpServletRequest request) {
		String jsonText = request.getParameter("jsonText");
		seedApi.imports(jsonText);
		return new JsonReponse();	
	}
	
	@ResponseBody
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public JsonReponse export(HttpServletRequest request) {
		List<NodeDTO> dtos = seedApi.list();
		return new JsonReponse(dtos);	
	}

}
