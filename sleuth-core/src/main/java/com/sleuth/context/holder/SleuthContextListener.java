package com.sleuth.context.holder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.BlockBuffer;
import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockGenesis;
import com.sleuth.block.transaction.TransactionBuffer;
import com.sleuth.block.transaction.UTXOBuffer;
import com.sleuth.block.weburi.WebUriBuffer;
import com.sleuth.block.weburi.WebUriPool;
import com.sleuth.context.ContextListener;
import com.sleuth.network.PeerService;
import com.sleuth.oauth.service.KeyPairService;

public class SleuthContextListener implements ContextListener {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private KeyPairService keyPairService;
	@Resource
	private PeerService peerService;
	@Resource
	private UTXOBuffer utxoBuffer;
	
	
	@Resource
	private BlockBuffer blockBuffer;
	@Resource
	private BlockChain blockChain;
	@Resource
	private BlockGenesis blockGenesis;
	@Resource
	private WebUriPool webUriPool;
	@Resource
	private WebUriBuffer webUriBuffer;
	@Resource
	private TransactionBuffer transactionBuffer;
	
	@Override
	@PostConstruct
	public void onPreloading() {
		logger.info("================start preloading================");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		//链路结构预加载
		this.blockChain.onPreloading();
		logger.info("Block Chain Preloading==========>>>>>>>>>");
		
		//创世区块预加载
		this.blockGenesis.onPreloading();
		logger.info("Block Genesis Preloading==========>>>>>>>>>");
		
		//区块缓存池预加载
		this.blockBuffer.onPreloading();
		logger.info("Block Buffer Preloading==========>>>>>>>>>");
		
		//交易缓冲预加载
		this.transactionBuffer.onPreloading();
		logger.info("Transaction Buffer Preloading==========>>>>>>>>>");
		
		//WebURI确认池预加载
		this.webUriPool.onPreloading();
		logger.info("WebUri Pool Preloading==========>>>>>>>>>");
		
		//WebURI缓冲池预加载
		this.webUriBuffer.onPreloading();
		logger.info("WebUri Buffer Preloading==========>>>>>>>>>");
	}
	
	@Override
	public void onBeforeHandle() {
		//1、先创建身份信息
		this.keyPairService.creater();
		//2、从主网下载节点地址
		this.peerService.download();
		//3、初始化P2P网络模型
		this.peerService.start();
		//4、加载UTXO池
		this.utxoBuffer.init();
	}

	@Override
	public void onAfterHandle() {
		peerService.destroy();
	}

}
