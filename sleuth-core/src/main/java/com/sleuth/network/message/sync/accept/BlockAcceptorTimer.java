package com.sleuth.network.message.sync.accept;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.block.schema.Block;
import com.sleuth.block.service.Iterator;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.network.message.protocol.ApProtocol;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.OkProtocol;
import com.sleuth.network.message.protocol.TabProtocol;

import io.netty.channel.Channel;

/** 区块数据同步任务
 * 
 * @author Jonse
 * @date 2021年3月12日
 */
public class BlockAcceptorTimer extends TimerTask {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private CmdType ch = CmdType.BLOCK_SYNC;
	
	private Channel channel;
	private Iterator iterator;
	private Long height;//需要同步的区块高度
	
	/** 节点数据同步任务
	 * 
	 * @param message
	 * @param channel
	 */
	public BlockAcceptorTimer(ApProtocol message, Channel channel, Iterator iterator) {
		this.channel = channel;
		this.iterator = iterator;
		JSONObject object = JSON.parseObject(message.getArgs());
		long height = object.getLong("height");
		this.height = height - 6 < 0 ? 0 : height - 6;
	}
	
	@Override
	public void run() {
		//迭代主链区块
		while(this.iterator.hasNext()) {
			try {
				Block block = this.iterator.next();
				if (block.getHeight() < this.height) {
					break;
				}
				TabProtocol table = TabProtocol.newProtocol(ch);
				table.setData(JSON.toJSONString(block));
				CLI.push(this.channel, table);
				logger.debug("sync block, hash={}, height={}", block.getHash(), block.getHeight());
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (Exception e) {
				logger.error("block sync faild.", e);
			}
		}
		//告诉客户端，完成区块同步
		OkProtocol ok = OkProtocol.newProtocol(CmdType.BLOCK_SYNC);
		CLI.push(this.channel, ok);
	}
	
}
