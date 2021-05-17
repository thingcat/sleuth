package com.sleuth.network.message.queue;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sleuth.network.message.TableProtocolConsumer;
import com.sleuth.network.message.protocol.TabProtocol;

/** 数据消费
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
@Component
public class QueueProtocolConsumerImpl implements TableProtocolConsumer {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final Queue<TabProtocol> dataQueue = new LinkedBlockingQueue<TabProtocol>(Integer.MAX_VALUE);
	
	//记录每个数据消息的ID，这样如果存在过，说明已经被接收到了，阻断传播链路，防止无限传播
	static final List<String> dataids = Lists.newCopyOnWriteArrayList();
	static final int IDS_MAX_LENGTH = 10000;
	
	@Override
	public void push(TabProtocol message) {
		if (message != null) {
			String uid = message.getUid();
			if (!dataids.contains(uid)) {
				dataQueue.offer(message);
//				dataids.add(uid);
			}
		}
	}
	
	@Override
	public long size() {
		return dataQueue.size();
	}

	@Override
	public TabProtocol poll() {
		return dataQueue.poll();
	}

	@Override
	public void clearIds() {
		int size = dataids.size();
		if (size > IDS_MAX_LENGTH) {
			//移除前面的元素
			int m = size - IDS_MAX_LENGTH;
			for(int i=0; i<m; i++) {
				dataids.remove(i);
			}
		}
	}

	@Override
	public boolean exists(String msgUid) {
		return dataids.contains(msgUid);
	}

	@Override
	public void joinUidBuffer(String uid) {
		if (!dataids.contains(uid)) {
			dataids.add(uid);
		}
	}
	
}
