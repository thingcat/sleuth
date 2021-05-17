package com.sleuth.core.socket.server;

import java.net.InetSocketAddress;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleuth.core.socket.HttpHandler;
import com.sleuth.core.socket.WebSocket;
import com.sleuth.core.socket.http.HttpRequestHandler;
import com.sleuth.core.socket.http.HttpResponseHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/** websocket通信
 * 
 * @author Administrator
 *
 */
public class WebSocketServer implements WebSocket {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	final WebSocketServerAdapter webSocketAdapter;
	final HttpHandler<FullHttpRequest> httpRequestHandler;
	final HttpHandler<FullHttpResponse> httpResponseHandler;
	
	private WebSocketServerHandshakerFactory handshakerFactory;
	
	//服务端要建立两个group，一个负责接收客户端的连接，一个负责处理数据传输	
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	private final ServerBootstrap bootstrap;
	private final URI uri;
	
	public WebSocketServer(URI uri, WebSocketServerAdapter webSocketAdapter) {
		//连接处理group				
		this.bossGroup = new NioEventLoopGroup();				
		//事件处理group				
		this.workerGroup = new NioEventLoopGroup();				
		this.bootstrap = new ServerBootstrap();
		
		this.httpRequestHandler = new HttpRequestHandler();
		this.httpResponseHandler = new HttpResponseHandler();
		
		this.uri = uri;
		this.webSocketAdapter = webSocketAdapter;
	}
	
	@Override
	public void service() {
		
		int port = this.uri.getPort();
		String path = this.uri.getPath();
		
		//构建握手工厂
		handshakerFactory = new WebSocketServerHandshakerFactory(this.uri.toString(), null, false);
		logger.info("Start WebSocket Server at {} port...........", port);
		
		// 绑定处理group
		bootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.DEBUG))
			.localAddress(new InetSocketAddress(port))
			.childHandler(new ChannelInitializer<SocketChannel>() {//处理新连接
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					//HttpRequestDecoder和HttpResponseEncoder的一个组合，针对http协议进行编解码
					pipeline.addLast("httpServerCodec", new HttpServerCodec());
					// 分块向客户端写数据，防止发送大文件时导致内存溢出， channel.write(new ChunkedFile(new File("bigFile.mkv")))
					pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
					// 将HttpMessage和HttpContents聚合到一个完成的 FullHttpRequest或FullHttpResponse中,具体是FullHttpRequest对象还是FullHttpResponse对象取决于是请求还是响应
                    // 需要放到HttpServerCodec这个处理器后面
					pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(10240));
					// webSocket 数据压缩扩展，当添加这个的时候WebSocketServerProtocolHandler的第三个参数需要设置成true
					pipeline.addLast(new WebSocketServerCompressionHandler());
					// 聚合 websocket 的数据帧，因为客户端可能分段向服务器端发送数据
					// https://github.com/netty/netty/issues/1112 https://github.com/netty/netty/pull/1207
					pipeline.addLast(new WebSocketFrameAggregator(10 * 1024 * 1024));
					// 服务器端向外暴露的 web socket 端点，当客户端传递比较大的对象时，maxFrameSize参数的值需要调大
					pipeline.addLast("webSocketoServerProtocolHandler", new WebSocketServerProtocolHandler(path, null, true, 10485760));
					// 自定义处理器 - 处理 web socket 文本消息
					pipeline.addLast("webSocketHandler", new ServerChannelInboundHandler(webSocketAdapter, handshakerFactory, httpRequestHandler, httpResponseHandler));
					// 自定义处理器 - 处理 web socket 二进制消息
//					pipeline.addLast("binaryWebSocketFrameHandler", new BinaryWebSocketFrameHandler());
					
				}
				
			}).option(ChannelOption.SO_BACKLOG, 1024)//保持连接数的个数
			.option(ChannelOption.TCP_NODELAY, true)//有数据立即发送
			.childOption(ChannelOption.SO_KEEPALIVE, true);//保持连接;
		
		try {
			//绑定端口，同步等待成功
			ChannelFuture future = bootstrap.bind(port).sync();
			if (future.isSuccess()) {
				logger.info("WebSocket start listen at ==>> {}", this.uri.toString());
				this.webSocketAdapter.onSuccess(future.channel());
			} else {
				logger.warn("WebSocket startup failed");
			}
			//等待服务监听端口关闭,就是由于这里会将线程阻塞
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("Startup Err: ", e);
		} finally {
			//优雅地退出，释放线程池资源
			this.bossGroup.shutdownGracefully();
			this.workerGroup.shutdownGracefully();
			logger.warn("Turn off WebSocket service!");
		}
		
	}

	@Override
	public void destroy() {
		CLI.clear();
		this.bossGroup.shutdownGracefully();
		this.workerGroup.shutdownGracefully();
		logger.warn("Turn off WebSocket service!");
	}

}
