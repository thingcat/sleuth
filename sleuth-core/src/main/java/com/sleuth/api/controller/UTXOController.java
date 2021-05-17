package com.sleuth.api.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sleuth.api.JsonReponse;

@Controller
@RequestMapping("/api/utxo")
public class UTXOController {

	
	@ResponseBody
	@RequestMapping(value="/payable", method=RequestMethod.GET)
	public JsonReponse list(HttpServletRequest request,
			@RequestParam(name="amount", required=true) BigDecimal amount,
			@RequestParam(name="address", required=true) String address) {
		
		return new JsonReponse();	
	}
	
}
