package com.sleuth.network.message;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.sleuth.network.message.queue.QueueRunner;

/** 启动一个消费者线程用于消费数据消息
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
@Component
public class TableProtocolConsumerListener implements InitializingBean {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TableProtocolConsumer tableProtocolConsumer;
	
	@Resource(name="pushTableProtocolHandler")
	private TableProtocolHandler pushTableHandler;
	
	@Resource(name="syncTableProtocolHandler")
	private TableProtocolHandler syncTableHandler;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("\n\n---------Start TabProtocol consumer thread---------\n");
		new Thread(new QueueRunner(tableProtocolConsumer, pushTableHandler, syncTableHandler)).start();
	}

}
