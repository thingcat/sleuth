package com.sleuth.block;

import java.util.List;

import com.sleuth.block.dto.BlockDTO;
import com.sleuth.network.message.protocol.TabProtocol;

/** 区块缓存池
 * 
 * <p>接收来自其它节点的区块</p>
 * 
 * @author Jonse
 * @date 2021年1月30日
 */
public interface BlockBuffer {
	
	/** 预加载 
	 * 
	 */
	public abstract void onPreloading();
	
	/** 重新放入缓冲池
	 * 
	 * @param block
	 */
	public abstract void repush(BlockDTO block);
	
	/** 加入缓冲池
	 * 
	 * @param message
	 * @return
	 */
	public abstract void push(TabProtocol message);
	
	/** 弹出缓冲层
	 * 
	 * @return
	 */
	public abstract BlockDTO poll();
	
	/** 缓冲池大小
	 * 
	 * @return
	 */
	public abstract int size();
	
	/** 获取缓冲池的区块
	 * 
	 * @param page
	 * @return
	 */
	public abstract List<BlockDTO> getBlocks(int page, int limit);
	
	/** 获取指定TAPP缓冲池的区块
	 * 
	 * @param tappHash
	 * @return
	 */
	public abstract List<BlockDTO> getBlocks(String tappHash);
}
