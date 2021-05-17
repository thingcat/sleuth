package com.sleuth.network.message.sync.apply;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.schema.Block;
import com.sleuth.block.service.Iterator;
import com.sleuth.network.message.CmdApply;
import com.sleuth.network.message.protocol.CmdType;

/** 指令同步申请
 * 
 * 客户端发送指令来获得相关资源
 * 
 * @author Jonse
 * @date 2021年5月15日
 */
public class MerkleApplyTimer extends TimerTask {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private CmdApply cmdApply;
	private Iterator iterator;
	
	public MerkleApplyTimer(CmdApply cmdApply, Iterator iterator) {
		this.cmdApply = cmdApply;
		this.iterator = iterator;
	}
	
	@Override
	public void run() {
		//迭代区块
		while(this.iterator.hasNext()) {
			try {
				Block block = this.iterator.next();
				
				Map<String, String> data = new HashMap<String, String>();
				//WebURI的默克尔树同步申请
				data.put("wxRoot", block.getWxRoot());
				//交易的默克尔树同步申请
				data.put("txRoot", block.getTxRoot());
				//申请同步
				this.cmdApply.onApply(CmdType.WX_MERKLE_SYNC, data);
				this.cmdApply.onApply(CmdType.TX_MERKLE_SYNC, data);
				
				logger.info("Apply for synchronization merkles, wxRoot = {}, txRoot = {}", 
						block.getWxRoot(), block.getTxRoot());
				TimeUnit.MILLISECONDS.sleep(200);
				
			} catch (Exception e) {
				logger.error("merkles apply sync faild.", e);
			}
		}
	}

}
