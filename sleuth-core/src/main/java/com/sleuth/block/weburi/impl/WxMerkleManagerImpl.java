package com.sleuth.block.weburi.impl;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.transaction.MerkleBuilder;
import com.sleuth.block.transaction.MerkleVerify;
import com.sleuth.block.weburi.WxMerkleBuffer;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.block.weburi.WxMerkleService;
import com.sleuth.core.utils.CopyUtil;

@Service
public class WxMerkleManagerImpl implements WxMerkleManager {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 交易hash值组一次最大广播长度  */
	static final int MAX_BROADCAST_SIZE = 128;
	
	@Resource
	private MerkleVerify merkleVerify;
	@Resource
	private WxMerkleService merkleService;
	@Resource
	private WxMerkleBuffer merkleBuffer;

	@Override
	public String builder(WebUri[] webUris) {
		if (webUris == null) {
			webUris = new WebUri[]{};
		}
		int length = webUris.length;
        byte[][] atIdArrays = new byte[length][];
        for (int i = 0; i < webUris.length; i++) {
        	atIdArrays[i] = webUris[i].getWxId().getBytes();
        }
        MerkleBuilder merkleBuilder = new MerkleBuilder(atIdArrays);
        byte[] hashBytes = merkleBuilder.getRoot().getHash();
        return DigestUtils.sha256Hex(hashBytes);
	}

	@Override
	public boolean verify(WxMerkleDTO merkle) {
		if (merkle != null) {
			return merkleVerify.verify(merkle);
		}
		return false;
	}

	@Override
	public void addResult(WxMerkle merkle) {
		//保存默克尔树
		this.merkleService.add(merkle);
		//将默克尔树广播出去
		final Timer timer = new Timer();
		timer.schedule(new PushMerkle(timer, merkle), 0);
	}

	@Override
	public void pushResult(WxMerkleDTO data) {
		WxMerkleDTO dto = this.merkleBuffer.recvfrom(data);
		//不为空，则接收完成
		if (dto != null) {
			//保存默克尔树
			WxMerkle merkle = CopyUtil.copyProperty(dto, WxMerkle.class);
			this.merkleService.add(merkle);
			//将默克尔树广播出去
			final Timer timer = new Timer();
			timer.schedule(new PushMerkle(timer, merkle), 0);
		}
	}

	@Override
	public void pullResult(WxMerkleDTO data) {
		WxMerkleDTO dto = this.merkleBuffer.recvfrom(data);
		//不为空，则接收完成
		if (dto != null) {
			//保存默克尔树
			WxMerkle merkle = CopyUtil.copyProperty(dto, WxMerkle.class);
			this.merkleService.add(merkle);
			//删除缓存中的数据
			this.merkleBuffer.remove(merkle.getHash());
		}
	}
	
	@Override
	public WxMerkle get(String merkleRoot) {
		return this.merkleService.get(merkleRoot);
	}
	
	/** 推送默克尔树
	 * 
	 * @author Jonse
	 * @date 2021年3月23日
	 */
	class PushMerkle extends TimerTask {
		
		private WxMerkle merkle;
		private Timer timer;
		
		public PushMerkle(Timer timer, WxMerkle merkle) {
			this.timer = timer;
			this.merkle = merkle;
		}

		@Override
		public void run() {
			WxMerkleDTO[] merkles = this.group(this.merkle);
			for (WxMerkleDTO e : merkles) {
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
		private WxMerkleDTO[] group(WxMerkle merkle) {
			WxMerkleDTO[] merkles = {};
			String[] txIds = merkle.getWxIds();
			if (txIds != null && txIds.length > 0) {
				int size = txIds.length;
				if (size > MAX_BROADCAST_SIZE) {
					for(int i=0; i<size-MAX_BROADCAST_SIZE; i+=MAX_BROADCAST_SIZE) {
						String[] items = ArrayUtils.subarray(txIds, i, i+MAX_BROADCAST_SIZE);
						WxMerkleDTO e = new WxMerkleDTO();
						e.setWxIds(items);
						e.setHash(merkle.getHash());
						e.setCreateAt(merkle.getCreateAt());
						e.setHasNext(false);
						merkles = ArrayUtils.add(merkles, e);
					}
				}
			} else {
				WxMerkleDTO e = CopyUtil.copyProperty(merkle, WxMerkleDTO.class);
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
