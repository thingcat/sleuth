package com.sleuth.core;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.exception.MineInterruptException;
import com.sleuth.block.mine.NonceProofOfWork;
import com.sleuth.block.mine.ProofOfWork;
import com.sleuth.block.mine.work.PowResult;
import com.sleuth.block.mine.work.WorkResult;
import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.subsidy.AllotRule;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.transaction.impl.TxMerkleManagerImpl;
import com.sleuth.block.weburi.WebUriPack;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.block.weburi.impl.WxMerkleManagerImpl;
import com.sleuth.core.utils.Base64Util;
import com.sleuth.core.utils.DateUtils;

public class GenesisBlockTest {
	
	static WebUri buildWebUri() {
		WebUri webUri = new WebUri();
		webUri.setDomain("google.com");
		webUri.setUri("https://www.google.com/");
		webUri.setTitle("Google");
		webUri.setCreateAt(DateUtils.nowToUtc());
		webUri.setPubKeyHash(Base64Util.toBinary("gtGQNQVlDzu3IiOmSVN40WWgE1o="));
		webUri.setWxId(webUri.toWxId());
		return webUri;
	}
	
	static WebUriPack buildWebUriPack(WebUri webUri) {
		WebUriPack webUriPack = new WebUriPack();
		final AllotRule allotRule = new AllotRule();
		WebUri[] webUris = new WebUri[]{ webUri };
		Map<String, TxOutput> txOutputs = allotRule.compute(0L, webUris);
		Transaction[] transactions = {};
		for(Map.Entry<String, TxOutput> entry : txOutputs.entrySet()) {
			//锁定脚本，使用公钥hash作为锁定脚本
			byte[] scriptSign = Base64Util.toBinary(entry.getKey());
			TxOutput[] outputs = new TxOutput[]{ entry.getValue() };
			Transaction tx = Transaction.newCoinbaseTX(scriptSign, outputs);
			// 设置交易ID
			tx.setTxId(tx.toTxId());
			transactions = ArrayUtils.add(transactions, tx);
		}
		webUriPack.setTransactions(transactions);
		webUriPack.setWebUris(webUris);
		return webUriPack;
	}
	
	static WorkResult buildBlock(WebUriPack webUriPack) throws MineInterruptException {
		//构建区块的基础信息
		Block newBlock = new Block(Hex.encodeHexString(new byte[32]), 0L, "Genesis Block");
		newBlock.setBitsRatio(10);
		Transaction[] transactions = webUriPack.getTransactions();
		WebUri[] webUris = webUriPack.getWebUris();
		
		WxMerkleManager wxmm = new WxMerkleManagerImpl();
		TxMerkleManager txmm = new TxMerkleManagerImpl();
		
		String wxRoot = wxmm.builder(webUris);
		String txRoot = txmm.builder(transactions);
		
		//创建基于工作量的挖矿对象
		ProofOfWork proofOfWork = new NonceProofOfWork(wxRoot, txRoot, newBlock, newBlock.getBitsRatio());
		//开始挖矿，并输出挖矿结果
		PowResult result = proofOfWork.compute();
		
		newBlock.setWxRoot(wxRoot);
		newBlock.setTxRoot(txRoot);
		
		WorkResult workResult = encaResult(newBlock, webUris, transactions, result);
		
		return workResult;
	}
	
	public static void main(String[] args) throws MineInterruptException {
		//创建WebUri
		WebUri webUri = buildWebUri();
		//打包
		WebUriPack webUriPack = buildWebUriPack(webUri);
		
		WorkResult workResult = buildBlock(webUriPack);
		
		Block block = workResult.getBlock();
		
		System.out.println(JSON.toJSONString(webUri));
		System.out.println(JSON.toJSONString(webUriPack.getTransactions()));
		System.out.println(JSON.toJSONString(block));
		
	}
	
	/** 封装结果
	 * 
	 * @param tappHash
	 * @param actions
	 * @param result
	 * @return
	 */
	static WorkResult encaResult(Block newBlock, WebUri[] webUris, Transaction[] transactions, PowResult result) {
		newBlock.setHash(result.getHash());
		newBlock.setTarget(result.getTarget());
		newBlock.setNonce(result.getNonce());
		newBlock.setBitsRatio(result.getBitsRatio());
//		newBlock.setTxRoot(result.getTxRoot());
//		newBlock.setWxRoot(result.getWxRoot());
		newBlock.setStartTime(result.getStartTime());
		newBlock.setEndTime(result.getEndTime());
		newBlock.setCreateAt(DateUtils.nowToUtc());
		newBlock.setFees(new BigDecimal("0.00"));
		
		WorkResult workResult = new WorkResult();
		workResult.setWxRoot(result.getWxRoot());
		workResult.setTxRoot(result.getTxRoot());
		workResult.setWebURIs(webUris);
		workResult.setTransactions(transactions);
		workResult.setPowResult(result);
		workResult.setBlock(newBlock);
		return workResult;
	}

}
