package com.sleuth.block.service;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Queues;
import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockService;
import com.sleuth.block.GlobalPower;
import com.sleuth.block.schema.Block;

@Service
public class HashGlobalPower implements GlobalPower {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 每隔 1024 个区块则进行算力调整 */
	public static final long height_breakpoint = 1024L;
	/** 控制出块的平均时间，当前值为10分钟 */
	public static final long middle_seconds = 10 * 60;
	/** 算力最小系数  */
	public static final int min_bit_ratio = 26;
	
	static final Queue<HashCounter> HashCounter = Queues.newConcurrentLinkedQueue();
	
	final Timer timer = new Timer();
	
	@Resource
	private BlockChain blockChain;
	@Resource
	private BlockService blockService;
	
	@Override
	public void calculate(Block newBlock) {
		Long nonce = newBlock.getNonce();
		Long startTime = newBlock.getStartTime();
		Long endTime = newBlock.getEndTime();
		HashCounter hashCounter = new HashCounter(nonce, startTime, endTime);
		HashCounter.offer(hashCounter);
		//调整算力
		if (newBlock.getHeight() % height_breakpoint == 0) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					update();
				}
			}, 0);
		}
	}

	@Override
	public void update() {
		long secondsCount = 0;//出块总时间，单位秒
		
		Block currentBlock = blockChain.currentBlock();
		Long height = currentBlock.getHeight();
		int bitsRatio = currentBlock.getBitsRatio();
		if (height > 0 && (height % height_breakpoint == 0)) {
			//理论值，总秒数
			long secondsValue = height_breakpoint * middle_seconds;
			//相差分钟数
			long subSeconds = Math.abs(secondsCount - secondsValue) / 60;
			if (subSeconds >= 2) {
				if (secondsValue > secondsCount) {
					bitsRatio--;
				} else {
					bitsRatio++;
				}
				if (bitsRatio < min_bit_ratio) {
					bitsRatio = min_bit_ratio;
				}
				//更新难度系数
				//TODO
			}
		}
		
	}

	@Override
	public long ghpCountValue() {
		
		long ghpCount = 0;//全网算力值
		
		HashCounter.forEach(e->{
			
		});
		/*
		Chain chain = this.blockChain.get(tappHash, tapp.getHeight());
		if (chain != null) {
			String[] branchs = chain.getBranchs();
			if (branchs == null) {
				Block block = this.blockService.get(chain.getBlockHash());
				if (block != null) {
					try {
						//出块时间
						long seconds = (block.getEndTime() - block.getStartTime()) / 1000;
						if (seconds > 0) {
							ghpCount += block.getNonce() / seconds;
						}
					} catch (Exception e) {
					}
				}
			} else {
				branchs = ArrayUtils.add(branchs, chain.getBlockHash());
				for (String branch : branchs) {
					Block block = this.blockService.get(branch);
					if (block != null) {
						//出块时间
			        	long seconds = (block.getEndTime() - block.getStartTime()) / 1000;
			        	if (seconds > 0) {
			        		ghpCount += block.getNonce() / seconds;
			        	}
					}
				}
			}
		}
		*/
		return ghpCount;
	}

	public class HashCounter {
		
		private Long nonce;
		private Long startTime;
		private Long endTime;
		
		public HashCounter() {
			
		}
		
		public HashCounter(Long nonce, Long startTime, Long endTime) {
			this.nonce = nonce;
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public Long getNonce() {
			return nonce;
		}

		public void setNonce(Long nonce) {
			this.nonce = nonce;
		}

		public Long getStartTime() {
			return startTime;
		}

		public void setStartTime(Long startTime) {
			this.startTime = startTime;
		}

		public Long getEndTime() {
			return endTime;
		}

		public void setEndTime(Long endTime) {
			this.endTime = endTime;
		}

	}
	
}
