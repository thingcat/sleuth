package com.sleuth.block.subsidy;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.schema.WebUri;
import com.sleuth.core.utils.Base64Util;

/** 奖励分配规则
 * 
 * <p>1、1-3名获得50%奖励</p>
 * <p>2、4-20名获得30%奖励</p>
 * <p>剩下的获得20%奖励</p>
 * 
 * @author Jonse
 * @date 2021年5月13日
 */
public class AllotRule extends SubsidyRule {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	static final BigDecimal ZERO = new BigDecimal("0");
	
	static final BigDecimal ratio_60 = new BigDecimal("0.6");
	static final BigDecimal ratio_40 = new BigDecimal("0.4");
	
	//50%
	static final BigDecimal ratio_50 = new BigDecimal("0.5");
	//30%
	static final BigDecimal ratio_30 = new BigDecimal("0.3");
	//20%
	static final BigDecimal ratio_20 = new BigDecimal("0.2");
	
	@Override
	public Map<String, TxOutput> compute(Long height, WebUri[] webUris) {
		//获得挖矿奖励数量 
		BigDecimal subsidyAmount = this.subsidyAmount(height);
		//是否还存在奖励
		if (subsidyAmount.compareTo(ZERO) > 0) {
			//分组
			Map<String, WebUri[]> groupResult = this.groupPubKeyHash(webUris);
			int kLen = groupResult.size();
			//全吃
			if (kLen <= 3) {
				return this.allotAll(groupResult, subsidyAmount);
			}
			
			//将MAP转换为LIST对象
			List<PKHWebUri> pkhWebUris = this.convert(groupResult);
			//根据WebUri[]的数量，从大到小进行排序
			pkhWebUris.sort(new Comparator<PKHWebUri>() {
				@Override
				public int compare(PKHWebUri o1, PKHWebUri o2) {
					return o2.getLength() > o1.getLength() ? 1 : 0;
				}
			});
			
			Map<String, TxOutput> txOutputs = Maps.newHashMap();
			
			if (kLen <= 20) {
				//前3
				Map<String, TxOutput> map1 = this.allotForRatio(pkhWebUris.subList(0, 3), 
						subsidyAmount.multiply(ratio_60));
				txOutputs.putAll(map1);
				//前20
				Map<String, TxOutput> map2 = this.allotForRatio(pkhWebUris.subList(3, pkhWebUris.size()-1), 
						subsidyAmount.multiply(ratio_40));
				txOutputs.putAll(map2);
									
			} else {
				//前3
				Map<String, TxOutput> map1 = this.allotForRatio(pkhWebUris.subList(0, 3), 
						subsidyAmount.multiply(ratio_50));
				txOutputs.putAll(map1);
				
				//前20
				Map<String, TxOutput> map2 = this.allotForRatio(pkhWebUris.subList(3, 20), 
						subsidyAmount.multiply(ratio_30));
				txOutputs.putAll(map2);
				
				Map<String, TxOutput> map3 = this.allotForRatio(pkhWebUris.subList(20, pkhWebUris.size()-1), 
						subsidyAmount.multiply(ratio_20));
				txOutputs.putAll(map3);
			}
			return txOutputs;
		}
		return null;
	}
	
	/** 按比例分配
	 * 
	 * @param counter 网址总量
	 * @param webUris 
	 * @param subsidyAmount 能够分配的数量
	 * @return
	 */
	private Map<String, TxOutput> allotForRatio(List<PKHWebUri> webUris, BigDecimal subsidyAmount) {
		int counter = 0;//总量记数器
		Map<String, TxOutput> txOutputs = Maps.newHashMap();
		for (PKHWebUri webUri : webUris) {
			counter += webUri.getLength();
		}
		for (PKHWebUri webUri : webUris) {
			float ratio = webUri.getLength() / counter;
			//奖励数额
			BigDecimal amount = subsidyAmount.multiply(new BigDecimal(ratio));
			String pubKeyHash = webUri.getPubKeyHash();
			TxOutput txOutput = TxOutput.newTxOutput(amount, Base64Util.toBinary(pubKeyHash));
			txOutputs.put(pubKeyHash, txOutput);
		}
		return txOutputs;
	}
	
	/** 前三分配
	 * 
	 * @param groupResult
	 * @param subsidyAmount
	 * @return
	 */
	private Map<String, TxOutput> allotAll(Map<String, WebUri[]> groupResult, BigDecimal subsidyAmount) {
		//按各自比例分配
		int vLen = groupResult.values().size();
		Map<String, TxOutput> txOutputs = Maps.newHashMap();
		for(Map.Entry<String, WebUri[]> entry : groupResult.entrySet()) {
			int len = entry.getValue().length;
			float ratio = len / vLen;
			//奖励数额
			BigDecimal amount = subsidyAmount.multiply(new BigDecimal(ratio));
			TxOutput txOutput = TxOutput.newTxOutput(amount, Base64Util.toBinary(entry.getKey()));
			txOutputs.put(entry.getKey(), txOutput);
		}
		return txOutputs;
	}
	
	/** 转化为PKHWebUri集合
	 * 
	 * @param groupResult
	 * @return
	 */
	private List<PKHWebUri> convert(Map<String, WebUri[]> groupResult) {
		List<PKHWebUri> list = Lists.newArrayList();
		for(Map.Entry<String, WebUri[]> entry : groupResult.entrySet()) {
			PKHWebUri e = new PKHWebUri(entry.getKey(), entry.getValue());
			list.add(e);
		}
		return list;
	}
	
	protected class PKHWebUri {
		
		private String pubKeyHash;
		private WebUri[] webUris;
		private int length;
		
		public PKHWebUri(String pubKeyHash, WebUri[] webUris) {
			this.pubKeyHash = pubKeyHash;
			this.webUris = webUris;
			this.length = this.webUris.length;
		}
		
		public String getPubKeyHash() {
			return pubKeyHash;
		}
		public void setPubKeyHash(String pubKeyHash) {
			this.pubKeyHash = pubKeyHash;
		}
		public WebUri[] getWebUris() {
			return webUris;
		}
		public void setWebUris(WebUri[] webUris) {
			this.webUris = webUris;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
	}
	
}
