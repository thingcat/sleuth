package com.sleuth.block.transaction.impl;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.store.TxMerkleStore;
import com.sleuth.block.transaction.MerkleBuilder;
import com.sleuth.block.transaction.MerkleVerify;
import com.sleuth.core.utils.DateUtils;

@Service
public class MerkleVerifyImpl implements MerkleVerify {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TxMerkleStore store;
	
	@Override
	public boolean verify(TxMerkleDTO merkle) {
		//验证时间
		if (!verifyTime(merkle)) {
			logger.warn("time verify failed. hash = {}", merkle.getHash());
			return false;
		}
		//验证下面的事件
		if (!verifyRootHash(merkle)) {
			logger.warn("TxMerkle verify failed. hash = {}", merkle.getHash());
			return false;
		}
		return true;
	}
	
	@Override
	public boolean verify(WxMerkleDTO merkle) {
		//验证时间
		if (!verifyTime(merkle)) {
			logger.warn("time verify failed. hash = {}", merkle.getHash());
			return false;
		}
		//验证下面的事件
		if (!verifyRootHash(merkle)) {
			logger.warn("WxMerkle verify failed. hash = {}", merkle.getHash());
			return false;
		}
		return true;
	}
	
	
	/** 区块的时间戳检测
	 * 区块的生成时间必须小于或者等于未来2个小时时间
	 * @param block
	 * @return
	 */
	private boolean verifyTime(TxMerkleDTO merkle) {
		Long blockTime = merkle.getCreateAt();
		Long localTime = DateUtils.nowToUtc() + 2 * 3600 * 1000;
		if (blockTime <= localTime) {
			return true;
		}
		logger.warn("merkle timestamp too far in the future, hash={}", merkle.getHash());
		return false;
	}
	
	/** 区块的时间戳检测
	 * 区块的生成时间必须小于或者等于未来2个小时时间
	 * @param block
	 * @return
	 */
	private boolean verifyTime(WxMerkleDTO merkle) {
		Long blockTime = merkle.getCreateAt();
		Long localTime = DateUtils.nowToUtc() + 2 * 3600 * 1000;
		if (blockTime <= localTime) {
			return true;
		}
		logger.warn("merkle timestamp too far in the future, hash={}", merkle.getHash());
		return false;
	}
	
	/** 验证默克尔树根节点
	 * 
	 * @param merkle
	 * @return
	 */
	private boolean verifyRootHash(TxMerkleDTO merkle) {
		String[] txIds = merkle.getTxIds();
		int length = txIds.length;
        byte[][] atIdArrays = new byte[length][];
        for (int i = 0; i < txIds.length; i++) {
        	atIdArrays[i] = txIds[i].getBytes();
        }
        MerkleBuilder merkleBuilder = new MerkleBuilder(atIdArrays);
        byte[] bytes = merkleBuilder.getRoot().getHash();
        String rootHash = DigestUtils.sha256Hex(bytes);
        return rootHash.equals(merkle.getHash());
	}
	
	/** 验证默克尔树根节点
	 * 
	 * @param merkle
	 * @return
	 */
	private boolean verifyRootHash(WxMerkleDTO merkle) {
		String[] wxIds = merkle.getWxIds();
		int length = wxIds.length;
        byte[][] atIdArrays = new byte[length][];
        for (int i = 0; i < wxIds.length; i++) {
        	atIdArrays[i] = wxIds[i].getBytes();
        }
        MerkleBuilder merkleBuilder = new MerkleBuilder(atIdArrays);
        byte[] bytes = merkleBuilder.getRoot().getHash();
        String rootHash = DigestUtils.sha256Hex(bytes);
        return rootHash.equals(merkle.getHash());
	}
	
}
