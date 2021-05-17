package com.sleuth.block.subsidy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.sleuth.block.schema.TxOutput;
import com.sleuth.block.schema.WebUri;
import com.sleuth.core.utils.Base64Util;

/** 创币奖励规则，分三个阶段奖励完成
 * 
 * <p>奖励上限：98000000</p>
 * <table>
 * 	<tr>
 * 		<th>阶段</th>
 * 		<th>奖励上限</th>
 * 		<th>区块奖励</th>
 * 		<th>区块高度</th>
 * 	</tr>
 * 	<tr>
 * 		<td>阶段一</td>
 * 		<td>73584000</td>
 * 		<td>1400/区块</td>
 * 		<td>0 ~ 52560</td>
 * 	</tr>
 * 	<tr>
 * 		<td>阶段二</td>
 * 		<td>18144000</td>
 * 		<td>700/区块</td>
 * 		<td>52561 ~ 78481</td>
 * 	</tr>
 * 	<tr>
 * 		<td>阶段三</td>
 * 		<td>6272000</td>
 * 		<td>200/区块</td>
 * 		<td>78482 ~ 109842</td>
 * 	</tr>
 * <table>
 * @author Jonse
 * @date 2021年5月13日
 */
public abstract class SubsidyRule {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** 奖励上限9千8百万枚   */
	static final BigDecimal SUBSIDY_UPPER_LIMIT = new BigDecimal("98000000.00");
	
	/** 阶段一挖矿奖励数量  */
	protected static final BigDecimal STAGE_ONE_SUBSIDY = new BigDecimal("1400.00");
	/** 阶段二挖矿奖励数量  */
	protected static final BigDecimal STAGE_TWO_SUBSIDY = new BigDecimal("700.00");
	/** 阶段三挖矿奖励数量  */
	protected static final BigDecimal STAGE_THREE_SUBSIDY = new BigDecimal("200.00");
	
	/** 奖励数额
	 * 
	 * @param height
	 * @return
	 */
	protected BigDecimal subsidyAmount(Long height) {
		//阶段一挖矿奖励，区块高度在 0 ~ 52560
		if (height >= 0 && height <= 52560) {
			return STAGE_ONE_SUBSIDY;
		}
		//阶段二挖矿奖励，区块高度在 52561 ~ 78481
		else if (height >= 52561 && height <= 78481) {
			return STAGE_TWO_SUBSIDY;
		}
		//阶段三挖矿奖励，区块高度在 78482 ~ 109842
		else if (height >= 78482 && height <= 109842) {
			return STAGE_THREE_SUBSIDY;
		}
		//默认返回0
		return new BigDecimal("0.00");
	}
	
	/** 根据 PubKeyHash 分组
	 * 
	 * @param webUris
	 * @return
	 */
	protected Map<String, WebUri[]> groupPubKeyHash(WebUri[] webUris) {
		Map<String, WebUri[]> maps = Maps.newTreeMap();
		for (WebUri webUri : webUris) {
			byte[] pubKeyHash = webUri.getPubKeyHash();
			String key = Base64Util.toString(pubKeyHash);
			WebUri[] uris = maps.get(key);
			if (uris == null) {
				maps.put(key, new WebUri[]{webUri});
			} else {
				uris = ArrayUtils.add(uris, webUri);
				maps.put(key, uris);
			}
		}
		return maps;
	}
	
	/** 计算奖励
	 * 
	 * @param height
	 * @param webUris
	 * @return
	 */
	public abstract Map<String, TxOutput> compute(Long height, WebUri[] webUris);

	/** 奖励分配
	 * 
	 * @param height
	 * @param webUris
	 * @return
	 */
	protected List<TxOutput> subsidyAllot(Long height, Map<String, WebUri[]> webUris) {
		//获得挖矿奖励数量 
		BigDecimal subsidyAmount = this.subsidyAmount(height);
		if (subsidyAmount.longValue() > 0) {
			//1-3名获得50%奖励
			//4-20名获得30%奖励
			//剩下的获得20%奖励
			int len = webUris.size();
			//可完全分配
			if (len > 20) {
				
			}
			//刚好分配
			else if (len > 3 && len < 20) {
				
			}
			//前三分配
			else if (len <= 3) {
				
			}
		}
		return null;
	}
	
}
