package com.sleuth.network.message.sync;

import java.util.Timer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sleuth.block.BlockChain;
import com.sleuth.block.BlockService;
import com.sleuth.block.service.Iterator;
import com.sleuth.block.transaction.TransactionManager;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.weburi.WebUriManager;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.network.message.CmdAcceptor;
import com.sleuth.network.message.protocol.ApProtocol;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.sync.accept.BlockAcceptorTimer;
import com.sleuth.network.message.sync.accept.TxMerkleAcceptorTimer;
import com.sleuth.network.message.sync.accept.WxMerkleAcceptorTimer;

import io.netty.channel.Channel;

@Component
public class ServerCmdAcceptor implements CmdAcceptor {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final Timer timer = new Timer();
	
	@Resource
	private BlockChain blockChain;
	@Resource
	private BlockService blockService;
	
	@Resource
	private WxMerkleManager wxMerkleManager;
	@Resource
	private WebUriManager webUriManager;
	
	@Resource
	private TxMerkleManager txMerkleManager;
	@Resource
	private TransactionManager transactionManager;

	@Override
	public void onAccept(Channel channel, ApProtocol message) {
		//区块同步
		if (CmdType.BLOCK_SYNC.getCh().equals(message.getCh())) {
			Iterator iterator = new Iterator(this.blockChain, this.blockService);
			this.timer.schedule(new BlockAcceptorTimer(message, channel, iterator), 0);
		}
		//WX默克尔树资源同步
		else if (CmdType.WX_MERKLE_SYNC.getCh().equals(message.getCh())) {
			this.timer.schedule(new WxMerkleAcceptorTimer(message, channel, wxMerkleManager, webUriManager), 0);
		}
		//TX默克尔树资源同步
		else if (CmdType.TX_MERKLE_SYNC.getCh().equals(message.getCh())) {
			this.timer.schedule(new TxMerkleAcceptorTimer(message, channel, txMerkleManager, transactionManager), 0);
		}
		
	}

}
