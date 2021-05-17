package com.sleuth.network.message.sync.apply;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.BlockChain;
import com.sleuth.block.schema.Block;
import com.sleuth.network.message.CmdApply;
import com.sleuth.network.message.protocol.CmdType;

/** 申请区块同步
 * 
 * @author Jonse
 * @date 2021年5月15日
 */
public class BlockApplyTimer extends TimerTask {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private CmdApply cmdApply;
	private BlockChain blockChain;
	
	public BlockApplyTimer(CmdApply cmdApply, BlockChain blockChain) {
		this.blockChain = blockChain;
		this.cmdApply = cmdApply;
	}
	
	@Override
	public void run() {
		//本地最新的区块，告诉服务端同步停止的区块高度
		Block block = this.blockChain.currentBlock();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("height", block.getHeight());
		//申请区块同步
		this.cmdApply.onApply(CmdType.BLOCK_SYNC, data);
		logger.info("Apply for synchronization block, height = {}", block.getHeight());
	}

}
