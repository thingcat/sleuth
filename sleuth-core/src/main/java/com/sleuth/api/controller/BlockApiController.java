package com.sleuth.api.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sleuth.api.BlockApi;
import com.sleuth.api.JsonReponse;
import com.sleuth.api.dto.BlockDTO;

@Controller
@RequestMapping("/api/block")
public class BlockApiController {

	@Resource
	private BlockApi blockApi;
	
	@ResponseBody
	@RequestMapping(value="/find/{hash}", method=RequestMethod.GET)
	public JsonReponse find(HttpServletRequest request,
			@PathVariable(name="hash", required=true) String hash) {
		BlockDTO block = blockApi.find(hash);
		return new JsonReponse(block);	
	}
	
	@ResponseBody
	@RequestMapping(value="/chain/list", method=RequestMethod.GET)
	public JsonReponse findChain(HttpServletRequest request,
			@RequestParam(name="tappHash", required=true) String tappHash,
			@RequestParam(name="from", required=true) long fromHeight,
			@RequestParam(name="to", required=true) long toHeight) {
		List<BlockDTO> blocks = blockApi.findForHeight(tappHash, fromHeight, toHeight);
		return new JsonReponse(blocks);	
	}
	
	@ResponseBody
	@RequestMapping(value="/buffer/list", method=RequestMethod.GET)
	public JsonReponse buffer(HttpServletRequest request,
			@RequestParam(name="page", required=true, defaultValue="1") int page) {
		List<BlockDTO> blocks = blockApi.findBuffer(page);
		return new JsonReponse(blocks);	
	}
	
	@ResponseBody
	@RequestMapping(value="/tapp/buffer/list", method=RequestMethod.GET)
	public JsonReponse tappBuffer(HttpServletRequest request,
			@RequestParam(name="tappHash", required=true) String tappHash) {
		List<BlockDTO> blocks = blockApi.findBuffer(tappHash);
		return new JsonReponse(blocks);	
	}
	
}
