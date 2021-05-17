package com.sleuth.block.service;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sleuth.block.BlockVerify;
import com.sleuth.block.dto.BlockDTO;
import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.core.storage.serialize.Serializer;
import com.sleuth.core.storage.serialize.protostuff.ProtostuffSerializer;
import com.sleuth.core.utils.CopyUtil;
import com.sleuth.core.utils.DateUtils;

@Service
public class BlockVerifyImpl implements BlockVerify {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 区块大小，最大不超过8M */
	final static int MAX_BLOCK_SIZE = 8388608;
	
	final Serializer<Block> serializer = ProtostuffSerializer.of(new Block().cachedSchema());
	
	@Resource
	private TxMerkleManager txMerkleManager;
	@Resource
	private WxMerkleManager wxMerkleManager;
	
	@Override
	public boolean verify(BlockDTO block) {
		if(!this.verifySerializerSize(block)) {
			return false;
		}
		if(!this.verifyWorkNonce(block)) {
			return false;
		}
		if(!this.verifyTime(block)) {
			return false;
		}
		if(!this.verifyTxMerkle(block)) {
			return false;
		}
		if(!this.verifyWxMerkle(block)) {
			return false;
		}
		return true;
	}
	
	/** 验证区块的序列化大小
	 * 
	 * @return
	 */
	private boolean verifySerializerSize(BlockDTO block) {
		Block block2 = CopyUtil.copyProperty(block, Block.class);
		byte[] bytes = serializer.serialize(block2);
		int size = bytes.length;
		if (size <= MAX_BLOCK_SIZE) {
			return true;
		}
		logger.warn("size limits failed, hash={}", block.getHash());
		return false;
	}
	
	/** 验证区块的工作量证明
	 * 
	 * @param block
	 * @return
	 */
	private boolean verifyWorkNonce(BlockDTO block) {
		String shaHex = block.getHash();
		if (new BigInteger(shaHex, 16).compareTo(block.getTarget()) == -1 && shaHex.startsWith("0000")) {
			return true;
		}
		logger.warn("proof of work failed, hash={}", block.getHash());
		return false;
	}
	
	/** 区块的时间戳检测
	 * 
	 * 区块的生成时间必须小于或者等于未来2个小时时间
	 * 
	 * @param block
	 * @return
	 */
	private boolean verifyTime(BlockDTO block) {
		Long blockTime = block.getCreateAt();
		Long localTime = DateUtils.nowToUtc() + 2 * 3600 * 1000;
		if (blockTime <= localTime) {
			return true;
		}
		logger.warn("block timestamp too far in the future, hash={}", block.getHash());
		return false;
	}
	
	/** 检测默克尔树
	 * 
	 * @param block
	 * @return
	 */
	private boolean verifyTxMerkle(BlockDTO block) {
		TxMerkle txMerkle = txMerkleManager.get(block.getTxRoot());
		TxMerkleDTO dto = CopyUtil.copyProperty(txMerkle, TxMerkleDTO.class);
		return txMerkleManager.verify(dto);
	}
	
	/** 检测默克尔树
	 * 
	 * @param block
	 * @return
	 */
	private boolean verifyWxMerkle(BlockDTO block) {
		WxMerkle wxMerkle = wxMerkleManager.get(block.getWxRoot());
		WxMerkleDTO dto = CopyUtil.copyProperty(wxMerkle, WxMerkleDTO.class);
		return wxMerkleManager.verify(dto);
	}
	
}
