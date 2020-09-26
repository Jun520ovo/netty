package com.netty.second;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
/**
 * 这是客户端和服务端互发100条的  有点毛病 但是用了个换行的解决了
*/
public class Client {
	EventLoopGroup group = new NioEventLoopGroup();

	public static void main(String[] args) throws Exception {
		new Client().connect(1111, "127.0.0.1");
	}

	public void connect(int port, String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ClientInternal());
			ChannelFuture f = b.connect(host, port).sync();
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public class ClientInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//用来换行的解码器
			arg0.pipeline().addLast(new ClientHandler());
			
		}

	}

}
