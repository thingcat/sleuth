package com.sleuth.network.message.push;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.server.CLI;
import com.sleuth.network.message.TableProtocolConsumer;
import com.sleuth.network.message.protocol.TabProtocol;
import com.sleuth.network.peer.PeerNetwork;
import com.sleuth.network.schema.NodeInfo;

import io.netty.channel.Channel;

/** 拦截需要广播的对象
 * 
 * @author Jonse
 * @date 2021年1月18日
 */
public class BroadcastMethodInterceptor implements MethodInterceptor {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static String host_name = "Unknown";
	
	public BroadcastMethodInterceptor() {
		try {
			String lable = InetAddress.getLocalHost().getHostName();
			if (lable != null) {
				host_name = lable;
			}
		} catch (UnknownHostException e) {
		}
	}
	
	@Resource
	private PeerNetwork peerNetwork;
	@Resource
	private TableProtocolConsumer tableConsumer;
	
	@Override
	public Object invoke(MethodInvocation invoke) throws Throwable {
		Method method = invoke.getMethod();
		//方法执行结果
		Object invokeResult = invoke.proceed();
		//是否需要广播
		if (method.isAnnotationPresent(Broadcast.class)) {
			if (invokeResult != null && invokeResult instanceof TabProtocol) {
				Broadcast broadcast = method.getAnnotation(Broadcast.class);
				TabProtocol message = (TabProtocol) invokeResult;
				String uid = message.getUid();
				logger.debug("BROADCAST =>> ch={}, ds={}, uid={}", message.getCh(), broadcast.ds(), uid);
				if (uid == null || !this.tableConsumer.exists(uid)) {
					NodeInfo localNodeinfo = this.peerNetwork.getLocalNodeInfo();
					if (localNodeinfo == null) {
						message.setBc(host_name);
					} else {
						message.setBc(localNodeinfo.getLable());
					}
					//推送消息
					this.pushAndFlush(message);
					//加入阻断传播链路
					this.tableConsumer.joinUidBuffer(uid);
				}
			}
		}
		return invokeResult;
	}

	/** 广播数据
	 * 
	 * @param message
	 */
	private void pushAndFlush(TabProtocol message) {
		Channel channel = CLI.get();
		if (channel != null) {
			CLI.push(channel, message);
		}
		CLI.push(message);
	}
	
}
