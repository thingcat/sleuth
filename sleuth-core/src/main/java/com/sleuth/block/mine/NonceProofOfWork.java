package com.sleuth.block.mine;

import java.math.BigInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.exception.MineInterruptException;
import com.sleuth.block.mine.work.PowResult;
import com.sleuth.block.schema.Block;
import com.sleuth.core.utils.ByteUtils;

/** 基于寻找目标值运算的方式生成区块
 * 
 * @author Administrator
 *
 */
public class NonceProofOfWork implements ProofOfWork {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean mining = true;
	private Block newBlock;//区块
	private int bitsRatio;//难度目标值系数
	private BigInteger target;//需要计算的难度目标值
	private String wxRoot;//默克尔树根节点
	private String txRoot;//默克尔树根节点
	
	public NonceProofOfWork(String wxRoot, String txRoot, Block newBlock, int bitsRatio) {
		this.newBlock = newBlock;
        this.bitsRatio = bitsRatio;
        this.target = BigInteger.valueOf(1).shiftLeft((256 - bitsRatio));
        this.wxRoot = wxRoot;
        this.txRoot = txRoot;
	}

	@Override
	public PowResult compute() throws MineInterruptException {
		long nonce = 0;
        String shaHex = "";
        logger.info(">>>> Mining the block target = {}", this.target);
        long startTime = System.currentTimeMillis();
        while (nonce < Long.MAX_VALUE) {
        	//如果收到中断的指令，则直接中断
        	if (!this.mining) {
				throw new MineInterruptException();
			}
        	byte[] data = this.prepareData(nonce);
            shaHex = DigestUtils.sha256Hex(data);
            if (new BigInteger(shaHex, 16).compareTo(this.target) == -1 && checkHashValue(shaHex)) {
            	long endTime = System.currentTimeMillis();
            	//挖矿成功耗费的时间，单位：秒
            	long seconds = (endTime - startTime) / 1000;
            	long nhp = 0;
            	if (seconds > 0) {
            		//算力：100Hash/s，就代表矿机每秒钟可以随机碰撞100次，简写成100H/s。算力 = 总次数/总耗时
                	nhp = nonce / seconds;
				}
            	logger.info("Elapsed Time: {} Seconds, {} Nonce", seconds, nonce);
                logger.info("NHP Value: {} H/s", nhp);
                logger.info(">>>> Correct hash Hex: {} \n \n", shaHex);
                PowResult powResult = new PowResult(this.bitsRatio, this.target, nonce, shaHex);
                powResult.setStartTime(startTime);
                powResult.setEndTime(endTime);
                return powResult;
            } else {
                nonce++;
            }
        }
        return null;
	}
	
	/**
	 * 准备数据
	 * <p>
	 * 注意：在准备区块数据时，一定要从原始数据类型转化为byte[]，不能直接从字符串进行转换
	 * @param nonce
	 * @return
	 */
	private byte[] prepareData(long nonce) {
		byte[] prevBlockHashBytes = {};
		if (StringUtils.isNoneBlank(this.newBlock.getPrevBlockHash())) {
			prevBlockHashBytes = new BigInteger(this.newBlock.getPrevBlockHash(), 16).toByteArray();
		}
		return ByteUtils.merge(
				prevBlockHashBytes,
				this.wxRoot.getBytes(),
				this.txRoot.getBytes(),
				this.newBlock.getData().getBytes(),
				ByteUtils.toBytes(this.newBlock.getCreateAt()), 
				ByteUtils.toBytes(this.bitsRatio),
				ByteUtils.toBytes(nonce));
	}
	
	/**
	 * 验证hash值是否满足系统条件 暂定前4位是0则满足条件
	 * 
	 * @param hash
	 * @return
	 */
	public boolean checkHashValue(String hash) {
		return hash.startsWith("0000");
	}

	@Override
	public void interrupt() {
		this.mining = false;
	}
	
}
