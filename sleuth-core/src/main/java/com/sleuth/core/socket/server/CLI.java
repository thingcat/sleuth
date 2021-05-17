package com.sleuth.core.socket.server;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sleuth.core.socket.message.Protocol;
import com.sleuth.network.schema.NodeInfo;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/** 客户端
 * 
 * @author Administrator
 *
 */
public final class CLI {
	
	/** 连接后的通道
	 * 
	 */
	static Channel SERVER_CHANNEL;
	
	/** 所有连接 */
	static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	/** 订阅组 */
	static final Map<String, ChannelGroup> subscribe = Maps.newHashMap();
	
	/** 返回所有连接通道迭代器
	 * 
	 * @return
	 */
	public static Iterator<Channel> iterator() {
		if (channelGroup.size() > 0) {
			return channelGroup.iterator();
		}
		return null;
	}
	
	/** 寻找连接通道
	 * 
	 * @param nodeInfo
	 * @return
	 */
	public static Channel findChannel(NodeInfo nodeInfo) {
		if (channelGroup.size() > 0) {
			Iterator<Channel> iterator = channelGroup.iterator();
			while(iterator.hasNext()) {
				Channel e = iterator.next();
				InetSocketAddress socketAddress = (InetSocketAddress) e.remoteAddress();
				String hostAddress = socketAddress.getAddress().getHostAddress();
				URI uri = nodeInfo.toUri();
				if (uri.getHost().equals(hostAddress)) {
					return e;
				}
			}
		}
		return null;
	}
	
	/** 添加新连接
	 * 
	 * @param e
	 */
	public static void add(Channel e) {
		channelGroup.add(e);
	}
	
	/** 删除一个链接
	 * 
	 * @param e
	 */
	public static void remove(Channel e) {
		channelGroup.remove(e);
		//删除订阅的内容
		for(Map.Entry<String, ChannelGroup> entry : subscribe.entrySet()) {
			ChannelGroup group = entry.getValue();
			group.remove(e);
		}
	}
	
	/** 清除所有的连接
	 * 
	 */
	public static void clear() {
		channelGroup.clear();
		//清除订阅的内容
		for(Map.Entry<String, ChannelGroup> entry : subscribe.entrySet()) {
			ChannelGroup group = entry.getValue();
			group.clear();
		}
	}
	
	/** 注册一个订阅组
	 * 
	 * @param name
	 */
	public static void register(String subname) {
		if (!subscribe.containsKey(subname)) {
			subscribe.put(subname, new DefaultChannelGroup(GlobalEventExecutor.INSTANCE));
		}
	}
	
	/** 添加到指定的订阅组，返回当前总数量
	 * 
	 * @param subname
	 * @param channel
	 */
	public static int add(String subname, Channel e) {
		ChannelGroup group = subscribe.get(subname);
		if (group != null) {
			group.add(e);
			return group.size();
		}
		return 0;
	}
	
	/** 取消订阅
	 * 
	 * @param subname
	 * @param channel
	 */
	public static void remove(String subname, Channel channel) {
		ChannelGroup group = subscribe.get(subname);
		if (group != null) {
			group.remove(channel);
		}
	}
	
	//-------------------------------------------------
	
	/** 向订阅组推送消息
	 * 
	 * @param message <T extends Protocol>
	 * @return
	 */
	public static <T extends Protocol> int push(T message) {
		Protocol protocol = message;
		return CLI.push(protocol.getCh(), new TextWebSocketFrame(JSON.toJSONString(message)));
	}
	
	/** 向订阅组推送消息
	 * 
	 * @param message
	 * @param filterId 需要过滤的
	 * @return
	 */
	public static <T extends Protocol> int push(T message, String filterId) {
		Protocol protocol = message;
		return CLI.push(protocol.getCh(), new TextWebSocketFrame(JSON.toJSONString(message)), filterId);
	}
	
	/** 向指定的连接通道推送消息
	 * 
	 * @param channel
	 * @param message
	 */
	public static <T extends Protocol> void push(Channel channel, T message) {
		CLI.push(channel, new TextWebSocketFrame(JSON.toJSONString(message)));
	}
	
	/** 向订阅组推送消息
	 * 
	 * @param subname
	 * @param message
	 */
	private static int push(String subname, WebSocketFrame message) {
		ChannelGroup group = subscribe.get(subname);
		if (group != null && group.size() > 0) {
			group.writeAndFlush(message);
			return group.size();
		}
		return 0;
	}
	
	private static int push(String subname, WebSocketFrame message, String filterId) {
		ChannelGroup group = subscribe.get(subname);
		if (group != null && group.size() > 0) {
			if (filterId == null) {
				group.writeAndFlush(message);
			} else {
				group.forEach(e->{
					String id = e.id().asShortText();
					if (!id.equals(filterId)) {
						push(e, message);
					}
				});
			}
		}
		return group.size();
	}
	
	/** 向指定的连接通道推送消息
	 * 
	 * @param channel
	 * @param message
	 */
	private static void push(Channel channel, WebSocketFrame message) {
		channel.writeAndFlush(message);
	}
	
	/** 设置连接后的服务端通道
	 * 
	 * @param channel
	 */
	public static void set(Channel channel) {
		SERVER_CHANNEL = channel;
	}
	
	/** 获得连接后的服务端通道
	 * 
	 * @return
	 */
	public static Channel get() {
		return SERVER_CHANNEL;
	}
}
