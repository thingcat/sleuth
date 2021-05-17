package com.sleuth.network.message.handle;

import java.util.Timer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.core.socket.message.MessageHandle;
import com.sleuth.core.socket.message.MessageManager;
import com.sleuth.network.message.CmdApply;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.OkProtocol;

import io.netty.channel.Channel;

/** 完成任务的响应
 * 
 * @author Jonse
 * @date 2021年1月21日
 */
@Component("finishedHandle")
public class FinishedHandle implements MessageHandle<OkProtocol> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private CmdApply applicant;
	
	final Timer timer = new Timer();

	@Override
	public void handle(MessageManager manager, Channel channel, OkProtocol message) {
		String ch = message.getCh();
		//区块同步完成了
		if (CmdType.BLOCK_SYNC.getCh().equals(ch)) {
			logger.warn("block synchronization finished...............");
			
			//申请默克尔树同步
			
		}
	}
	
}
