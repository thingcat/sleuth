package com.sleuth.core.socket.client;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.TimerTask;

/** 看门狗
 * 
 * @author Jonse
 * @date 2019年12月23日
 */
public abstract class ReconnetWatchdog extends ChannelInboundHandlerAdapter implements TimerTask, ChannelHandlerHolder {

}
