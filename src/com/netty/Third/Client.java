package com.netty.Third;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.string.StringEncoder;
/**
 * ���ǿͻ��˺ͷ���˻�������תΪString����
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
			// �ȴ��ͻ�����·�ر�
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
public class ClientInternal extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			//arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));//�������еĽ�����
			arg0.pipeline().addLast(new Encoder());//תΪ�ַ�������
			arg0.pipeline().addLast(new Decord());//תΪ�ַ�������
			//arg0.pipeline().addLast(new StringEncoder());//תΪ�ַ�������
			arg0.pipeline().addLast(new ClientHandler());
		}
	}
}