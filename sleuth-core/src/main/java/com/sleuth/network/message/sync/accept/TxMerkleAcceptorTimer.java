package com.sleuth.network.message.sync.accept;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.block.dto.TransactionDTO;
import com.sleuth.block.dto.TxMerkleDTO;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.transaction.TransactionManager;
import com.sleuth.block.transaction.TxMerkleManager;
import com.sleuth.block.transaction.TxUtils;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.core.utils.CopyUtil;
import com.sleuth.network.message.protocol.ApProtocol;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;

import io.netty.channel.Channel;

/** 默克尔树申请受理
 * 
 * @author Jonse
 * @date 2021年5月15日
 */
public class TxMerkleAcceptorTimer extends TimerTask {

	static final int MAX_BROADCAST_SIZE = 128;
	static final CmdType ch = CmdType.WX_MERKLE_SYNC;
	
	private Channel channel;
	private String merkleRoot;
	private TxMerkleManager merkleManager;
	private TransactionManager transactionManager;
	
	public TxMerkleAcceptorTimer(ApProtocol message, Channel channel, 
			TxMerkleManager merkleManager, TransactionManager transactionManager) {
		this.channel = channel;
		JSONObject data = JSON.parseObject(message.getArgs());
		this.merkleRoot = data.getString("txRoot");
		
		this.merkleManager = merkleManager;
		this.transactionManager = transactionManager;
	}

	@Override
	public void run() {
		TxMerkle merkle = this.merkleManager.get(this.merkleRoot);
		if (merkle != null) {
			TxMerkleDTO[] merkles = this.group(merkle);
			for (TxMerkleDTO e : merkles) {
				TabProtocol table = TabProtocol.newProtocol(ch);
				table.setData(JSON.toJSONString(e));
				CLI.push(this.channel, table);
				try {
					TimeUnit.MILLISECONDS.sleep(100);//暂停100毫秒
				} catch (InterruptedException ex) {
				}
			}
			
			//发送完成后，发送交易数据
			for (String txId : merkle.getTxIds()) {
				Transaction tx = this.transactionManager.findById(txId);
				if (tx != null) {
					TransactionDTO e = TxUtils.toDTO(tx);
					TabProtocol table = TabProtocol.newProtocol(CmdType.TRANSACTION_SYNC);
					table.setData(JSON.toJSONString(e));
					CLI.push(this.channel, table);
					try {
						TimeUnit.MILLISECONDS.sleep(100);//暂停100毫秒
					} catch (InterruptedException ex) {
					}
				}
			}
		}
	}
	
	/** 分组默克尔树
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
			} else {
				TxMerkleDTO e = CopyUtil.copyProperty(merkle, TxMerkleDTO.class);
				merkles = ArrayUtils.add(merkles, e);
			}
		}
		if (merkles.length > 0) {
			int i = merkles.length - 1;
			merkles[i].setHasNext(true);
		}
		return merkles;
	}

}
