package com.sleuth.block.service;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.sleuth.block.BlockBuffer;
import com.sleuth.block.BlockChainManager;
import com.sleuth.block.dto.BlockDTO;
import com.sleuth.network.message.protocol.TabProtocol;

@Service
public class BlockBufferImpl implements BlockBuffer {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final Queue<BlockDTO> queue = Queues.newConcurrentLinkedQueue();
	
	@Resource
	private BlockChainManager blockChainManager;
	
	@Override
	public void onPreloading() {
		logger.info("loading block buffer pool........");
		logger.info("{} block objects were found waiting to be processed.\n", this.size());
		new Thread(new TaskBlockBuffer(this, blockChainManager)).start();
		logger.info("start block processing thread.");
	}
	
	@Override
	public void push(TabProtocol message) {
		BlockDTO block = JSON.parseObject(message.getData(), BlockDTO.class);
		block.setBc(message.getBc());
		queue.offer(block);
	}
	
	@Override
	public void repush(BlockDTO block) {
		queue.offer(block);
	}

	@Override
	public BlockDTO poll() {
		return queue.poll();
	}

	@Override
	public int size() {
		return queue.size();
	}
	
	@Override
	public List<BlockDTO> getBlocks(int page, int limit) {
		if (queue.size() > 0) {
			Iterator<BlockDTO> iterator = queue.iterator();
			List<BlockDTO> list = Lists.newArrayList();
			while(iterator.hasNext()) {
				list.add(iterator.next());
			}
			int fromIndex = (page - 1) * limit;
			int toIndex = fromIndex + limit;
			int length = list.size();
			if (toIndex > length) {
				toIndex = length;
				if (fromIndex > toIndex) {
					return null;
				}
			}
			try {
				return list.subList(fromIndex, toIndex);
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}
		return null;
	}
	
	@Override
	public List<BlockDTO> getBlocks(String tappHash) {
		if (queue.size() > 0) {
			Iterator<BlockDTO> iterator = queue.iterator();
			List<BlockDTO> list = Lists.newArrayList();
			while(iterator.hasNext()) {
				list.add(iterator.next());
			}
			return list;
		}
		return null;
	}
	
	/** 内部类，处理接收到的区块
	 * 
	 * @author Jonse
	 * @date 2021年2月6日
	 */
	public class TaskBlockBuffer implements Runnable {
		
		final Logger logger = LoggerFactory.getLogger(getClass());
		
		private final BlockBuffer blockBuffer;
		private final BlockChainManager blockChainManager;
		
		public TaskBlockBuffer(BlockBuffer blockBuffer, BlockChainManager blockChainManager) {
			this.blockBuffer = blockBuffer;
			this.blockChainManager = blockChainManager;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					if (this.blockBuffer.size() > 0) {
						this.handle(blockBuffer.poll());
					}
				} catch (Exception e) {
					logger.error("buffer error, ", e);
				}
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					logger.error("sleep error, ", e);
				}
			}
		}
		
		/** 处理区块
		 * 
		 * @param block
		 */
		private void handle(BlockDTO block) {
			//判断默克尔树是否接收完成，
			if (this.blockChainManager.checkMerkle(block)) {
				//校验区块是否合法
				if (this.blockChainManager.verify(block)) {
					//是否为孤块
					if (!this.blockChainManager.isOrphanBlock(block)) {
						//保存区块到本地
						this.blockChainManager.pushResult(block);
					} else {
						//保存到孤块池
						this.blockChainManager.addOrphanBlock(block);
					}
					//进入下一次迭代
					if (this.blockBuffer.size() > 0) {
						this.handle(this.blockBuffer.poll());
					}
				}
			} else {
				//如果没有接收完成，则重新放入缓冲池，等待接收完成
				this.blockBuffer.repush(block);
			}
		}
	}
	
}
