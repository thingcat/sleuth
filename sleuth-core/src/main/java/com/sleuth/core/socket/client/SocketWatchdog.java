package com.sleuth.core.socket.client;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.server.CLI;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.Timeout;
import io.netty.util.Timer;

/** 断线重连监控
 * 
 * @author Jonse
 * @date 2019年12月18日
 */
@Sharable
public abstract class SocketWatchdog extends ReconnetWatchdog {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	static final String WSS = "wss";
	static final int max_connect = 2;
	
	//重连次数
    private volatile int attempts = max_connect;
    
    private final EventLoopGroup loopGroup;
    private final Bootstrap bootstrap;  
    private final ChannelFutureListener channelListener;
    private final WebSocketClientAdapter socketAdapter;
    
    private final Timer timer;
    private final URI uri;
    
    public SocketWatchdog(EventLoopGroup loopGroup, Bootstrap bootstrap, Timer timer, URI uri, 
    		ChannelFutureListener channelListener, WebSocketClientAdapter socketAdapter) {
    	this.loopGroup = loopGroup;
    	this.bootstrap = bootstrap;
    	this.timer = timer;
    	this.uri = uri;
    	this.channelListener = channelListener;
    	this.socketAdapter = socketAdapter;
    }
    
    /** 
     * channel链路每次active的时候，将其连接的次数重新指向0 
     */  
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
    	this.attempts = max_connect; 
    	logger.warn("The current link has been activated and the number of reconnection attempts is reset to {}.", this.attempts);  
        super.channelActive(ctx);
    }
	
    /** 连接每次关闭的时候，触发该交易
     * 
     */
    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	super.channelInactive(ctx);
    	CLI.set(null);
    	this.attempts--;
    	if (this.attempts >= 0) {
    		logger.warn("The server({}) refused to connect. remaining reconnection times {}", this.uri, this.attempts);
    		//5秒进后行重新连接
    		this.timer.newTimeout(this, 2000, TimeUnit.MILLISECONDS);
		} else {
			//取消对该节点的连接，转向下一个节点
			this.attempts = max_connect; 
			this.timer.stop();
			ctx.close();
			this.loopGroup.shutdownGracefully();
			this.socketAdapter.onFailure(this.uri);
			logger.warn("=============================REFUSE - {}====================================\n", this.uri);
		}
    }
    
    @Override
    public void run(Timeout timeout) throws Exception {  
        ChannelFuture future;  
        //bootstrap已经初始化好了，只需要将handler填入就可以了  
        synchronized (bootstrap) { 
        	
        	int port = uri.getPort() == -1 ? 443 : uri.getPort();
        	
            bootstrap.handler(new ChannelInitializer<Channel>() {  
                @Override  
                protected void initChannel(Channel channel) throws Exception {  
                	ChannelPipeline pipeline = channel.pipeline();
					SslContext sslContext = null;
					if (WSS.equals(uri.getScheme())) {
						sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
						SslHandler sslHandler = sslContext.newHandler(channel.alloc(), uri.getHost(), port);
			        	pipeline.addLast(sslHandler);
					}
			        pipeline.addLast(handlers());
                }  
            });  
            future = bootstrap.connect(this.uri.getHost(), port);  
        }  
        //future对象  
        future.addListener(channelListener); 
    }  
    
}
