package com.sleuth.network.peer.hash;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author 光蓝Comet
 * @description 一句话说明这个类的作用
 * @date 2018/8/21
 */
public class NodeId {

    private BigInteger nodeId;
    
    /**
     * 初始化NodeId
     */
    public NodeId(){
        Integer random = new Random().nextInt(256);
        byte[] bt = SHA1.encodeBytes(random.toString());
        byte[] dest = new byte[bt.length + 1];
        System.arraycopy(bt, 0, dest, 1, bt.length);
        this.nodeId = new BigInteger(dest);
    }
    
    public NodeId(BigInteger nodeId) {
    	this.nodeId = nodeId;
    }

    public BigInteger getNid() {
        return nodeId;
    }
    
}
