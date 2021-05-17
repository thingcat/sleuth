package com.sleuth.block.transaction.impl;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.transaction.TxMerkleBuffer;
import com.sleuth.block.transaction.MerkleBuilder;
import com.sleuth.block.transaction.MerkleVerify;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.transaction.TxMerkleService;
import com.sleuth.core.utils.CopyUtil;

@Service
public class TxMerkleManagerImpl implements TxMerkleManager {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 交易hash值组一次最大广播长度  */
	static final int MAX_BROADCAST_SIZE = 128;
	
	@Resource
	private MerkleVerify merkleVerify;
	@Resource
	private TxMerkleService merkleService;
	@Resource
	private TxMerkleBuffer merkleBuffer;

	@Override
	public String builder(Transaction[] transactions) {
		if (transactions == null) {
			transactions = new Transaction[]{};
		}
		int length = transactions.length;
        byte[][] atIdArrays = new byte[length][];
        for (int i = 0; i < transactions.length; i++) {
        	atIdArrays[i] = transactions[i].getTxId().getBytes();
        }
        MerkleBuilder merkleBuilder = new MerkleBuilder(atIdArrays);
        byte[] hashBytes = merkleBuilder.getRoot().getHash();
        return DigestUtils.sha256Hex(hashBytes);
	}

	@Override
	public boolean verify(TxMerkleDTO merkle) {
		if (merkle != null) {
			return merkleVerify.verify(merkle);
		}
		return false;
	}

	@Override
	public void addResult(TxMerkle merkle) {
		//保存默克尔树
		this.merkleService.add(merkle);
		//将默克尔树广播出去
		final Timer timer = new Timer();
		timer.schedule(new PushMerkle(timer, merkle), 0);
	}

	@Override
	public void pushResult(TxMerkleDTO data) {
		TxMerkleDTO dto = this.merkleBuffer.recvfrom(data);
		//不为空，则接收完成
		if (dto != null) {
			//保存默克尔树
			TxMerkle merkle = CopyUtil.copyProperty(dto, TxMerkle.class);
			this.merkleService.add(merkle);
			//将默克尔树广播出去
			final Timer timer = new Timer();
			timer.schedule(new PushMerkle(timer, merkle), 0);
		}
	}

	@Override
	public void pullResult(TxMerkleDTO data) {
		TxMerkleDTO dto = this.merkleBuffer.recvfrom(data);
		//不为空，则接收完成
		if (dto != null) {
			//保存默克尔树
			TxMerkle merkle = CopyUtil.copyProperty(dto, TxMerkle.class);
			this.merkleService.add(merkle);
			//删除缓存中的数据
			this.merkleBuffer.remove(merkle.getHash());
		}
	}
	
	@Override
	public TxMerkle get(String merkleHash) {
		return this.merkleService.get(merkleHash);
	}
	
	/** 推送默克尔树
	 * 
	 * @author Jonse
	 * @date 2021年3月23日
	 */
	class PushMerkle extends TimerTask {
		
		private TxMerkle merkle;
		private Timer timer;
		
		public PushMerkle(Timer timer, TxMerkle merkle) {
			this.timer = timer;
			this.merkle = merkle;
		}

		@Override
		public void run() {
			TxMerkleDTO[] merkles = this.group(this.merkle);
			for (TxMerkleDTO e : merkles) {
				merkleService.doRecvFrom(e);
				try {
					TimeUnit.MILLISECONDS.sleep(100);//暂停100毫秒
				} catch (InterruptedException ex) {
				}
			}
			//删除缓存中的数据
			merkleBuffer.remove(this.merkle.getHash());
			this.timer.cancel();
		}
		
		/** 广播默克尔树
		 * 
		 * @param merkle
		 */
		private TxMerkleDTO[] group(TxMerkle merkle) {
			TxMerkleDTO[] merkles = {};
			String[] txIds = merkle.getTxIds();
			if (txIds != null && txIds.length > 0) {
				int size = txIds.length;
				if (size > MAX_BROADCAST_SIZE) {
					for(int i=0; i<size-MAX_BROADCAST_SIZE; i+=MAX_BROADCAST_SIZE) {
						String[] items = ArrayUtils.subarray(txIds, i, i+MAX_BROADCAST_SIZE);
						TxMerkleDTO e = new TxMerkleDTO();
						e.setTxIds(items);
						e.setHash(merkle.getHash());
						e.setCreateAt(merkle.getCreateAt());
						e.setHasNext(false);
						merkles = ArrayUtils.add(merkles, e);
					}
				}
			} else {
				TxMerkleDTO e = CopyUtil.copyProperty(merkle, TxMerkleDTO.class);
				merkles = ArrayUtils.add(merkles, e);
			}
			
			if (merkles.length > 0) {
				int i = merkles.length - 1;
				merkles[i].setHasNext(true);
			}
			
			return merkles;
		}
	}
	
}
