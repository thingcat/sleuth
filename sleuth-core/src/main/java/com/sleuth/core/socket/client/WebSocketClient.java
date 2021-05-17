package com.sleuth.core.socket.client;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.WebSocket;
import com.sleuth.core.socket.server.CLI;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

/** 客户端连接通道
 * 
 * @author Jonse
 * @date 2019年12月23日
 */
public class WebSocketClient implements WebSocket {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final String WSS_PROTOCOL = "WSS";
	
	//共用一个loopGroup
	private final EventLoopGroup loopGroup = new NioEventLoopGroup();
	private final Bootstrap bootstrap;
	
	//消息处理
	private final WebSocketClientAdapter adapterService;
	
	//重连定时器
	final Timer timer = new HashedWheelTimer();
	final ChannelConnetListener connetListener = new ChannelConnetListener();
	
	private final URI uri;
	
	public WebSocketClient(URI uri, WebSocketClientAdapter adapterService) {
		this.uri = uri;
		this.adapterService = adapterService;
		
		this.bootstrap = new Bootstrap();
		this.bootstrap.group(loopGroup)
        	.channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_KEEPALIVE, true);
	}
	
	/** 连接服务器
	 * 
	 * @throws Exception
	 */
	@Override
	public void service() {
		
		logger.warn("=============================CONNECT - {}====================================\n", this.uri);
		
		//重连监控狗
		final SocketWatchdog watchdog = new SocketWatchdog(this.loopGroup, this.bootstrap, this.timer, this.uri, 
				this.connetListener, this.adapterService) {

			@Override
			public ChannelHandler[] handlers() {
				return new ChannelHandler[] {
					this,
					//日志打印
					new LoggingHandler(LogLevel.INFO),
					//设置心跳包间隔时长
					new IdleStateHandler(0, 15, 0, TimeUnit.SECONDS),
					//心跳包触发器
					new SocketIdleStateTrigger(adapterService),
					// 将请求与应答消息编码或者解码为HTTP消息
					new HttpClientCodec(),
					new HttpObjectAggregator(8192),
					WebSocketClientCompressionHandler.INSTANCE,
					//自定义业务处理
					initChannelInboundHandler(uri, adapterService)
				};
			}
			
		};
		
		//进行连接
		ChannelFuture future;
		
        synchronized (this.bootstrap) { 
        	
        	future = this.bootstrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel channel) throws Exception {
					//初始化socket通道
					ChannelPipeline pipeline = channel.pipeline();
					SslContext sslContext = null;
					if (WSS_PROTOCOL.equalsIgnoreCase(uri.getScheme())) {
						sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
						SslHandler sslHandler = sslContext.newHandler(channel.alloc(), uri.getHost(), uri.getPort());
			        	pipeline.addLast(sslHandler);
					}
			        pipeline.addLast(watchdog.handlers());
				}
			}).connect(uri.getHost(), uri.getPort());  
        } 
        
        // 以下代码在synchronized同步块外面是安全的  
        
        //增加连接监听
        future.addListener(this.connetListener);  
        try {
        	//等待建立通道
        	future.sync().channel();
			//等待握手成功
	        //clientHandler.handshakeFuture().sync();
	        //System.out.println("握手成功");
		} catch (Exception e) {
			logger.error("Waiting for connection failed, error: {}", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			//在SocketWatchdog类里面关闭掉
			//this.loopGroup.shutdownGracefully();
		}
	}
	
	public class ChannelConnetListener implements ChannelFutureListener {
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			//如果重连失败，则调用ChannelInactive方法，再次出发重连事件  
	        if (!future.isSuccess()) {
	        	CLI.set(null);
	        	ChannelPipeline pipeline = future.channel().pipeline();
	        	//此方法会触发channelInactive
	        	pipeline.fireChannelInactive();
	        	logger.warn("The connect to host for the first time, try to reconnect."); 
	        } else {  
	        	logger.warn("=============================OK - {}====================================\n", uri);
	        	Channel channel = future.channel();
	        	CLI.set(channel);
	        	adapterService.onSuccess(channel);
	        }  
		}
		
	}
	
	/** 初始化连接通道
	 * 
	 * @param uri
	 * @param adapterService
	 * @return
	 */
	public SimpleChannelInboundHandler<Object> initChannelInboundHandler(URI uri, WebSocketClientAdapter adapterService) {
		WebSocketClientHandshaker clientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(
        		uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        return new ClientChannelInboundHandler(clientHandshaker, adapterService);
	}

	@Override
	public void destroy() {
		Channel channel = CLI.get();
		if (channel != null) {
			channel.close();
			CLI.set(null);
		}
		this.loopGroup.shutdownGracefully();
	}

}
