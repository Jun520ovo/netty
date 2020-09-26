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
 * ��������ͨ�Ŀͻ��˺ͷ���˻�����Ϣ
 * 
 */
public class Server {
	public static void main(String[] args) throws Exception {
		new Server().bind(1111);
	}

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();// �첽�̳߳�
		EventLoopGroup workerGroup = new NioEventLoopGroup();// �첽�̳߳�

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup) // ��ӹ����߳���
					.channel(NioServerSocketChannel.class) // ���ùܵ�ģʽ
					.option(ChannelOption.SO_BACKLOG, 1024) // ����BLOCK��С
					.childHandler(new ServerInternal());// �����Լ��������

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
