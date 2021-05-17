package com.sleuth.api.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sleuth.api.HashPowerApi;
import com.sleuth.block.GlobalPower;

@Service
public class HashPowerApiImpl implements HashPowerApi {
	
	@Resource
	private GlobalPower hashPower;
	
	@Override
	public long ghpCountValue(String tappHash) {
		/*TApp tapp = this.tappService.get(tappHash);
		if (tapp != null) {
			return this.hashPower.ghpCountValue(tapp);
		}*/
		return 0;
	}

}
