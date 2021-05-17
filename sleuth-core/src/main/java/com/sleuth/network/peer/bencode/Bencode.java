package com.sleuth.network.peer.bencode;

/**
 * @author 光蓝Comet
 * @description 一句话说明这个类的作用
 * @date 2018/8/21
 */
public interface Bencode {
	
    public abstract byte[] bencode(Object obj) throws Exception;
    
}
