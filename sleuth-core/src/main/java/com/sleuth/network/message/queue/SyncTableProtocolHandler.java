package com.sleuth.network.message.queue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sleuth.block.BlockChainManager;
import com.sleuth.block.dto.BlockDTO;
import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.transaction.TransactionManager;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.weburi.WebUriManager;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.network.message.TableProtocolConsumer;
import com.sleuth.network.message.TableProtocolHandler;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;

/** 同步数据的消息具体执行者
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
@Component("syncTableProtocolHandler")
public class SyncTableProtocolHandler implements TableProtocolHandler {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private BlockChainManager blockChainManager;
	@Resource
	private TxMerkleManager txMerkleManager;
	@Resource
	private WxMerkleManager wxMerkleManager;
	@Resource
	private WebUriManager webUriManager;
	@Resource
	private TransactionManager transactionManager;
	
	@Override
	public void handle(TableProtocolConsumer tableConsumer, TabProtocol message) {
		String ch = message.getCh();
		//区块同步
		if (CmdType.BLOCK_SYNC.getCh().equals(ch)) {
			BlockDTO dto = JSON.parseObject(message.getData(), BlockDTO.class);
			this.blockChainManager.pullResult(dto);
		}
		//Wx默克尔树同步
		else if (CmdType.WX_MERKLE_SYNC.getCh().equals(ch)) {
			WxMerkleDTO dto = JSON.parseObject(message.getData(), WxMerkleDTO.class);
			this.wxMerkleManager.pullResult(dto);
		}
		//Tx默克尔树同步
		else if (CmdType.TX_MERKLE_SYNC.getCh().equals(ch)) {
			TxMerkleDTO dto = JSON.parseObject(message.getData(), TxMerkleDTO.class);
			this.txMerkleManager.pullResult(dto);
		}
		//weburi同步
		else if (CmdType.WEBURI_SYNC.getCh().equals(ch)) {
			WebUriDTO dto = JSON.parseObject(message.getData(), WebUriDTO.class);
			this.webUriManager.pullResult(dto);
		}
		//交易数据同步
		else if (CmdType.TRANSACTION_SYNC.getCh().equals(ch)) {
			TransactionDTO dto = JSON.parseObject(message.getData(), TransactionDTO.class);
			this.transactionManager.pullResult(dto);
		}
	}
	
}
