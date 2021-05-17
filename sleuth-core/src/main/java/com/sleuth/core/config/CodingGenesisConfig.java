package com.sleuth.core.config;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.subsidy.AllotRule;
import com.sleuth.block.transaction.MerkleBuilder;
import com.sleuth.core.utils.Base64Util;
import com.sleuth.core.utils.DateUtils;

/** 硬编码方式初始化创世区块
 * 
 * @author Jonse
 * @date 2021年5月15日
 */
public class CodingGenesisConfig extends Genesis {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static Genesis instance;
	
	public static Genesis getInstance() {
		if (instance == null) {
			instance = new CodingGenesisConfig();
		}
		return instance;
	}
	
	private Block block;
	private WebUri[] webUris;
	private WxMerkle wxMerkle;
	private TxMerkle txMerkle;
	private Transaction[] transactions;
	
	
	static final byte[] PUBLIC_KEY_HASH = Base64Util.toBinary("gtGQNQVlDzu3IiOmSVN40WWgE1o=");
	
	private CodingGenesisConfig() {
		
		logger.warn("Using hard coding to create Genesis block.");
		
		this.block = new Block();
		this.block.setPrevBlockHash(Genesis.ZERO_PARENT_HASH);
		this.block.setHash(Genesis.ZERO_BLOCK_HASH);
		this.block.setHeight(Genesis.ZERO_HEIGHT);
		
		this.block.setBitsRatio(Genesis.ZERO_BITS_RATIO);
		this.block.setTarget(Genesis.ZERO_TARGET);
		this.block.setNonce(Genesis.ZERO_BLOCK_NONCE);
		
		this.block.setStartTime(Genesis.ZERO_START_TIME);
		this.block.setEndTime(Genesis.ZERO_END_TIME);
		
		this.block.setWxRoot(Genesis.ZERO_WX_MERKLE_ROOT);
		this.block.setTxRoot(Genesis.ZERO_TX_MERKLE_ROOT);
		
		this.block.setFees(Genesis.ZERO_MINER_FEES);
		this.block.setData(Genesis.ZERO_BLOCK_DATA);
		this.block.setCreateAt(Genesis.ZERO_CREATE_AT);
		
		this.webUris = this.genesisWebUris();
		this.transactions = this.genesisCoinbaseTX(webUris);
		this.wxMerkle = this.createByWxMerkle(webUris);
		this.txMerkle = this.createByTxMerkle(transactions);
	}

	@Override
	public Block genesisBlock() {
		return this.block;
	}
	
	@Override
	public WebUri[] getWebURIs() {
		return this.webUris;
	}
	
	@Override
	public Transaction[] getTransactions() {
		return this.transactions;
	}
	
	@Override
	public WxMerkle getWxMerkle() {
		return this.wxMerkle;
	}
	
	@Override
	public TxMerkle getTxMerkle() {
		return this.txMerkle;
	}
	
	/** 创世挖矿
	 * 
	 * @return
	 */
	protected WebUri[] genesisWebUris() {
		WebUri webUri = new WebUri();
		webUri.setDomain("google.com");
		webUri.setUri("https://www.google.com/");
		webUri.setTitle("Google");
		webUri.setCreateAt(DateUtils.nowToUtc());
		webUri.setPubKeyHash(PUBLIC_KEY_HASH);
		webUri.setWxId(webUri.toWxId());
		return new WebUri[]{ webUri };
	}
	
	/** 创世币
	 * 
	 * @param webUris
	 * @return
	 */
	protected Transaction[] genesisCoinbaseTX(WebUri[] webUris) {
		final AllotRule allotRule = new AllotRule();
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
		return transactions;
	}
	
	/** 创建默克尔树
	 * 
	 * @param webUris
	 * @return
	 */
	protected WxMerkle createByWxMerkle(WebUri[] webUris) {
		int length = webUris.length;
        byte[][] atIdArrays = new byte[length][];
        for (int i = 0; i < webUris.length; i++) {
        	atIdArrays[i] = webUris[i].getWxId().getBytes();
        }
        MerkleBuilder merkleBuilder = new MerkleBuilder(atIdArrays);
        byte[] hashBytes = merkleBuilder.getRoot().getHash();
        String merkleRoot = DigestUtils.sha256Hex(hashBytes);
        return WxMerkle.newMerkle(merkleRoot, webUris);
	}
	
	/** 创建默克尔树
	 * 
	 * @param transactions
	 * @return
	 */
	protected TxMerkle createByTxMerkle(Transaction[] transactions) {
		int length = transactions.length;
        byte[][] atIdArrays = new byte[length][];
        for (int i = 0; i < transactions.length; i++) {
        	atIdArrays[i] = transactions[i].getTxId().getBytes();
        }
        MerkleBuilder merkleBuilder = new MerkleBuilder(atIdArrays);
        byte[] hashBytes = merkleBuilder.getRoot().getHash();
        String merkleRoot = DigestUtils.sha256Hex(hashBytes);
        return TxMerkle.newMerkle(merkleRoot, transactions);
	}

}
