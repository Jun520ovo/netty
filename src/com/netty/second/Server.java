package com.netty.second;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
/**
 * 这是客户端和服务端互发100条的  有点毛病 但是用了个换行的解决了
*/
public class Server {
	public static void main(String[] args) throws Exception{
		new Server().bind(1111);
	}
	
	public void bind(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();// 异步线程池
		EventLoopGroup workerGroup = new NioEventLoopGroup();// 异步线程池
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup) // 添加工作线程组
					.channel(NioServerSocketChannel.class) // 设置管道模式
					.option(ChannelOption.SO_BACKLOG, 1024) // 配置BLOCK大小
					.childHandler(new ServerInternal());// 我们自己处理的类
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	public class ServerInternal extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {//這裡是寫編碼器 解碼器的
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//用来换行的解码器
			arg0.pipeline().addLast(new ServerHandler());
		}
	}
}
