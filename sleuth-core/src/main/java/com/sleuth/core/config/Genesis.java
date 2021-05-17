package com.sleuth.core.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.block.schema.Block;
import com.sleuth.block.schema.Transaction;
import com.sleuth.block.schema.TxMerkle;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.schema.WxMerkle;

public abstract class Genesis {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected static final String GENESIS_CONFIG = "/genesis.json";
	
	protected static final String ZERO_PARENT_HASH = Hex.encodeHexString(new byte[32]);
	public static final String ZERO_BLOCK_HASH = "0000a426b609192f4b8f854807548903583f5c7b25f75721f7b8603fa4b43f30";
	public static final Long ZERO_HEIGHT = 0L;
	
	protected static final Integer ZERO_BITS_RATIO = 10;
	protected static final Long ZERO_BLOCK_NONCE = 141229L;
	protected static final Long ZERO_START_TIME = 1621060328263L;
	protected static final Long ZERO_END_TIME = 1621060329187L;
	protected static final BigInteger ZERO_TARGET = new BigInteger("113078212145816597093331040047546785012958969400039613319782796882727665664");
	
	protected static final String ZERO_TX_MERKLE_ROOT = "a7bf109796df4d6b431a81e21d0d906cb3f822e0e528d0ac71b664f2d5a5ad28";
	protected static final String ZERO_WX_MERKLE_ROOT = "3c1107c6398bc9acca71c6165213bdea47614ce2749e2554cdfec415c8d27abc";
	
	protected static final BigDecimal ZERO_MINER_FEES = new BigDecimal("0.00");
	protected static final String ZERO_BLOCK_DATA = "Genesis Block";
	protected static final Long ZERO_CREATE_AT = 1621031529189L;
	
	protected Path loader() {
		try {
			String webXmlPath = this.getClass().getResource(GENESIS_CONFIG).toString();
			return Paths.get(new URI(webXmlPath));
		} catch (URISyntaxException e) {
			throw new GenesisConfigException();
		}
	}
	
	/** 获取创世区块信息
	 * 
	 * @return
	 */
	public abstract Block genesisBlock();
	
	public abstract WebUri[] getWebURIs();
	
	public abstract Transaction[] getTransactions();
	
	public abstract WxMerkle getWxMerkle();
	
	public abstract TxMerkle getTxMerkle();
	
}
