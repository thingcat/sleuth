package com.sleuth.block.mine.work;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.BlockChainManager;
import com.sleuth.block.exception.MineInterruptException;
import com.sleuth.block.mine.NonceProofOfWork;
import com.sleuth.block.mine.ProofOfWork;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.transaction.TransactionManager;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.weburi.WebUriManager;
import com.sleuth.block.weburi.WebUriPack;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.core.utils.DateUtils;

/** 挖矿线程
 * 
 * @author Jonse
 * @date 2019年6月10日
 */
public class PowRunner implements Runnable {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean interrupted = false;
	
	private ProofOfWork proofOfWork;
	
	private Block newBlock;
	
	private final Block block;
	private final WebUriManager webUriManager;
	private final TransactionManager txManager;
	private final BlockChainManager blockChainManager;
	
	private final TxMerkleManager txMerkleManager;
	private final WxMerkleManager wxMerkleManager;
	
	public PowRunner(Block block, WebUriManager webUriManager, TransactionManager txManager, 
			BlockChainManager blockChainManager, TxMerkleManager txMerkleManager, WxMerkleManager wxMerkleManager) {
		this.block = block;
		//构建区块的基础信息
		this.newBlock = new Block(this.block.getHash(), this.block.getHeight() + 1);
		this.webUriManager = webUriManager;
		this.txManager = txManager;
		this.blockChainManager = blockChainManager;
		this.txMerkleManager = txMerkleManager;
		this.wxMerkleManager = wxMerkleManager;
	}
	
	@Override
	public void run() {
		
		logger.warn("start the mining work thread successfully!!!");
		
		while(!this.interrupted) {
			try {
				/*挖矿前的数据准备：
				  1、打包转账交易数据
				  2、打包WebUri生成的创币数据
				*/
				Transaction[] transferTX = this.txManager.baleBufferTransactions();
				WebUriPack webUriPack = this.webUriManager.baleBufferWebUri(this.newBlock.getHeight());
				Transaction[] coinbaseTX = webUriPack.getTransactions();
				//合并交易单
				Transaction[] transactions = {};
				if (coinbaseTX != null && coinbaseTX.length > 0) {
					transactions = ArrayUtils.addAll(transactions, coinbaseTX);
				}
				if (transferTX != null && transferTX.length > 0) {
					transactions = ArrayUtils.addAll(transactions, transferTX);
				}
				
				WebUri[] webUris = webUriPack.getWebUris();
				
				if (transactions != null && transactions.length > 0) {
					//开始工作
					PowResult result = this.doWork(webUris, transactions);
					//组装区块信息
					WorkResult workResult = this.encaResult(webUris, transactions, result);
					//完成挖矿
					logger.warn("a new block is created!!!");
					logger.warn("block = {}", this.newBlock.toString());
					//保存工作结果
					this.blockChainManager.addResult(workResult);
					//退出
					break;
				}
			} catch (Exception e) {
				//中断挖矿行为，并退出
				if (e instanceof MineInterruptException) {
					logger.warn(e.getMessage());
					break;
				}
				logger.error("This time mining is a failure, the case: {}", e.getMessage());
				logger.error("MINE EROOR: ", e);
			}						
		}
		
	}
	
	/** 执行挖矿
	 * 
	 * @param bitsRatio
	 * @param transactions
	 * @return
	 * @throws MineInterruptException
	 */
	private PowResult doWork(WebUri[] webUris, Transaction[] transactions) throws MineInterruptException {
		//构建网址的默克尔树根节点
		String wxRoot = this.wxMerkleManager.builder(webUris);
		//构建交易的默克尔树根节点
		String txRoot = this.txMerkleManager.builder(transactions);
		//创建基于工作量的挖矿对象
		this.proofOfWork = new NonceProofOfWork(wxRoot, txRoot, this.newBlock, this.newBlock.getBitsRatio());
		//开始挖矿，并输出挖矿结果
		PowResult result = this.proofOfWork.compute();
		//将默克尔树放入结果集
		result.setWxRoot(wxRoot);
		result.setTxRoot(txRoot);
		//返回挖矿结果
		return result;
	}
	
	/** 封装结果
	 * 
	 * @param tappHash
	 * @param actions
	 * @param result
	 * @return
	 */
	private WorkResult encaResult(WebUri[] webUris, Transaction[] transactions, PowResult result) {
		this.newBlock.setHash(result.getHash());
		this.newBlock.setTarget(result.getTarget());
		this.newBlock.setNonce(result.getNonce());
		this.newBlock.setBitsRatio(result.getBitsRatio());
		this.newBlock.setTxRoot(result.getTxRoot());
		this.newBlock.setWxRoot(result.getWxRoot());
		this.newBlock.setStartTime(result.getStartTime());
		this.newBlock.setEndTime(result.getEndTime());
		this.newBlock.setCreateAt(DateUtils.nowToUtc());
		
		WorkResult workResult = new WorkResult();
		workResult.setWxRoot(result.getWxRoot());
		workResult.setTxRoot(result.getTxRoot());
		workResult.setWebURIs(webUris);
		workResult.setTransactions(transactions);
		workResult.setPowResult(result);
		workResult.setBlock(this.newBlock);
		return workResult;
	}
	
	/** 中断线程
	 * 
	 */
	public void interrupt() {
		if (this.proofOfWork != null) {
			this.proofOfWork.interrupt();
		}
		this.interrupted = true;
	}
	
}
