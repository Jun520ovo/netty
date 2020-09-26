package com.nettyFile.ThreadOneFile;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * ���ļ� ����ļ�
 */
public class Server {
	public static void main(String[] args) throws Exception {
		new Server().bind(8889);
	}

	

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();// �첽�̳߳�
		EventLoopGroup workerGroup = new NioEventLoopGroup();// �첽�̳߳�

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup) // ��ӹ����߳���
					.channel(NioServerSocketChannel.class) // ���ùܵ�ģʽ
					.option(ChannelOption.SO_BACKLOG, 1024) // ����BLOCK��С
					.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))//����buffer��������С
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
		protected void initChannel(SocketChannel arg0) throws Exception {// �@�e�ǌ����a�� ��a����
			
			// arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//�������еĽ�����
			// arg0.pipeline().addLast(new Decord());//�ַ�����
			// arg0.pipeline().addLast(new Encoder());//�ַ�����
			 arg0.pipeline().addLast(new ByteArrayEncoder());//���ǲ���Ҫ�����  ����ȥ���Ǹ���
			 arg0.pipeline().addLast(new Decoder());
			 arg0.pipeline().addLast(new StringEncoder());//�ַ�����
			 arg0.pipeline().addLast(new ServerHandler());
		}
	}
}
