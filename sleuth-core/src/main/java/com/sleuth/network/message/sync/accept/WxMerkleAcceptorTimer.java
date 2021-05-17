package com.sleuth.network.message.sync.accept;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleuth.block.dto.WebUriDTO;
import com.sleuth.block.dto.WxMerkleDTO;
import com.sleuth.block.schema.WebUri;
import com.sleuth.block.schema.WxMerkle;
import com.sleuth.block.weburi.WebUriManager;
import com.sleuth.block.weburi.WxMerkleManager;
import com.sleuth.core.socket.server.CLI;
import com.sleuth.core.utils.CopyUtil;
import com.sleuth.network.message.protocol.ApProtocol;
import com.sleuth.network.message.protocol.CmdType;
import com.sleuth.network.message.protocol.TabProtocol;

import io.netty.channel.Channel;

/** 默克尔树同步受理者
 * 
 * @author Jonse
 * @date 2021年5月15日
 */
public class WxMerkleAcceptorTimer extends TimerTask {

	static final int MAX_BROADCAST_SIZE = 128;
	
	private Channel channel;
	private String merkleRoot;
	private WxMerkleManager merkleManager;
	private WebUriManager webUriManager;
	
	public WxMerkleAcceptorTimer(ApProtocol message, Channel channel, WxMerkleManager merkleManager, WebUriManager webUriManager) {
		this.channel = channel;
		JSONObject data = JSON.parseObject(message.getArgs());
		this.merkleRoot = data.getString("wxRoot");
		this.merkleManager = merkleManager;
		this.webUriManager = webUriManager;
	}

	@Override
	public void run() {
		WxMerkle merkle = this.merkleManager.get(this.merkleRoot);
		if (merkle != null) {
			WxMerkleDTO[] merkles = this.group(merkle);
			for (WxMerkleDTO e : merkles) {
				TabProtocol table = TabProtocol.newProtocol(CmdType.WX_MERKLE_SYNC);
				table.setData(JSON.toJSONString(e));
				CLI.push(this.channel, table);
				try {
					TimeUnit.MILLISECONDS.sleep(100);//暂停100毫秒
				} catch (InterruptedException ex) {
				}
			}
			
			//发送完成后，发送WebUri数据
			for (String wxId : merkle.getWxIds()) {
				WebUri webUri = this.webUriManager.findById(wxId);
				if (webUri != null) {
					WebUriDTO e = new WebUriDTO(webUri);
					TabProtocol table = TabProtocol.newProtocol(CmdType.WEBURI_SYNC);
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
	private WxMerkleDTO[] group(WxMerkle merkle) {
		WxMerkleDTO[] merkles = {};
		String[] wxIds = merkle.getWxIds();
		if (wxIds != null && wxIds.length > 0) {
			int size = wxIds.length;
			if (size > MAX_BROADCAST_SIZE) {
				for(int i=0; i<size-MAX_BROADCAST_SIZE; i+=MAX_BROADCAST_SIZE) {
					String[] items = ArrayUtils.subarray(wxIds, i, i+MAX_BROADCAST_SIZE);
					WxMerkleDTO e = new WxMerkleDTO();
					e.setWxIds(items);
					e.setHash(merkle.getHash());
					e.setCreateAt(merkle.getCreateAt());
					e.setHasNext(false);
					merkles = ArrayUtils.add(merkles, e);
				}
			} else {
				WxMerkleDTO e = CopyUtil.copyProperty(merkle, WxMerkleDTO.class);
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
