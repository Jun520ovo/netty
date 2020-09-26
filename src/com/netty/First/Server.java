package com.netty.First;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 这是最普通的客户端和服务端互发消息
 * 
 */
public class Server {
	public static void main(String[] args) throws Exception {
		new Server().bind(1111);
	}

	public void bind(int port) throws Exception {
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

	public class ServerInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			arg0.pipeline().addLast(new ServerHandler());
		}

	}

}
