package com.sleuth.network.message.queue;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.network.message.TableProtocolConsumer;
import com.sleuth.network.message.TableProtocolHandler;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.Op;
import com.sleuth.network.message.protocol.TabProtocol;

public class QueueRunner implements Runnable {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private TableProtocolConsumer tableConsumer;
	
	private TableProtocolHandler pushTableHandler;
	private TableProtocolHandler syncTableHandler;
	
	public QueueRunner(TableProtocolConsumer tableConsumer, TableProtocolHandler pushTableHandler, TableProtocolHandler syncTableHandler) {
		this.tableConsumer = tableConsumer;
		this.pushTableHandler = pushTableHandler;
		this.syncTableHandler = syncTableHandler;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("TableConsumer");
		while(true) {
			long size = this.tableConsumer.size();
			if (size > 0) {
				this.handle(size);
			}
			
			try {
				TimeUnit.SECONDS.sleep(3);
				this.tableConsumer.clearIds();
			} catch (InterruptedException e) {
				logger.error("Thread sleep error: ", e);
			}
		}
	}
	
	private void handle(long size) {
		if (size > 0) {
			for(int i=0; i<size; i++) {
				TabProtocol message = this.tableConsumer.poll();
				try {
					logger.debug(message.toString());
					CmdType ch = CmdType.valueOf2(message.getCh());
					if (ch != null) {
						//推送消息
						if (Op.push.getOp().equals(ch.getOp())) {
							this.pushTableHandler.handle(this.tableConsumer, message);
						} 
						//同步消息
						else if (Op.sync.getOp().equals(ch.getOp())) {
							this.syncTableHandler.handle(this.tableConsumer, message);
						}
					}
				} catch (Exception e) {
					message.setUsed(message.getUsed()+1);
					if (message.getUsed() < 5) {
						this.tableConsumer.push(message);
					}
					logger.error("Table protocol consumer error, ", e);
				}
			}
			
			this.handle(this.tableConsumer.size());
			
		}
	}

}
